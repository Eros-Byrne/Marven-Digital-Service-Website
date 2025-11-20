package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class QuizControllerButtonTests {

    @Mock
    private QuizService quizService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        QuizController controller = new QuizController();
        ReflectionTestUtils.setField(controller, "quizService", quizService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private Quiz makeQuiz() {
        Quiz q = new Quiz();
        q.setName("Sample Quiz");

        Question q1 = new Question();
        q1.setQuestionId(1L);
        q1.setText("Q1");

        Question q2 = new Question();
        q2.setQuestionId(2L);
        q2.setText("Q2");

        Question q3 = new Question();
        q3.setQuestionId(3L);
        q3.setText("Q3");
        q.setQuestions(List.of(q1, q2, q3));
        return q;
    }


    @Test
    void testNextButtonRedirectsToNextQuestion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        QuizAttempt attempt = new QuizAttempt();
        attempt.setAnswers(new HashMap<>());
        session.setAttribute("quizAttempt", attempt);

        mockMvc.perform(post("/quiz/1/attempt/0/question/0/answer")
                        .param("answer", "3")
                        .param("nav", "next")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quiz/1/attempt/0/question/1"));
    }

    @Test
    void testPreviousButtonRedirectsToPreviousQuestion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        QuizAttempt attempt = new QuizAttempt();
        attempt.setAnswers(new HashMap<>());
        session.setAttribute("quizAttempt", attempt);

        mockMvc.perform(post("/quiz/1/attempt/0/question/2/answer")
                        .param("answer", "4")
                        .param("nav", "prev")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quiz/1/attempt/0/question/1"));
    }

}
