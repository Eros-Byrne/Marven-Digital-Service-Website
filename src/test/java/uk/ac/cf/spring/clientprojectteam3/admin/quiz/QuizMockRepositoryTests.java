package uk.ac.cf.spring.clientprojectteam3.admin.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import uk.ac.cf.spring.clientprojectteam3.quiz.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class QuizMockRepositoryTests {

    @Mock
    private QuizRepository quizRepo;

    @Mock
    private AdminQuizRepository adminQuizRepo;

    @InjectMocks
    private QuizServiceImpl quizService;

    @InjectMocks
    private AdminQuizServiceImpl adminQuizService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldCreateQuizAndFetchQuestions() {
        long quizId = 1L;

        Question q1 = new Question(1L, quizId, "Q1", 1L);
        Question q2 = new Question(2L, quizId, "Q2", 2L);

        when(quizRepo.getQuestions(quizId)).thenReturn(List.of(q1, q2));
        when(adminQuizRepo.getCapabilityById(1L)).thenReturn("C1");
        when(adminQuizRepo.getCapabilityById(2L)).thenReturn("C2");
        when(adminQuizRepo.getCapabilityColourById(1L)).thenReturn("#FF0000");
        when(adminQuizRepo.getCapabilityColourById(2L)).thenReturn("#00FF00");

        Quiz quiz = new Quiz(quizId, "Sample Quiz", "Desc 1", 10);

        adminQuizService.setQuestionsForQuiz(quiz);

        verify(quizRepo, times(1)).getQuestions(quizId);

        List<Question> questions = quiz.getQuestions();
        assertEquals(2, questions.size());
        assertTrue(questions.stream().anyMatch(q -> q.getText().equals("Q1")));
        assertTrue(questions.stream().anyMatch(q -> q.getText().equals("Q2")));

        // Verify capabilities were set
        assertEquals("C1", questions.get(0).getCapabilityName());
        assertEquals("#FF0000", questions.get(0).getCapabilityColour());
        assertEquals("C2", questions.get(1).getCapabilityName());
        assertEquals("#00FF00", questions.get(1).getCapabilityColour());
    }

    @Test
    void shouldDisableQuestion() {
        Question q1 = new Question(1L, 1L, "Q1", 1L);
        List<Question> questions = Collections.singletonList(q1);
        when(quizRepo.getQuestions(1L)).thenReturn(questions);

        adminQuizService.deleteQuizQuestion(1);

        verify(adminQuizRepo, times(1)).deleteQuestion(1);
    }
}
