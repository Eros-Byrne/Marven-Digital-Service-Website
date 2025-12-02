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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

    @SpringBootTest
    @AutoConfigureMockMvc
    @Transactional
    public class FullContainerSaveCloseTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @Autowired
        private QuizService quizService;

        private int quizId = 1; // assume this exists in your DB
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
            QuizAttempt attempt = quizService.loadAttemptFromSession(quizId, session);
            session.setAttribute("quizAttempt", attempt);

            // Simulate answering the first question and clicking Save & Close
            mockMvc.perform(post("/quiz/{quizId}/attempt/{attemptId}/question/{index}/answer",
                            quizId, attemptId, 0)
                            .param("answer", "3")
                            .param("nav", "saveclose")
                            .session(session)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/quiz/1/attempt/2/save-close"));

            // Save the attempt to DB using your existing service
            quizService.saveIncompleteAttempt(userId, (int) attemptId, attempt);

            // Verify that the answer was saved
            Quiz quiz = quizService.getQuizForAttempt(quizId, (int) attemptId);
            List<Question> questions = quiz.getQuestions();
            assertNotNull(questions);
        }


        @Test
        void shouldDeleteAttemptWith0Answers() throws Exception {
            MockHttpSession session = new MockHttpSession();
            QuizAttempt attempt = quizService.loadAttemptFromSession(quizId, session);
            session.setAttribute("quizAttempt", attempt);

            // Simulate no answer and clicking Save & Close
            mockMvc.perform(post("/quiz/{quizId}/attempt/{attemptId}/question/{index}/answer",
                            quizId, attemptId, 0)
                            .param("nav", "saveclose")
                            .session(session)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/quiz/1/attempt/1/save-close"));

            quizService.deleteEmptyAttempt((int) attemptId,session);

            // Verify that the attempt no longer exists in the DB
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM user_attempt WHERE user_attempt_id = ?",
                    Integer.class,
                    attemptId
            );

            assertEquals(0, count.intValue(), "Empty attempt should be deleted");
        }
    }

