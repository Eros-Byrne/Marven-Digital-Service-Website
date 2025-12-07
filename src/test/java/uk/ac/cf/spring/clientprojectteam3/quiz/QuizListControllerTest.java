package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizListController.class)
@WithMockUser(username = "test", roles = {"USER"})
public class QuizListControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private QuizRepository quizRepository;

    @MockitoBean
    private QuizController quizController;
    @MockitoBean
    private UserService currentUserService;
    @MockitoBean
    private QuizService quizService;

    @Test
    public void shouldDisplayQuizzes() throws Exception {
        // Arrange
        when(currentUserService.getCurrentUserId()).thenReturn(1);

        List<QuizCardDTO> sampleQuizzes = List.of(
                new QuizCardDTO(1, "Quiz 1", "Desc 1", 10, 1, 1, 0,1),
                new QuizCardDTO(2, "Quiz 2", "Desc 2", 15, 1, 1, 0,2)
        );

        when(quizService.getQuizCards(1)).thenReturn(sampleQuizzes);

        // Act & Assert
        MvcResult result = mvc.perform(get("/quiz-list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("quizzes/quiz-list"))
                .andExpect(model().attribute("quizzes", sampleQuizzes))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Check that quiz titles appear in the HTML
        assertTrue(content.contains("Quiz 1"));
        assertTrue(content.contains("Quiz 2"));

        // Check that quiz descriptions appear in the HTML
        assertTrue(content.contains("Desc 1"));
        assertTrue(content.contains("Desc 2"));
    }

    @Test
    public void shouldDisplayNoQuizzesMessage() throws Exception {
        // Arrange
        when(currentUserService.getCurrentUserId()).thenReturn(1);
        when(quizService.getQuizCards(1)).thenReturn(Collections.emptyList());

        // Act & Assert
        MvcResult result = mvc.perform(get("/quiz-list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("quizzes/quiz-list"))
                .andExpect(model().attribute("noQuizzes", true))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Check that the "no quizzes" message appears in the HTML
        assertTrue(content.contains("No quizzes available."));
    }

    @Test
    public void shouldHandleRepositoryException() throws Exception {
        // Arrange
        when(currentUserService.getCurrentUserId()).thenReturn(1);
        when(quizService.getQuizCards(1))
                .thenThrow(new RuntimeException("DB failure"));

        // Act & Assert
        MvcResult result = mvc.perform(get("/quiz-list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("quizzes/quiz-list"))
                .andExpect(model().attribute("dbError", true))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Check that the error message appears in the HTML
        assertTrue(content.contains("Error fetching quizzes."));
    }
}
