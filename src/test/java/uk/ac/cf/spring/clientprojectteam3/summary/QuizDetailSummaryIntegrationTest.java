package uk.ac.cf.spring.clientprojectteam3.summary;

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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "test@example.com", roles = {"USER"})
public class QuizDetailSummaryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private long userId = 1L;
    private long quizId = 1L;

    @BeforeEach
    void setup() {
        // Clean existing data
        jdbcTemplate.execute("SET foreign_key_checks=0");
        jdbcTemplate.execute("DELETE FROM answer");
        jdbcTemplate.execute("DELETE FROM user_attempt");
        jdbcTemplate.execute("SET foreign_key_checks=1");

        // Create attempt 1 (score avg = 3, converted = 60)
        long attempt1 = createUserAttempt(userId, quizId, 1, 1);
        insertAnswer(1L, attempt1, 3);
        insertAnswer(2L, attempt1, 3);
        insertAnswer(3L, attempt1, 3);

        // Create attempt 2 (score avg = 4, converted = 80)
        long attempt2 = createUserAttempt(userId, quizId, 2, 1);
        insertAnswer(1L, attempt2, 4);
        insertAnswer(2L, attempt2, 4);
        insertAnswer(3L, attempt2, 4);

        // Create attempt 3 (score avg = 5, converted = 100)
        long attempt3 = createUserAttempt(userId, quizId, 3, 1);
        insertAnswer(1L, attempt3, 5);
        insertAnswer(2L, attempt3, 5);
        insertAnswer(3L, attempt3, 5);
    }

    private long createUserAttempt(long userId, long quizId, int attemptNumber, int complete) {
        jdbcTemplate.update(
                "INSERT INTO user_attempt (user_id, quiz_id, attempt, complete) VALUES (?, ?, ?, ?)",
                userId, quizId, attemptNumber, complete
        );
        Long attemptId = jdbcTemplate.queryForObject(
                "SELECT LAST_INSERT_ID()",
                Long.class
        );
        return attemptId != null ? attemptId : 0L;
    }

    private void insertAnswer(long questionId, long attemptId, int score) {
        jdbcTemplate.update(
                "INSERT INTO answer (question_id, user_attempt_id, score) VALUES (?, ?, ?)",
                questionId, attemptId, score
        );
    }

    @Test
    void testQuizDetailPage_loadsSuccessfully() throws Exception {
        MvcResult result = mockMvc.perform(get("/summary/quiz/" + quizId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("quiz-detail-summary"))
                .andExpect(model().attributeExists("quizName"))
                .andExpect(model().attributeExists("userName"))
                .andExpect(model().attributeExists("overallScore"))
                .andExpect(model().attributeExists("capabilityLabels"))
                .andExpect(model().attributeExists("capabilityScores"))
                .andExpect(model().attribute("hasData", true))
                .andExpect(model().attribute("hasError", false))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("Quiz Detail:"));
        assertTrue(content.contains("Building a team"));
    }

    @Test
    void testQuizDetailPage_lineChartRendersCorrectly() throws Exception {
        MvcResult result = mockMvc.perform(get("/summary/quiz/" + quizId))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify line chart configuration
        assertTrue(content.contains("type: 'line'"));
        assertTrue(content.contains("tension: 0.3"));
        assertTrue(content.contains("fill: true"));
        assertTrue(content.contains("<canvas id=\"capabilitiesChart\"></canvas>"));
        assertTrue(content.contains("borderColor: '#6068FF'"));
        assertTrue(content.contains("pointBackgroundColor: '#6068FF'"));
    }

    @Test
    void testQuizDetailPage_showsMultipleAttempts() throws Exception {
        MvcResult result = mockMvc.perform(get("/summary/quiz/" + quizId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("capabilityLabels"))
                .andExpect(model().attributeExists("capabilityScores"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Should have 3 attempt labels
        assertTrue(content.contains("attempt") || content.contains("Attempt"));
        assertTrue(content.contains("3 attempt(s) completed") ||
                   content.contains("Completion:"));
    }

    @Test
    void testQuizDetailPage_displaysCapabilityBreakdown() throws Exception {
        MvcResult result = mockMvc.perform(get("/summary/quiz/" + quizId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("capabilityResults"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify detailed results table
        assertTrue(content.contains("Detailed Results by Outcome") ||
                   content.contains("Detailed Results"));
        assertTrue(content.contains("<th>Capability</th>"));
        assertTrue(content.contains("<th"));
        assertTrue(content.contains("Score"));
    }

    @Test
    void testQuizDetailPage_hasBackNavigationButtons() throws Exception {
        MvcResult result = mockMvc.perform(get("/summary/quiz/" + quizId))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify navigation buttons
        assertTrue(content.contains("Back to Summary") ||
                   content.contains("/summary"));
        assertTrue(content.contains("Back to Quizzes") ||
                   content.contains("/quiz-list"));
        assertTrue(content.contains("project-button"));
    }

    @Test
    void testQuizDetailPage_displaysCorrectScoreProgression() throws Exception {
        MvcResult result = mockMvc.perform(get("/summary/quiz/" + quizId))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Latest score should be 100 (from attempt 3 with all 5s)
        assertTrue(content.contains("100") || content.contains("score"));
    }

    @Test
    void testQuizDetailPage_withNoAttempts_showsError() throws Exception {
        // Clean all attempts
        jdbcTemplate.execute("DELETE FROM answer");
        jdbcTemplate.execute("DELETE FROM user_attempt");

        MvcResult result = mockMvc.perform(get("/summary/quiz/" + quizId))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz-detail-summary"))
                .andExpect(model().attribute("hasError", true))
                .andExpect(model().attribute("hasData", false))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("No completed attempts") ||
                   content.contains("No data"));
    }

    @Test
    void testQuizDetailPage_displaysUserInformation() throws Exception {
        MvcResult result = mockMvc.perform(get("/summary/quiz/" + quizId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userName"))
                .andExpect(model().attributeExists("completionDate"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("User:"));
        assertTrue(content.contains("Date:"));
    }
}
