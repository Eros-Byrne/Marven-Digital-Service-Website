package uk.ac.cf.spring.clientprojectteam3.quiz;

public interface QuizService {
    Quiz getQuizForAttempt(int quizId, int attemptId);
}
