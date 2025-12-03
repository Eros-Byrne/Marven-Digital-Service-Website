package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizListController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuizListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuizRepository quizRepo;

    @Test
    void testQuizListDisplaysAllQuizzes() throws Exception {
        Quiz quiz1 = new Quiz(1L, "Quiz 1", "Description 1", 10);
        Quiz quiz2 = new Quiz(2L, "Quiz 2", "Description 2", 15);

        Mockito.when(quizRepo.getQuizNames()).thenReturn(Arrays.asList(quiz1, quiz2));

        mockMvc.perform(get("/quiz-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("quizzes/quiz-list"))
                .andExpect(model().attributeExists("quizzes"))
                .andExpect(content().string(containsString("Quiz 1")))
                .andExpect(content().string(containsString("Quiz 2")))
                .andExpect(content().string(containsString("Description 1")))
                .andExpect(content().string(containsString("Description 2")));
    }

    @Test
    void testQuizListShowsNoQuizzesMessageWhenEmpty() throws Exception {
        Mockito.when(quizRepo.getQuizNames()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/quiz-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("quizzes/quiz-list"))
                .andExpect(model().attributeExists("noQuizzes"))
                .andExpect(content().string(containsString("No quizzes available")));
    }

    @Test
    void testStartQuizButtonLinksCorrectly() throws Exception {
        Quiz quiz = new Quiz(1L, "Quiz 1", "Description 1", 10);

        Mockito.when(quizRepo.getQuizNames()).thenReturn(Arrays.asList(quiz));

        mockMvc.perform(get("/quiz-list"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/quiz/1/attempt/0/question/0")));
    }
}
