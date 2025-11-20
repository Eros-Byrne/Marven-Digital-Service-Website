package uk.ac.cf.spring.clientprojectteam3.quiz;

import java.util.List;

public interface QuizRepo {
    List<Quiz> getQuizNames();
    List<Question> getQuestions(long quizId);
    List<Answers> getAnswers(long quizId, long userId);
    void addAnswer(long quizId, long userId, int attemptNumber, long questionId, int quizScore);
    Quiz getQuiz(long quizId);

    void setRowMappers();
}
