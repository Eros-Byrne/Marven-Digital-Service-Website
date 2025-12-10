package uk.ac.cf.spring.clientprojectteam3.admin.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.cf.spring.clientprojectteam3.quiz.Question;
import uk.ac.cf.spring.clientprojectteam3.quiz.Quiz;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminQuizServiceTests {

    @Mock
    private AdminQuizRepository adminQuizRepository;

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private AdminQuizServiceImpl adminQuizService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllQuizzes() {
        Quiz quiz1 = new Quiz(1L, "Q1", "D1", 10);
        Quiz quiz2 = new Quiz(2L, "Q2", "D2", 20);

        when(adminQuizRepository.findAllQuizzes()).thenReturn(List.of(quiz1, quiz2));

        List<Quiz> quizzes = adminQuizService.getQuizzes();

        assertEquals(2, quizzes.size());
        verify(adminQuizRepository, times(1)).findAllQuizzes();
    }

    @Test
    void shouldSetQuestionsForQuiz() {
        Quiz quiz = new Quiz(1L, "Q1", "D1", 10);

        Question q1 = new Question(1L, 1L, "Question 1", 1L);
        Question q2 = new Question(2L, 1L, "Question 2", 2L);

        when(quizRepository.getQuestions(1L)).thenReturn(List.of(q1, q2));
        when(adminQuizRepository.getCapabilityById(1L)).thenReturn("C1");
        when(adminQuizRepository.getCapabilityById(2L)).thenReturn("C2");
        when(adminQuizRepository.getCapabilityColourById(1L)).thenReturn("Red");
        when(adminQuizRepository.getCapabilityColourById(2L)).thenReturn("Blue");

        adminQuizService.setQuestionsForQuiz(quiz);

        assertEquals(2, quiz.getQuestions().size());
        assertEquals("C1", quiz.getQuestions().get(0).getCapabilityName());
        assertEquals("Red", quiz.getQuestions().get(0).getCapabilityColour());
        assertEquals("C2", quiz.getQuestions().get(1).getCapabilityName());
        assertEquals("Blue", quiz.getQuestions().get(1).getCapabilityColour());

        verify(quizRepository, times(1)).getQuestions(1L);
        verify(adminQuizRepository, times(1)).getCapabilityById(1L);
        verify(adminQuizRepository, times(1)).getCapabilityById(2L);
        verify(adminQuizRepository, times(1)).getCapabilityColourById(1L);
        verify(adminQuizRepository, times(1)).getCapabilityColourById(2L);
    }

    @Test
    void shouldCreateQuiz() {
        adminQuizService.createQuiz("Java Basics", 1L, "Intro to Java", 30);

        verify(adminQuizRepository, times(1))
                .createQuiz("Java Basics", 1L, "Intro to Java", 30);
    }

    @Test
    void shouldCreateQuestion() {
        adminQuizService.createQuestion(1, 1, "What is Java?");

        verify(adminQuizRepository, times(1))
                .createQuestion(1, 1, "What is Java?");
    }

    @Test
    void shouldDeleteQuestion() {
        adminQuizService.deleteQuizQuestion(1);

        verify(adminQuizRepository, times(1)).deleteQuestion(1);
    }
}
