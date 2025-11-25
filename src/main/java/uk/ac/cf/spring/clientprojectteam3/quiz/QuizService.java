package uk.ac.cf.spring.clientprojectteam3.quiz;

import jakarta.servlet.http.HttpSession;

public interface QuizService {
    Quiz getQuizForAttempt(int quizId, int attemptId);

    boolean isComplete(QuizAttempt attempt, Quiz quiz);

    int firstUnansweredIndex(QuizAttempt attempt, Quiz quiz);

    void submitAttempt(int attemptId, QuizAttempt attempt);

    boolean indexValid(Quiz quiz, int index);

    void storeAttempt(HttpSession session, QuizAttempt attempt);

    void recordAnswer(QuizAttempt attempt, int index, Integer answer);

    void saveAttemptToSession(HttpSession session, QuizAttempt attempt);

    QuizAttempt loadAttemptFromSession(int quizId, HttpSession session);
}
