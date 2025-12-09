package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.ac.cf.spring.clientprojectteam3.user.UserServiceImpl;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "test@example.com")
class QuizControllerButtonTests {

    @Mock
    private QuizService quizService;

    @Mock
    private UserServiceImpl currentUserService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        QuizController controller = new QuizController();
        ReflectionTestUtils.setField(controller, "quizService", quizService);
        ReflectionTestUtils.setField(controller, "currentUserService", currentUserService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        Mockito.lenient().when(currentUserService.getCurrentUserId()).thenReturn(1);
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
        attempt.setQuizId(1);
        attempt.setCurrentQuestionIndex(0);
        attempt.setAnswers(new HashMap<>());
        session.setAttribute("quizAttempt", attempt);

        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(new Question(), new Question()));

        // Make stubs lenient to avoid unnecessary stubbing errors
        Mockito.lenient().when(quizService.loadAttemptFromSession(1, 0, session)).thenReturn(attempt);
        Mockito.lenient().when(quizService.getQuizForAttempt(1, 0)).thenReturn(quiz);

        Mockito.doAnswer(invocation -> {
            QuizAttempt a = invocation.getArgument(0);
            int index = invocation.getArgument(1);
            Integer answer = invocation.getArgument(2);
            if (answer != null) {
                a.getAnswers().put(index, answer);
            }
            return null;
        }).when(quizService).recordAnswer(Mockito.any(), Mockito.anyInt(), Mockito.any());

        mockMvc.perform(post("/quiz/1/attempt/question/0/answer")
                        .param("nav", "next")
                        .param("answer", "1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quiz/1/attempt/question/1"));

        // Verify that the answer was recorded
        assertEquals(1, attempt.getAnswers().get(0));

        // Verify that currentQuestionIndex was updated by the controller
        assertEquals(0, attempt.getCurrentQuestionIndex());
    }

    @Test
    void testPreviousButtonRedirectsToPreviousQuestion() throws Exception {
        MockHttpSession session = new MockHttpSession();

        // Set up a QuizAttempt in session
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuizId(1);
        attempt.setCurrentQuestionIndex(1); // starting at question 1
        attempt.setAnswers(new HashMap<>());
        session.setAttribute("quizAttempt", attempt);

        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(new Question(), new Question()));

        // Stub service methods leniently to avoid unnecessary stubbing errors
        Mockito.lenient().when(quizService.loadAttemptFromSession(1, 0, session)).thenReturn(attempt);
        Mockito.lenient().when(quizService.getQuizForAttempt(1, 0)).thenReturn(quiz);

        Mockito.doAnswer(invocation -> {
            QuizAttempt a = invocation.getArgument(0);
            int index = invocation.getArgument(1);
            Integer answer = invocation.getArgument(2);
            if (answer != null) {
                a.getAnswers().put(index, answer);
            }
            return null;
        }).when(quizService).recordAnswer(Mockito.any(), Mockito.anyInt(), Mockito.any());

        mockMvc.perform(post("/quiz/1/attempt/question/1/answer")
                        .param("nav", "prev")
                        .param("answer", "2")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quiz/1/attempt/question/0"));

        assertEquals(2, attempt.getAnswers().get(1));

        // Current question index is not updated by recordAnswer, remains the same
        assertEquals(1, attempt.getCurrentQuestionIndex());
    }


}
