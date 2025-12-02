package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
class QuizServiceTests {

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizServiceImpl quizService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Question makeQuestion(int id, String text) {
        Question q = new Question();
        q.setQuestionId(id);
        q.setText(text);
        return q;
    }

    @Test
    void loadAttemptFromSession_shouldReturnNewAttemptWhenNoneExists() {
        MockHttpSession session = new MockHttpSession();

        QuizAttempt attempt = quizService.loadAttemptFromSession(5, session);

        assertNotNull(attempt);
        assertEquals(5, attempt.getQuizId());
        assertTrue(attempt.getAnswers().isEmpty());
    }

    @Test
    void loadAttemptFromSession_shouldReturnExistingAttempt() {
        MockHttpSession session = new MockHttpSession();
        QuizAttempt existing = new QuizAttempt();
        existing.setQuizId(9);

        session.setAttribute("quizAttempt", existing);

        QuizAttempt result = quizService.loadAttemptFromSession(9, session);

        assertSame(existing, result);
    }

    @Test
    void storeAttempt_shouldPutAttemptInSession() {
        MockHttpSession session = new MockHttpSession();
        QuizAttempt attempt = new QuizAttempt();

        quizService.saveAttemptToSession(session, attempt);

        assertSame(attempt, session.getAttribute("quizAttempt"));
    }

    @Test
    void saveAttemptToSession_shouldOverwriteSessionValue() {
        MockHttpSession session = new MockHttpSession();
        QuizAttempt attempt = new QuizAttempt();

        quizService.saveAttemptToSession(session, attempt);

        assertSame(attempt, session.getAttribute("quizAttempt"));
    }

    @Test
    void recordAnswer_shouldSaveNonNullAnswer() {
        QuizAttempt attempt = new QuizAttempt();

        quizService.recordAnswer(attempt, 0, 3);

        assertEquals(3, attempt.getAnswers().get(0));
    }

    @Test
    void recordAnswer_shouldIgnoreNullAnswer() {
        QuizAttempt attempt = new QuizAttempt();
        attempt.getAnswers().put(0, 2);

        quizService.recordAnswer(attempt, 0, null);

        // unchanged
        assertEquals(2, attempt.getAnswers().get(0));
    }

    @Test
    void indexValid_shouldReturnTrueForValidIndex() {
        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(makeQuestion(1, "Q1")));

        assertTrue(quizService.indexValid(quiz, 0));
    }

    @Test
    void indexValid_shouldReturnFalseForInvalidIndex() {
        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(makeQuestion(1, "Q1")));

        assertFalse(quizService.indexValid(quiz, -1));
        assertFalse(quizService.indexValid(quiz, 5));
    }


    @Test
    void shouldLoadQuizAndQuestions() {
        Quiz quiz = new Quiz();
        when(quizRepository.getQuiz(10)).thenReturn(quiz);
        when(quizRepository.getQuestions(10)).thenReturn(
                List.of(makeQuestion(1, "Q1"), makeQuestion(2, "Q2"))
        );

        Quiz result = quizService.getQuizForAttempt(10, 0);

        assertEquals(10, result.getQuizId());
        assertEquals(2, result.getQuestions().size());
        verify(quizRepository).getQuiz(10);
        verify(quizRepository).getQuestions(10);
    }


    @Test
    void isComplete_shouldReturnFalseIfAnyMissing() {
        QuizAttempt attempt = new QuizAttempt();
        attempt.getAnswers().put(0, 3);

        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(
                makeQuestion(1, "Q1"),
                makeQuestion(2, "Q2")
        ));

        assertFalse(quizService.isComplete(attempt, quiz));
    }

    @Test
    void isComplete_shouldReturnTrueWhenAllAnswered() {
        QuizAttempt attempt = new QuizAttempt();
        attempt.getAnswers().put(0, 3);
        attempt.getAnswers().put(1, 2);

        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(
                makeQuestion(1, "Q1"),
                makeQuestion(2, "Q2")
        ));

        assertTrue(quizService.isComplete(attempt, quiz));
    }



    @Test
    void firstUnansweredIndex_shouldReturnIndexOfFirstMissing() {
        QuizAttempt attempt = new QuizAttempt();
        attempt.getAnswers().put(0, 3);

        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(
                makeQuestion(1, "Q1"),
                makeQuestion(2, "Q2")
        ));

        assertEquals(1, quizService.firstUnansweredIndex(attempt, quiz));
    }

    @Test
    void firstUnansweredIndex_shouldReturnMinusOneIfComplete() {
        QuizAttempt attempt = new QuizAttempt();
        attempt.getAnswers().put(0, 1);
        attempt.getAnswers().put(1, 2);

        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(
                makeQuestion(1, "Q1"),
                makeQuestion(2, "Q2")
        ));

        assertEquals(-1, quizService.firstUnansweredIndex(attempt, quiz));
    }


}
