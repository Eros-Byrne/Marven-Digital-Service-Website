package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;


    @Override
    public Quiz getQuizForAttempt(int quizId, int attemptId) {
        Quiz quiz = quizRepository.getQuiz(quizId);
        quiz.setQuizId(quizId);
        List<Question> quizQuestions = quizRepository.getQuestions(quizId);
        quiz.setQuestions(quizQuestions);

        return quiz;
    }

    @Override
    public List<Quiz> getQuizNames() {
        return quizRepository.getQuizNames();
    }

    public List<Question> getQuestionsForQuiz(int quizId){
        return quizRepository.getQuestions(quizId);
    }

    public AttemptDTO getAttemptForQuiz(int quizId, int userId, int attemptNumber){
        return quizRepository.getAttempt(quizId, userId, attemptNumber);
    }

    public Long getCurrentAttempt(int userId){
        return 1L;
    }
}
