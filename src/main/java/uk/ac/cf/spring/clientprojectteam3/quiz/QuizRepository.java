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

    // Methods for summary page
    Map<Long, Integer> getAttemptAnswers(long attemptId);

    int getAttemptNumber(long attemptId);

    List<QuizCardDTO> getQuizCardsByUserId(long userId);

    // NEW METHOD: Find latest completed attempt for a user and quiz
    Long findLatestCompletedAttempt(long userId, long quizId);
}