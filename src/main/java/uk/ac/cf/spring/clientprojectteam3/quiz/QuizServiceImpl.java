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

    public boolean isComplete(QuizAttempt attempt, Quiz quiz) {
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            if (!attempt.getAnswers().containsKey(i)) return false;
        }
        return true;
    }

    public int firstUnansweredIndex(QuizAttempt attempt, Quiz quiz) {
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            if (!attempt.getAnswers().containsKey(i)) return i;
        }
        return -1; // all answered
    }

    public void submitAttempt(int attemptId, QuizAttempt attempt) {
        attempt.getAnswers().forEach((questionIndex, answerValue) -> saveAnswer(attemptId, questionIndex, answerValue));
        markAttemptComplete(attemptId);
    }

    private void markAttemptComplete(int attemptId) {
        System.out.println("Attempt " + attemptId + " has been marked as complete");
    }

    private void saveAnswer(int attemptId, Integer questionIndex, Integer answerValue) {
        System.out.println("Score: " + answerValue + " has been saved to question: " + questionIndex + " for attempt: " + attemptId);
    }

}
