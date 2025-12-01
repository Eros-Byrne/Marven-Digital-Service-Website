package uk.ac.cf.spring.clientprojectteam3.quiz;

import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface QuizService {
    boolean isComplete(QuizAttempt attempt, Quiz quiz);

    int firstUnansweredIndex(QuizAttempt attempt, Quiz quiz);

    void submitAttempt(long userId, long attemptId, QuizAttempt attempt);

    boolean indexValid(Quiz quiz, int index);

    void recordAnswer(QuizAttempt attempt, int index, Integer answer);

    void saveAttemptToSession(HttpSession session, QuizAttempt attempt);

    QuizAttempt loadAttemptFromSession(int quizId, HttpSession session);

    long startAttempt(long userId, int i);

    Quiz getQuizForAttempt(long quizId, int attemptId);

    List<Quiz> getQuizNames();

    List<Question> getQuestionsForQuiz(int quizId);

    Long getCurrentAttempt(int userId);

    void saveIncompleteAttempt(long userId, int attemptId, QuizAttempt attempt);
}
