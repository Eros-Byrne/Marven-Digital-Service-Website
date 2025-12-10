package uk.ac.cf.spring.clientprojectteam3.summary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizAttemptScore;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizButtonInfo;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuizDetailSummaryRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QuizRepository quizRepository;

    @BeforeEach
    void setupDatabase() {
        // Clean up existing data
        jdbcTemplate.execute("SET foreign_key_checks=0");
        jdbcTemplate.execute("DELETE FROM answer");
        jdbcTemplate.execute("DELETE FROM user_attempt");
        jdbcTemplate.execute("SET foreign_key_checks=1");

        // Create user attempts for quiz 1, user 100
        // Attempt 1 (completed, avg score 3)
        jdbcTemplate.update("INSERT INTO user_attempt (user_attempt_id, user_id, quiz_id, attempt, complete) VALUES (101, 100, 1, 1, 1)");
        jdbcTemplate.update("INSERT INTO answer (question_id, user_attempt_id, score) VALUES (1, 101, 3)");
        jdbcTemplate.update("INSERT INTO answer (question_id, user_attempt_id, score) VALUES (2, 101, 3)");
        jdbcTemplate.update("INSERT INTO answer (question_id, user_attempt_id, score) VALUES (3, 101, 3)");

        // Attempt 2 (completed, avg score 4)
        jdbcTemplate.update("INSERT INTO user_attempt (user_attempt_id, user_id, quiz_id, attempt, complete) VALUES (102, 100, 1, 2, 1)");
        jdbcTemplate.update("INSERT INTO answer (question_id, user_attempt_id, score) VALUES (1, 102, 4)");
        jdbcTemplate.update("INSERT INTO answer (question_id, user_attempt_id, score) VALUES (2, 102, 4)");
        jdbcTemplate.update("INSERT INTO answer (question_id, user_attempt_id, score) VALUES (3, 102, 4)");

        // Attempt 3 (incomplete, should be ignored)
        jdbcTemplate.update("INSERT INTO user_attempt (user_attempt_id, user_id, quiz_id, attempt, complete) VALUES (103, 100, 1, 3, 0)");
        jdbcTemplate.update("INSERT INTO answer (question_id, user_attempt_id, score) VALUES (1, 103, 5)");

        // Attempt 4 (completed, avg score 5)
        jdbcTemplate.update("INSERT INTO user_attempt (user_attempt_id, user_id, quiz_id, attempt, complete) VALUES (104, 100, 1, 4, 1)");
        jdbcTemplate.update("INSERT INTO answer (question_id, user_attempt_id, score) VALUES (1, 104, 5)");
        jdbcTemplate.update("INSERT INTO answer (question_id, user_attempt_id, score) VALUES (2, 104, 5)");

        // Different quiz (quiz 2)
        jdbcTemplate.update("INSERT INTO user_attempt (user_attempt_id, user_id, quiz_id, attempt, complete) VALUES (105, 100, 2, 1, 1)");
        jdbcTemplate.update("INSERT INTO answer (question_id, user_attempt_id, score) VALUES (4, 105, 4)");
    }

    @Test
    void testGetAllCompletedAttempts_returnsAttemptsInOrder() {
        List<QuizAttemptScore> attempts = quizRepository.getAllCompletedAttempts(100L, 1L);

        assertEquals(3, attempts.size(), "Should return 3 completed attempts");

        // Verify they are in ascending order by attempt number
        assertEquals(1, attempts.get(0).getAttemptNumber());
        assertEquals(2, attempts.get(1).getAttemptNumber());
        assertEquals(4, attempts.get(2).getAttemptNumber()); // Skip 3 because incomplete
    }

    @Test
    void testGetAllCompletedAttempts_ignoresIncompleteAttempts() {
        List<QuizAttemptScore> attempts = quizRepository.getAllCompletedAttempts(100L, 1L);

        // Should not include attempt 3 which has complete=0
        boolean hasIncompleteAttempt = attempts.stream()
                .anyMatch(a -> a.getAttemptNumber() == 3);

        assertFalse(hasIncompleteAttempt, "Should not include incomplete attempts");
    }

    @Test
    void testGetAllCompletedAttempts_calculatesAverageScoreCorrectly() {
        List<QuizAttemptScore> attempts = quizRepository.getAllCompletedAttempts(100L, 1L);

        // Attempt 1: avg(3,3,3) * 20 = 3 * 20 = 60
        assertEquals(60.0, attempts.get(0).getAverageScore(), 0.01);

        // Attempt 2: avg(4,4,4) * 20 = 4 * 20 = 80
        assertEquals(80.0, attempts.get(1).getAverageScore(), 0.01);

        // Attempt 4: avg(5,5) * 20 = 5 * 20 = 100
        assertEquals(100.0, attempts.get(2).getAverageScore(), 0.01);
    }

    @Test
    void testGetAllCompletedAttempts_emptyForNoCompletedAttempts() {
        // User 999 has no attempts
        List<QuizAttemptScore> attempts = quizRepository.getAllCompletedAttempts(999L, 1L);

        assertTrue(attempts.isEmpty(), "Should return empty list for user with no attempts");
    }

    @Test
    void testGetAllCompletedAttempts_emptyForDifferentQuiz() {
        // User 100 has no completed attempts for quiz 99
        List<QuizAttemptScore> attempts = quizRepository.getAllCompletedAttempts(100L, 99L);

        assertTrue(attempts.isEmpty(), "Should return empty list for quiz with no attempts");
    }

    @Test
    void testGetQuizButtonsInfo_showsCompletionStatus() {
        List<QuizButtonInfo> buttons = quizRepository.getQuizButtonsInfo(100L);

        assertNotNull(buttons);
        assertTrue(buttons.size() >= 2, "Should return all quizzes");

        // User 100 has completed quiz 1
        QuizButtonInfo quiz1Button = buttons.stream()
                .filter(b -> b.getQuizId() == 1)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Quiz 1 button not found"));

        assertTrue(quiz1Button.isCompleted(), "Quiz 1 should be marked as completed");

        // User 100 has completed quiz 2
        QuizButtonInfo quiz2Button = buttons.stream()
                .filter(b -> b.getQuizId() == 2)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Quiz 2 button not found"));

        assertTrue(quiz2Button.isCompleted(), "Quiz 2 should be marked as completed");
    }

    @Test
    void testGetQuizButtonsInfo_returnsAllQuizzes() {
        // User 999 has no attempts at all
        List<QuizButtonInfo> buttons = quizRepository.getQuizButtonsInfo(999L);

        assertNotNull(buttons);
        assertTrue(buttons.size() >= 6, "Should return all 6 quizzes");

        // All quizzes should be marked as not completed
        long completedCount = buttons.stream()
                .filter(QuizButtonInfo::isCompleted)
                .count();

        assertEquals(0, completedCount, "All quizzes should be marked as not completed for new user");
    }

    @Test
    void testGetQuizButtonsInfo_includesQuizNames() {
        List<QuizButtonInfo> buttons = quizRepository.getQuizButtonsInfo(100L);

        assertNotNull(buttons);
        assertFalse(buttons.isEmpty());

        // Verify quiz names are populated
        for (QuizButtonInfo button : buttons) {
            assertNotNull(button.getQuizName(), "Quiz name should not be null");
            assertFalse(button.getQuizName().isEmpty(), "Quiz name should not be empty");
        }
    }
}
