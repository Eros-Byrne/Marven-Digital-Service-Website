package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class QuizListControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizListController quizListController;

    private List<Quiz> sampleQuizzes;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(quizListController).build();

        sampleQuizzes = Arrays.asList(
                new Quiz(1, "Quiz 1", "Desc 1", 10, null),
                new Quiz(2, "Quiz 2", "Desc 2", 15, null)
        );
    }

    @Test
    public void testQuizzesAreDisplayed() throws Exception {
        when(quizRepository.getQuizNames()).thenReturn(sampleQuizzes);

        mockMvc.perform(get("/quiz-list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("quizzes"))
                .andExpect(model().attribute("noQuizzes", false))
                .andExpect(view().name("quiz-list"));
    }

    @Test
    public void testNoQuizzesMessage() throws Exception {
        when(quizRepository.getQuizNames()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/quiz-list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("noQuizzes"))
                .andExpect(model().attribute("noQuizzes", true))
                .andExpect(view().name("quiz-list"));
    }

    @Test
    public void testRepositoryThrowsException() throws Exception {
        when(quizRepository.getQuizNames()).thenThrow(new RuntimeException("DB failure"));

        mockMvc.perform(get("/quiz-list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Failed to load quizzes."))
                .andExpect(view().name("quiz-list"));
    }
}
