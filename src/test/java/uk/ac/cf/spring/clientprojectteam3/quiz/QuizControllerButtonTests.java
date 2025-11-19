package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerButtonTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuizService quizService;

    private QuizDTO createSampleQuiz(int numQuestions) {
        List<Map<String, Object>> questions = List.of(
                Map.of("id", 0, "text", "Question 1", "options", List.of("1","2","3","4","5")),
                Map.of("id", 1, "text", "Question 2", "options", List.of("1","2","3","4","5")),
                Map.of("id", 2, "text", "Question 3", "options", List.of("1","2","3","4","5"))
        );
        QuizDTO dto = new QuizDTO();
        dto.setTitle("Sample Quiz");
        dto.setQuestions(questions.subList(0, numQuestions));
        return dto;
    }

    @Test
    void firstQuestionShouldNotShowPreviousButton() throws Exception {
        QuizDTO quiz = createSampleQuiz(3);
        Mockito.when(quizService.getQuizForAttempt(Mockito.anyInt())).thenReturn(quiz);

        mockMvc.perform(get("/quiz/attempt/0/question/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("id=\"prevBtn\""))))
                .andExpect(content().string(containsString("id=\"nextBtn\"")));
    }

    @Test
    void lastQuestionShouldNotShowNextButton() throws Exception {
        QuizDTO quiz = createSampleQuiz(3);
        Mockito.when(quizService.getQuizForAttempt(Mockito.anyInt())).thenReturn(quiz);

        mockMvc.perform(get("/quiz/attempt/0/question/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id=\"prevBtn\"")))
                .andExpect(content().string(not(containsString("id=\"nextBtn\""))));
    }

    @Test
    void middleQuestionShouldShowBothButtons() throws Exception {
        QuizDTO quiz = createSampleQuiz(3);
        Mockito.when(quizService.getQuizForAttempt(Mockito.anyInt())).thenReturn(quiz);

        mockMvc.perform(get("/quiz/attempt/0/question/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id=\"prevBtn\"")))
                .andExpect(content().string(containsString("id=\"nextBtn\"")));
    }

    @Test
    void onlyOneQuestionShouldShowNoButtons() throws Exception {
        List<Map<String, Object>> questions = List.of(
            Map.of("id", 0,
                    "text", "Question 1",
                    "options", List.of("1","2","3","4","5")
            ));
        QuizDTO quiz = new QuizDTO();
        quiz.setTitle("Sample Quiz");
        quiz.setQuestions(questions.subList(0, 1));

        Mockito.when(quizService.getQuizForAttempt(Mockito.anyInt())).thenReturn(quiz);

        mockMvc.perform(get("/quiz/attempt/0/question/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("id=\"prevBtn\""))))
                .andExpect(content().string(not(containsString("id=\"nextBtn\""))));
    }
}
