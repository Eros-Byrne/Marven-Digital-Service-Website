package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class QuizRepositoryTests {

    QuizRepositoryImpl quizRepo;

    @Mock
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        quizRepo = new QuizRepositoryImpl(jdbcTemplate);

    }

    @Test
    void createUserAttempt_ShouldInsertAndReturnGeneratedKey() {
        // Mock the JdbcTemplate.update that uses a PreparedStatementCreator and KeyHolder
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            // args[1] is the KeyHolder
            ((org.springframework.jdbc.support.KeyHolder) args[1]).getKeyList().add(Map.of("GENERATED_KEY", 1L));
            return 1; // rows affected
        }).when(jdbcTemplate).update(any(org.springframework.jdbc.core.PreparedStatementCreator.class),
                any(org.springframework.jdbc.support.KeyHolder.class));

        long generatedId = quizRepo.createUserAttempt(1L, 1);

        assertTrue(generatedId > 0, "Generated ID should be > 0");
    }

    @Test
    void testGetQuizNames() {
        Quiz quiz = new Quiz(1L, "Sample Quiz", "Desc", 30);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of(quiz));

        List<Quiz> result = quizRepo.getQuizNames();

        assertEquals(1, result.size());
        assertEquals("Sample Quiz", result.get(0).getName());
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    void testGetQuestions() {
        Question question = new Question(10L, 1L, "Text", 101L);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyLong()))
                .thenReturn(List.of(question));

        List<Question> result = quizRepo.getQuestions(1L);

        assertEquals(1, result.size());
        assertEquals("Text", result.get(0).getText());
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class), eq(1L));
    }

    @Test
    void testGetQuiz() {
        Quiz quiz = new Quiz(10L, "Sample", "Desc", 30);
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong()))
                .thenReturn(quiz);

        Quiz result = quizRepo.getQuiz(10L);

        assertEquals("Sample", result.getName());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(RowMapper.class), eq(10L));
    }

    @Test
    void testAddAnswer_ShouldStoreUserAnswers() {
        // Arrange: simulate no previous answers for attempt 1
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L), eq(1L), eq(1)))
                .thenReturn(Collections.emptyList());

        // Act: add answers for attempt 1
        quizRepo.addAnswer(1, 1, 1, 1, 5);
        quizRepo.addAnswer(1, 1, 1, 2, 10);
        quizRepo.addAnswer(1, 1, 1, 3, 15);

        // Assert: verify JdbcTemplate.update was called correctly for each insert
        verify(jdbcTemplate, times(3)).update(anyString(), eq(1L), eq(1L), eq(1), anyString());

        // Arrange: simulate existing answers for attempt 2
        HashMap<Long, Integer> existingAnswers = new HashMap<>(Map.of(1L, 50, 2L, 100));
        Answers previousAttempt = new Answers(1, 1, existingAnswers, 2);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L), eq(1L), eq(2)))
                .thenReturn(List.of(previousAttempt));

        // Act: add another answer for attempt 2 (should update existing)
        quizRepo.addAnswer(1, 1, 2, 3, 150);

        // Assert: verify JdbcTemplate.update was called for the update
        verify(jdbcTemplate, times(1))
                .update(startsWith("UPDATE user_answers SET answer_json"), anyString(), eq(1L), eq(1L), eq(2));
    }
}
