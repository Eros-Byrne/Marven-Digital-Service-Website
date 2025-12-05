package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.ac.cf.spring.clientprojectteam3.security.CurrentUserService;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class QuizControllerTests {

    private MockMvc mockMvc;
    private MockHttpSession session;

    @Mock
    private QuizService quizService;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private QuizController controller;

    @BeforeEach
    void setup() {
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
        MockHttpSession session = new MockHttpSession();
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuizId(1);
        attempt.setCurrentQuestionIndex(0);
        attempt.setAnswers(new HashMap<>());
        session.setAttribute("quizAttempt", attempt);

        Question question = new Question();
        question.setQuestionId(1L);
        question.setText("Question 1");

        Quiz quiz = new Quiz();
        quiz.setName("Sample Quiz");
        quiz.setQuestions(List.of(question));

        when(quizService.loadAttemptFromSession(1, session)).thenReturn(attempt);
        when(quizService.getQuizForAttempt(1, 1)).thenReturn(quiz);
        when(quizService.indexValid(quiz, 0)).thenReturn(true);

        // Perform request
        MvcResult result = mockMvc.perform(get("/quiz/1/attempt/1/question/0")
                        .session(session))
                .andReturn();
        System.out.println(result.getModelAndView());

        mockMvc.perform(get("/quiz/1/attempt/1/question/0").session(session))
                .andExpect(model().attribute("quizTitle", "Sample Quiz"))
                .andExpect(model().attribute("question", question))
                .andExpect(model().attribute("index", 0))
                .andExpect(model().attribute("total", 1))
                .andExpect(model().attribute("attemptId", 1));
    }


    @Test
    void shouldRedirectQuestionPageWithInvalidIndex() throws Exception {
        Quiz quiz = new Quiz();
        Question q1 = makeQuestion(1, "Question 1");
        quiz.setQuestions(List.of(q1));

        when(quizService.getQuizForAttempt(1, 1)).thenReturn(quiz);
        when(quizService.indexValid(any(Quiz.class), eq(10))).thenReturn(false);


        mockMvc.perform(get("/quiz/1/attempt/1/question/10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quiz/1/attempt/1/question/0"));
    }

    @Test
    void shouldTakeUserToNextQuestion() throws Exception {
        Quiz quiz = new Quiz();
        quiz.setName("Sample Quiz");
        when(currentUserService.getCurrentUserId()).thenReturn(1);
        List<Question> questions = List.of(
                makeQuestion(1, "Question 1"),
                makeQuestion(2, "Question 2"),
                makeQuestion(3, "Question 3")
        );
        quiz.setQuestions(questions);


        mockMvc.perform(get("/quiz/1/attempt/0/question/0"))
                .andExpect(status().is3xxRedirection());


        mockMvc.perform(get("/quiz/1/attempt/0/question/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testPreviousButtonRedirectsToPreviousQuestion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuizId(1);
        attempt.setCurrentQuestionIndex(1);
        attempt.setAnswers(new HashMap<>());
        session.setAttribute("quizAttempt", attempt);

        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(new Question(), new Question()));

        Mockito.lenient().when(quizService.loadAttemptFromSession(1, session)).thenReturn(attempt);
        Mockito.lenient().when(quizService.getQuizForAttempt(1, 0)).thenReturn(quiz);

        // Simulate recordAnswer behavior
        Mockito.doAnswer(invocation -> {
            QuizAttempt a = invocation.getArgument(0);
            int index = invocation.getArgument(1);
            Integer answer = invocation.getArgument(2);
            if (answer != null) {
                a.getAnswers().put(index, answer);
            }
            return null;
        }).when(quizService).recordAnswer(Mockito.any(), Mockito.anyInt(), Mockito.any());

        mockMvc.perform(post("/quiz/1/attempt/0/question/1/answer")
                        .param("nav", "prev")
                        .param("answer", "2")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quiz/1/attempt/0/question/0"));

        assertEquals(2, attempt.getAnswers().get(1));
    }



    @Test
    void answer_shouldSaveAnswerAndMoveToNextQuestion() {
        session = new MockHttpSession();

        QuizAttempt attempt = new QuizAttempt();
        attempt.setAttemptId(10);
        attempt.setCurrentQuestionIndex(0);

        session.setAttribute("quizAttempt", attempt);

        when(quizService.loadAttemptFromSession(1, session))
                .thenReturn(attempt);

        Mockito.doAnswer(inv -> {
            QuizAttempt a = inv.getArgument(0);
            int index = inv.getArgument(1);
            Integer answer = inv.getArgument(2);
            a.getAnswers().put(index, answer);
            return null;
        }).when(quizService).recordAnswer(Mockito.any(), Mockito.anyInt(), Mockito.any());

        Mockito.doNothing().when(quizService).saveAttemptToSession(session, attempt);

        String result = controller.answer(
                1,   // quizId
                10,  // attemptId
                0,   // index
                1,   // answer
                "next", // nav
                session
        );

        assertEquals("redirect:/quiz/1/attempt/10/question/1", result);
        assertEquals(0, attempt.getCurrentQuestionIndex());
        assertEquals(1, attempt.getAnswers().get(0));
    }
}
