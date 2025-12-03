
package uk.ac.cf.spring.clientprojectteam3.quiz;

import java.util.List;
import java.util.Map;

public interface QuizRepository {
    List<Quiz> getQuizNames();
    List<Question> getQuestions(long quizId);
    Quiz getQuiz(long quizId);

    void setRowMappers();

    void saveAnswer(long userAttemptId, long questionId, Integer score);

    void markAttemptComplete(long userAttemptId);

    long createUserAttempt(long userId, long quizId);

    void markAttemptIncomplete(int attemptId);

    void deleteEmptyAttempt(int attemptId);

    // New methods for summary page
    Map<Long, Integer> getAttemptAnswers(long attemptId);

    int getAttemptNumber(long attemptId);
}