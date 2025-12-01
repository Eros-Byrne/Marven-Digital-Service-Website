package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
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
        // Add answers by index
        attempt.getAnswers().put(0, 3);
        attempt.getAnswers().put(1, 5);

        quizService.submitAttempt(userId, attemptId, attempt);

        // Verify answers were saved in DB
        Integer scoreQ1 = jdbcTemplate.queryForObject(
                "SELECT score FROM answer WHERE user_attempt_id = ? AND question_id = ?",
                Integer.class,
                attemptId,
                getQuestionId(0)
        );
        Integer scoreQ2 = jdbcTemplate.queryForObject(
                "SELECT score FROM answer WHERE user_attempt_id = ? AND question_id = ?",
                Integer.class,
                attemptId,
                getQuestionId(1)
        );

        assertEquals(3, scoreQ1.intValue());
        assertEquals(5, scoreQ2.intValue());
    }

    private long getQuestionId(int index) {
        // Helper to fetch the questionId from DB for the given quiz and index
        return jdbcTemplate.queryForObject(
                "SELECT question_id FROM quiz_questions WHERE quiz_id = ? ORDER BY question_id LIMIT 1 OFFSET ?",
                Long.class,
                quizId,
                index
        );
    }
}
