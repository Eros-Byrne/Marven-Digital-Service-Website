package uk.ac.cf.spring.clientprojectteam3.quiz;

import java.util.List;

public interface QuizService {
    Quiz getQuizForAttempt(int quizId, int attemptId);

    List<Quiz> getQuizNames();

    List<Question> getQuestionsForQuiz(int quizId);

    AttemptDTO getAttemptForQuiz(int quizId, int userId, int attemptId);

    Long getCurrentAttempt(int userId);
}
