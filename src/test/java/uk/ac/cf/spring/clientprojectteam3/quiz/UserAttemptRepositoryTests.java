package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UserAttemptRepositoryTests {
    @Autowired
    private QuizRepositoryImpl quizRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private long userId = 1L;
    private long quizId = 1L;

    @Test
    void createUserAttempt_firstAttempt_shouldHaveAttempt1() {
        long attemptId = quizRepository.createUserAttempt(userId, quizId);

        Integer attemptNumber = jdbcTemplate.queryForObject(
                "SELECT attempt FROM user_attempt WHERE user_attempt_id = ?",
                Integer.class,
                attemptId
        );

        assertEquals(1, attemptNumber.intValue(), "First attempt number should be 1");
    }

    @Test
    void createUserAttempt_secondAttempt_shouldIncrementAttemptNumber() {
        // Create first attempt
        long firstAttemptId = quizRepository.createUserAttempt(userId, quizId);

        // Create second attempt
        long secondAttemptId = quizRepository.createUserAttempt(userId, quizId);

        Integer secondAttemptNumber = jdbcTemplate.queryForObject(
                "SELECT attempt FROM user_attempt WHERE user_attempt_id = ?",
                Integer.class,
                secondAttemptId
        );

        assertEquals(2, secondAttemptNumber.intValue(), "Second attempt number should be 2");
    }

    @Test
    void createUserAttempt_multipleAttempts_shouldIncrementProperly() {
        for (int i = 1; i <= 5; i++) {
            long attemptId = quizRepository.createUserAttempt(userId, quizId);

            Integer attemptNumber = jdbcTemplate.queryForObject(
                    "SELECT attempt FROM user_attempt WHERE user_attempt_id = ?",
                    Integer.class,
                    attemptId
            );

            assertEquals(i, attemptNumber.intValue(), "Attempt number should match iteration");
        }
    }


}
