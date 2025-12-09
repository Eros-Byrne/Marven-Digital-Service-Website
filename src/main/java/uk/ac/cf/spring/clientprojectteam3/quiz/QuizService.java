package uk.ac.cf.spring.clientprojectteam3.quiz;

import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface QuizService {
//    Quiz getQuizForAttempt(int quizId, int attemptId);

    boolean isComplete(QuizAttempt attempt, Quiz quiz);

    int firstUnansweredIndex(QuizAttempt attempt, Quiz quiz);

    void submitAttempt(long userId, long attemptId, QuizAttempt attempt);

    boolean indexValid(Quiz quiz, int index);

    void recordAnswer(QuizAttempt attempt, int index, Integer answer);

    void saveAttemptToSession(HttpSession session, QuizAttempt attempt);

    QuizAttempt loadAttemptFromSession(int quizId, int attemptId, HttpSession session);

    long startAttempt(long userId, long quizId);

    Quiz getQuizForAttempt(long quizId, int attemptId);

    void saveIncompleteAttempt(long userId, int attemptId, QuizAttempt attempt);

    void deleteEmptyAttempt(int attemptId, HttpSession session);

    List<QuizCardDTO> getQuizCards(Integer userId);

    int getOrStartAttempt(long userId, int quizId);

    int getAttemptNumber(long attemptId);
}
