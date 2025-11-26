package uk.ac.cf.spring.clientprojectteam3.quiz;

import jakarta.servlet.http.HttpSession;

public interface QuizService {
    Quiz getQuizForAttempt(long quizId, int attemptId);

    boolean isComplete(QuizAttempt attempt, Quiz quiz);

    int firstUnansweredIndex(QuizAttempt attempt, Quiz quiz);

    void submitAttempt(long userId, long attemptId, QuizAttempt attempt);

    boolean indexValid(Quiz quiz, int index);

    void storeAttempt(HttpSession session, QuizAttempt attempt);

    void recordAnswer(QuizAttempt attempt, int index, Integer answer);

    void saveAttemptToSession(HttpSession session, QuizAttempt attempt);

    QuizAttempt loadAttemptFromSession(int quizId, HttpSession session);

    long startAttempt(long userId, int i);
}
