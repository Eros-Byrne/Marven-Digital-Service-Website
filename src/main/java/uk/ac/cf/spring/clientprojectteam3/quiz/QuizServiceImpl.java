package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;

    @Override
    public QuizDTO getQuizForAttempt(int attemptId) {

        // TODO: consider db design when implementing.

        return quizRepository.getQuiz();
    }
}
