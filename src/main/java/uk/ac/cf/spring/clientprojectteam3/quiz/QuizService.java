package uk.ac.cf.spring.clientprojectteam3.quiz;

public interface QuizService {
    Quiz getQuizForAttempt(int quizId, int attemptId);

    boolean isComplete(QuizAttempt attempt, Quiz quiz);

    int firstUnansweredIndex(QuizAttempt attempt, Quiz quiz);

    void submitAttempt(int attemptId, QuizAttempt attempt);
}
