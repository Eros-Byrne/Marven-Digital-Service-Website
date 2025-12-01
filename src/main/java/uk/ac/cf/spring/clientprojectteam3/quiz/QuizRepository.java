package uk.ac.cf.spring.clientprojectteam3.quiz;

import java.util.List;

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
}
