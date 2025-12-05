package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class QuizServiceSaveAnswersTest {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private long userId = 1L;
    private int quizId = 1; // Make sure this quiz exists in DB
    private long attemptId;

    @BeforeEach
    void setup() {
        // Start a new attempt
        attemptId = quizService.startAttempt(userId, quizId);
    }

    @Test
    void saveAnswers_shouldPersistAllAnswers() {
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuizId(quizId);

        // Map answers using actual question IDs from DB
        attempt.setAnswers(new HashMap<>());
        long q1Id = getQuestionId(0);
        long q2Id = getQuestionId(1);
        attempt.getAnswers().put(0, 3);
        attempt.getAnswers().put(1, 5);

        // Submit the attempt
        quizService.submitAttempt(userId, attemptId, attempt);

        // Verify answers were saved
        Integer scoreQ1 = jdbcTemplate.queryForObject(
                "SELECT score FROM answer WHERE user_attempt_id = ? AND question_id = ?",
                Integer.class,
                attemptId,
                q1Id
        );
        Integer scoreQ2 = jdbcTemplate.queryForObject(
                "SELECT score FROM answer WHERE user_attempt_id = ? AND question_id = ?",
                Integer.class,
                attemptId,
                q2Id
        );

        assertEquals(3, scoreQ1.intValue());
        assertEquals(5, scoreQ2.intValue());
    }

    private long getQuestionId(int index) {
        // Fetch actual question ID for the quiz
        return jdbcTemplate.queryForObject(
                "SELECT question_id FROM quiz_questions WHERE quiz_id = ? ORDER BY question_id LIMIT 1 OFFSET ?",
                Long.class,
                quizId,
                index
        );
    }
}
