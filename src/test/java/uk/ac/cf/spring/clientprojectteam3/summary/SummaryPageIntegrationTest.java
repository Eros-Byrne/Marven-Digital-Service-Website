package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for Summary Page - tests the full container with real database
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "test-user", roles = {"USER"})
public class SummaryPageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QuizService quizService;

    private long userId = 1L;
    private long quizId = 1L;
    private long attemptId;

    @BeforeEach
    void setup() {
        // Create a new quiz attempt with some answers
        attemptId = quizService.startAttempt(userId, quizId);

        // Insert test answers
        jdbcTemplate.update(
                "INSERT INTO answer (question_id, user_attempt_id, score) VALUES (?, ?, ?)",
                1L, attemptId, 4
        );
        jdbcTemplate.update(
                "INSERT INTO answer (question_id, user_attempt_id, score) VALUES (?, ?, ?)",
                2L, attemptId, 5
        );
        jdbcTemplate.update(
                "INSERT INTO answer (question_id, user_attempt_id, score) VALUES (?, ?, ?)",
                3L, attemptId, 2
        );

        // Mark attempt as complete
        jdbcTemplate.update(
                "UPDATE user_attempt SET complete = 1 WHERE user_attempt_id = ?",
                attemptId
        );
    }

    @Test
    void summaryPage_shouldLoadSuccessfully() throws Exception {
        MvcResult result = mockMvc.perform(get("/summary/quiz/" + quizId + "/attempt/" + attemptId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("summary"))
                .andExpect(model().attributeExists("quizName"))
                .andExpect(model().attributeExists("overallScore"))
                .andExpect(model().attributeExists("capabilityLabels"))
                .andExpect(model().attributeExists("capabilityScores"))
                .andExpect(model().attribute("hasData", true))
                .andExpect(model().attribute("hasError", false))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Page elements
        assertTrue(content.contains("<canvas id=\"capabilitiesChart\"></canvas>"));
        assertTrue(content.contains("chart.umd.min.js"));
        assertTrue(content.contains("Your Quiz Results"));
        assertTrue(content.contains("Overall Score"));
    }

    @Test
    void summaryPage_shouldDisplayChartDataCorrectly() throws Exception {
        MvcResult result = mockMvc.perform(get("/summary/quiz/" + quizId + "/attempt/" + attemptId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("capabilityLabels"))
                .andExpect(model().attributeExists("capabilityScores"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Chart.js initialization
        assertTrue(content.contains("new Chart("));     // generic, works with ctx or element ID

        // Updated for BAR chart
        assertTrue(content.contains("type: 'bar'"));

        // Data arrays
        assertTrue(content.contains("capabilityLabels"));
        assertTrue(content.contains("capabilityScores"));

        // Chart config range
        assertTrue(content.contains("suggestedMin: 0"));
        assertTrue(content.contains("suggestedMax: 100"));
    }

    @Test
    void summaryPage_shouldDisplayStrengthsAndWeaknesses() throws Exception {
        MvcResult result = mockMvc.perform(get("/summary/quiz/" + quizId + "/attempt/" + attemptId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("strengths"))
                .andExpect(model().attributeExists("weaknesses"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Strengths/weaknesses sections
        assertTrue(content.contains("Key Strengths"));
        assertTrue(content.contains("Needs Improvement"));

        // Capability table
        assertTrue(content.contains("Detailed Results by Capability"));
        assertTrue(content.contains("<th>Capability</th>"));
        assertTrue(content.contains("<th style=\"width: 120px;\">Score</th>"));
        assertTrue(content.contains("<th>Status</th>"));
    }
}
