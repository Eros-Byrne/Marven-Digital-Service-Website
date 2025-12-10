package uk.ac.cf.spring.clientprojectteam3.admin.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import uk.ac.cf.spring.clientprojectteam3.capabilities.Capability;
import uk.ac.cf.spring.clientprojectteam3.quiz.Question;
import uk.ac.cf.spring.clientprojectteam3.quiz.Quiz;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizRepository;

import java.util.List;
import java.util.Map;

@Service
public class AdminQuizServiceImpl implements AdminQuizService {
    @Autowired
    private AdminQuizRepository adminQuizRepository;
    @Autowired
    private QuizRepository quizRepository;

    @Override
    public List<Quiz> getQuizzes() {
        return adminQuizRepository.findAllQuizzes();
    }

    @Override
    public void setQuestionsForQuiz(Quiz quiz) {
        List<Question> questions = quizRepository.getQuestions(quiz.getQuizId());
        for(Question question : questions) {
            String capabilityName = adminQuizRepository.getCapabilityById(question.getCapabilityId());
            String capabilityColour = adminQuizRepository.getCapabilityColourById(question.getCapabilityId());
            question.setCapabilityName(capabilityName);
            question.setCapabilityColour(capabilityColour);
        }
        quiz.setQuestions(questions);
    }

    @Override
    public void deleteQuizQuestion(int questionId) {
        adminQuizRepository.deleteQuestion(questionId);
    }

    @Override
    public void createQuiz(String title, Long outcomeId, String description, int time) {
        adminQuizRepository.createQuiz(title,outcomeId,description,time);
    }

    @Override
    public void createQuestion(int quizId, int capabilityId, String text) {
        adminQuizRepository.createQuestion(quizId, capabilityId, text);
    }
}
