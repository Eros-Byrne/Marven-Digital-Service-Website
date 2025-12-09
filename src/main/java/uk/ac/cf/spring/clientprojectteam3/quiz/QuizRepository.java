package uk.ac.cf.spring.clientprojectteam3.quiz;

import java.util.List;
import java.util.Map;

public interface QuizRepository {
    List<QuizCardDTO> getBlankQuizCards();
    List<Question> getQuestions(long quizId);
    Quiz getQuiz(long quizId);

    void setRowMappers();

    void saveAnswer(long userAttemptId, long questionId, Integer score);

    void markAttemptComplete(long userAttemptId);

    long createUserAttempt(long userId, long quizId);

    void markAttemptIncomplete(int attemptId);

    void deleteEmptyAttempt(int attemptId);

    // New methods for summary page
    Map<Integer, Integer> getAttemptAnswers(long attemptId);

    int getAttemptNumber(long attemptId);

    List<QuizCardDTO> getQuizCardsByUserId(long userId);

    Integer getLatestAttemptId(long userId, int quizId);

    int getAttemptStatus(int latestAttemptId);

    // NEW METHOD: Find latest completed attempt for a user and quiz
    Long findLatestCompletedAttempt(long userId, long quizId);

    // NEW METHOD: Get all completed attempts for a specific quiz by a user
    List<QuizAttemptScore> getAllCompletedAttempts(long userId, long quizId);

    // NEW METHOD: Get all quizzes with completion status for a user
    List<QuizButtonInfo> getQuizButtonsInfo(long userId);
}