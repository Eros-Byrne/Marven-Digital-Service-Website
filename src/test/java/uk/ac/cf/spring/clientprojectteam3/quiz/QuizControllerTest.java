package uk.ac.cf.spring.clientprojectteam3.quiz;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
class QuizControllerTest {

    @Mock
    private QuizService quizService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        QuizController controller = new QuizController();
        ReflectionTestUtils.setField(controller, "quizService", quizService); // inject manually
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldShowQuestionPageAndQuizQuestionData() throws Exception {
        QuizDTO quiz = new QuizDTO();
        quiz.setTitle("Sample Quiz");
        Map<String, Object> q1 = Map.of(
                "id", 1L,
                "text", "Question 1",
                "options", List.of("1","2","3","4","5")
        );

        quiz.setQuestions(List.of(q1));

        Mockito.when(quizService.getQuizForAttempt(0)).thenReturn(quiz);

        mockMvc.perform(get("/quiz/attempt/0/question/0"))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz"))
                .andExpect(model().attributeExists("quizTitle", "question", "index", "total", "attemptId"))
                .andExpect(model().attribute("quizTitle", "Sample Quiz"))
                .andExpect(model().attribute("index", 0))
                .andExpect(model().attribute("total", 1))
                .andExpect(model().attribute("attemptId", 0));
    }

    @Test
    void shouldRedirectQuestionPageWithInvalidIndex() throws Exception {

        QuizDTO quiz = new QuizDTO();
        Map<String, Object> q1 = Map.of(
                "id", 1L,
                "text", "Question 1",
                "options", List.of("1","2","3","4","5")
        );
        quiz.setQuestions(List.of(q1)); // only 1 question
        Mockito.when(quizService.getQuizForAttempt(0)).thenReturn(quiz);
        // out-of-bounds index
        mockMvc.perform(get("/quiz/attempt/0/question/10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quiz/attempt/0/question/0"));
    }

    @Test
    void shouldTakeUserToNextQuestion() throws Exception {

        QuizDTO quiz = new QuizDTO();
        quiz.setTitle("Sample Quiz");
        quiz.setQuestions(List.of(
                Map.of("id", 1, "text", "Question 1", "options", List.of("1","2","3","4","5")),
                Map.of("id", 2, "text", "Question 2", "options", List.of("1","2","3","4","5")),
                Map.of("id", 3, "text", "Question 3", "options", List.of("1","2","3","4","5"))
        ));

        Mockito.when(quizService.getQuizForAttempt(0)).thenReturn(quiz);

        // Go to first question (index 0)
        mockMvc.perform(get("/quiz/attempt/0/question/0"))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz"))
                .andExpect(model().attribute("index", 0))
                .andExpect(model().attribute("total", 3))
                .andExpect(model().attribute("question", quiz.getQuestions().get(0)));

        // Go to second question (simulate Next button)
        mockMvc.perform(get("/quiz/attempt/0/question/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz"))
                .andExpect(model().attribute("index", 1))
                .andExpect(model().attribute("total", 3))
                .andExpect(model().attribute("question", quiz.getQuestions().get(1)));
    }

    @Test
    void shouldTakeUserToPreviousQuestion() throws Exception {

        QuizDTO quiz = new QuizDTO();
        quiz.setTitle("Sample Quiz");
        quiz.setQuestions(List.of(
                Map.of("id", 1, "text", "Question 1", "options", List.of("1","2","3","4","5")),
                Map.of("id", 2, "text", "Question 2", "options", List.of("1","2","3","4","5")),
                Map.of("id", 3, "text", "Question 3", "options", List.of("1","2","3","4","5"))
        ));

        Mockito.when(quizService.getQuizForAttempt(0)).thenReturn(quiz);

        // Go to second question (index 1)
        mockMvc.perform(get("/quiz/attempt/0/question/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz"))
                .andExpect(model().attribute("index", 1))
                .andExpect(model().attribute("question", quiz.getQuestions().get(1)));

        // Go to previous question (index 0)
        mockMvc.perform(get("/quiz/attempt/0/question/0"))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz"))
                .andExpect(model().attribute("index", 0))
                .andExpect(model().attribute("question", quiz.getQuestions().get(0)));
    }
}





