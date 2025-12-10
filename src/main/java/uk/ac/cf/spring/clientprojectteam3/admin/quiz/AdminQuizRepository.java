package uk.ac.cf.spring.clientprojectteam3.admin.quiz;

import uk.ac.cf.spring.clientprojectteam3.quiz.Quiz;

import java.util.List;

public interface AdminQuizRepository {
    List<Quiz> findAllQuizzes();

    String getCapabilityById(long capabilityId);

    String getCapabilityColourById(long capabilityId);

    void deleteQuestion(int questionId);

    long createQuiz(String title, Long outcomeId, String description, int time);

    void createQuestion(int quizId, int capabilityId, String text);
}
