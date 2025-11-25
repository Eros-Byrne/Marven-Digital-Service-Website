package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class QuizRepositoryTests {
    QuizRepositoryImpl quizRepo;

    @Mock
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // manually inject the mock
        quizRepo = new QuizRepositoryImpl(jdbcTemplate);
        quizRepo.setRowMappers();
    }
    @Test
    public void AddAnswerTest() {
        // Mock empty list first (no previous attempt)
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L), eq(1L), eq(1)))
                .thenReturn(Collections.emptyList());

        // Mock empty list for attempt 2 as well
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L), eq(1L), eq(2)))
                .thenReturn(Collections.emptyList());

        // Add answers (calls will hit the mocks above)
        quizRepo.addAnswer(1, 1, 1, 1, 5);
        quizRepo.addAnswer(1, 1, 1, 2, 1);
        quizRepo.addAnswer(1, 1, 1, 3, 6);

        quizRepo.addAnswer(1, 1, 2, 1, 50);
        quizRepo.addAnswer(1, 1, 2, 2, 10);
        quizRepo.addAnswer(1, 1, 2, 3, 60);

        // Now mock getAnswers to return constructed Answers objects
        Answers attempt1 = new Answers(1, 1, new HashMap<>(Map.of(1L,5, 2L,1, 3L,6)), 1);
        Answers attempt2 = new Answers(1, 1, new HashMap<>(Map.of(1L,50, 2L,10, 3L,60)), 2);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L), eq(1L)))
                .thenReturn(List.of(attempt1, attempt2));

        List<Answers> answers = quizRepo.getAnswers(1, 1);

        assertEquals(2, answers.size());
        assertEquals(3, answers.get(0).getAnswers().size());
        assertEquals(5, (int) answers.get(0).getAnswers().get(1L));
        assertEquals(60, (int) answers.get(1).getAnswers().get(3L));
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
        List<Question> questionList = List.of(question);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L))).thenReturn(questionList);

        List<Question> result = quizRepo.getQuestions(1L);

        assertEquals(1, result.size());
        assertEquals("Text", result.get(result.size() - 1).getText());
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class), eq(1L));
    }

    @Test
    void testGetQuiz() {
        Quiz quiz = new Quiz(10L, "Sample", "Desc", 30);
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(10L))).thenReturn(quiz);

        Quiz result = quizRepo.getQuiz(10L);

        assertEquals("Sample", result.getName());
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(RowMapper.class), eq(10L));
    }

}
