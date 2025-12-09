package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

    @SpringBootTest
    @AutoConfigureMockMvc
    @DirtiesContext
    @Transactional
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    public class FullContainerSaveCloseTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @Autowired
        private QuizService quizService;

        private int quizId = 1;
        private long userId = 1L;
        private long attemptId;

        @BeforeEach
        void setup() {
            // start a new attempt
            attemptId = quizService.startAttempt(userId, 1);
            // Load the attempt into a mock session
            QuizAttempt attempt = new QuizAttempt();
            attempt.setQuizId(quizId);
            attempt.setCurrentQuestionIndex(0);
        }

        @Test
        void shouldStore1Answer() throws Exception {
            MockHttpSession session = new MockHttpSession();
            QuizAttempt attempt = quizService.loadAttemptFromSession(quizId, (int) attemptId, session);
            session.setAttribute("quizAttempt", attempt);

            mockMvc.perform(post("/quiz/{quizId}/attempt/question/{index}/answer",
                            quizId, 0)
                            .param("answer", "3")
                            .param("nav", "saveclose")
                            .session(session)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/quiz/1/attempt/save-close"));

            // Save to DB
            quizService.saveIncompleteAttempt(userId, (int) attemptId, attempt);

            // Verify
            Quiz quiz = quizService.getQuizForAttempt(quizId, (int) attemptId);
            List<Question> questions = quiz.getQuestions();
            assertNotNull(questions);
        }

        @Test
        void shouldDeleteAttemptWith0Answers() throws Exception {
            MockHttpSession session = new MockHttpSession();
            QuizAttempt attempt = quizService.loadAttemptFromSession(quizId, (int) attemptId, session);
            session.setAttribute("quizAttempt", attempt);

            mockMvc.perform(post("/quiz/{quizId}/attempt/question/{index}/answer",
                            quizId, attemptId, 0)
                            .param("nav", "saveclose")
                            .session(session)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/quiz/1/attempt/save-close"));

            // Delete attempt
            quizService.deleteEmptyAttempt((int) attemptId, session);

            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM user_attempt WHERE user_attempt_id = ?",
                    Integer.class,
                    attemptId
            );
            assertEquals(0, count.intValue(), "Empty attempt should be deleted");
        }
    }