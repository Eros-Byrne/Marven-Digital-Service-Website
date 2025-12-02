package uk.ac.cf.spring.clientprojectteam3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import uk.ac.cf.spring.clientprojectteam3.quiz.Quiz;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizListController;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class QuizListControllerTest {

    @Mock
    private QuizRepository quizRepo;

    @Mock
    private Model model;

    @InjectMocks
    private QuizListController quizListController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowQuizList_WithQuizzes() {
        List<Quiz> quizzes = Arrays.asList(
                new Quiz(1L, "Quiz 1", "Description 1", 10),
                new Quiz(2L, "Quiz 2", "Description 2", 15)
        );

        when(quizRepo.getQuizNames()).thenReturn(quizzes);

        String view = quizListController.showQuizList(model);

        assertEquals("quizzes/quiz-list", view);
        verify(model).addAttribute("quizzes", quizzes);
        verify(model, never()).addAttribute(eq("noQuizzes"), any());
        verify(model, never()).addAttribute(eq("dbError"), any());
    }

    @Test
    void testShowQuizList_EmptyDatabase() {
        when(quizRepo.getQuizNames()).thenReturn(Collections.emptyList());

        String view = quizListController.showQuizList(model);

        assertEquals("quizzes/quiz-list", view);
        verify(model).addAttribute("noQuizzes", true);
        verify(model, never()).addAttribute(eq("quizzes"), any());
        verify(model, never()).addAttribute(eq("dbError"), any());
    }

    @Test
    void testShowQuizList_DatabaseError() {
        when(quizRepo.getQuizNames()).thenThrow(new RuntimeException("DB down"));

        String view = quizListController.showQuizList(model);

        assertEquals("quizzes/quiz-list", view);
        verify(model).addAttribute(eq("dbError"), anyString());
        verify(model, never()).addAttribute(eq("quizzes"), any());
        verify(model, never()).addAttribute(eq("noQuizzes"), any());
    }
}
