package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class QuizControllerTests {

    @Mock
    private QuizService quizService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        QuizController controller = new QuizController();
        ReflectionTestUtils.setField(controller, "quizService", quizService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private Question makeQuestion(int id, String text) {
        Question q = new Question();
        q.setQuestionId(id);
        q.setText(text);
        return q;
    }

    @Test
    void shouldShowQuestionPageAndQuizQuestionData() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setName("Sample Quiz");

        Question q1 = makeQuestion(1, "Question 1");

        quiz.setQuestions(List.of(q1));

        Mockito.when(quizService.getQuizForAttempt(1, 0)).thenReturn(quiz);

        mockMvc.perform(get("/quiz/1/attempt/0/question/0"))
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
        Quiz quiz = new Quiz();

        Question q1 = makeQuestion(1, "Question 1");

        quiz.setQuestions(List.of(q1));

        Mockito.when(quizService.getQuizForAttempt(1, 0)).thenReturn(quiz);

        mockMvc.perform(get("/quiz/1/attempt/0/question/10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quiz/1/attempt/0/question/0"));
    }

    @Test
    void shouldTakeUserToNextQuestion() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setName("Sample Quiz");

        List<Question> questions = List.of(
                makeQuestion(1, "Question 1"),
                makeQuestion(2, "Question 2"),
                makeQuestion(3, "Question 3")
        );

        quiz.setQuestions(questions);

        Mockito.when(quizService.getQuizForAttempt(1, 0)).thenReturn(quiz);

        mockMvc.perform(get("/quiz/1/attempt/0/question/0"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("index", 0));

        mockMvc.perform(get("/quiz/1/attempt/0/question/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("index", 1));
    }

    @Test
    void shouldTakeUserToPreviousQuestion() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setName("Sample Quiz");

        List<Question> questions = List.of(
                makeQuestion(1, "Question 1"),
                makeQuestion(2, "Question 2")
        );

        quiz.setQuestions(questions);

        Mockito.when(quizService.getQuizForAttempt(1, 0)).thenReturn(quiz);

        mockMvc.perform(get("/quiz/1/attempt/0/question/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("index", 1));

        mockMvc.perform(get("/quiz/1/attempt/0/question/0"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("index", 0));
    }
}
