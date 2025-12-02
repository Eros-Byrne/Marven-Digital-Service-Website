package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class QuizListRepositoryTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    QuizRepository quizRepository;

    @BeforeEach
    void setupDatabase() {
        // Drop first to ensure clean state
        jdbcTemplate.execute("SET foreign_key_checks=0");
        jdbcTemplate.execute("DROP TABLE IF EXISTS user_attempt");
        jdbcTemplate.execute("DROP TABLE IF EXISTS quiz");
        jdbcTemplate.execute("SET foreign_key_checks=1");


        // Create schema
        jdbcTemplate.execute("""
            CREATE TABLE quiz (
                quiz_id BIGINT PRIMARY KEY,
                name VARCHAR(128),
                description TEXT,
                time_estimate INT
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE user_attempt (
                user_attempt_id BIGINT PRIMARY KEY,
                quiz_id BIGINT,
                user_id BIGINT,
                attempt INT,
                complete INT
            )
        """);

        // Insert quizzes
        jdbcTemplate.update("INSERT INTO quiz (quiz_id, name, description, time_estimate) VALUES (1, 'Quiz A', 'Desc A', 10)");
        jdbcTemplate.update("INSERT INTO quiz (quiz_id, name, description, time_estimate) VALUES (2, 'Quiz B', 'Desc B', 15)");

        // Insert attempts for user 100 (Quiz A)
        jdbcTemplate.update("INSERT INTO user_attempt VALUES (1, 1, 100, 1, 0)");
        jdbcTemplate.update("INSERT INTO user_attempt VALUES (2, 1, 100, 2, 1)");
        jdbcTemplate.update("INSERT INTO user_attempt VALUES (3, 1, 100, 3, 0)"); // most recent attempt
    }

    @Test
    void testGetQuizCardsByUserId_returnsMostRecentAttempt() {
        long userId = 100;

        List<QuizCardDTO> results = quizRepository.getQuizCardsByUserId(userId);

        assertEquals(2, results.size()); // 2 quizzes exist

        QuizCardDTO quizA = results.stream()
                .filter(q -> q.getQuizId() == 1)
                .findFirst()
                .orElseThrow();

        assertEquals(3, quizA.getAttemptNumber());   // MOST RECENT
        assertEquals(0, quizA.getCompleted());       // from attempt 3

        QuizCardDTO quizB = results.stream()
                .filter(q -> q.getQuizId() == 2)
                .findFirst()
                .orElseThrow();

        assertEquals(0, quizB.getAttemptNumber());   // no attempts → default 0
        assertEquals(0, quizB.getCompleted());       // no attempts → default 0
    }

    @Test
    void testNoAttemptsForUser_allQuizzesReturnZeroValues() {
        long userId = 999; // does not exist in user_attempt

        List<QuizCardDTO> results = quizRepository.getQuizCardsByUserId(userId);

        assertEquals(2, results.size());

        results.forEach(dto -> {
            assertEquals(0, dto.getAttemptNumber());
            assertEquals(0, dto.getCompleted());
        });
    }
}
