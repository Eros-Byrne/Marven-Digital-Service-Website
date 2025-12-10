package uk.ac.cf.spring.clientprojectteam3.admin.quiz;

import uk.ac.cf.spring.clientprojectteam3.quiz.Quiz;

import java.util.List;

public interface AdminQuizService {
    List<Quiz> getQuizzes();

    void setQuestionsForQuiz(Quiz quiz);

    void deleteQuizQuestion(int id);

    void createQuiz(String title, Long outcomeId, String description, int time);

    void createQuestion(int id, int capabilityId, String text);
}
