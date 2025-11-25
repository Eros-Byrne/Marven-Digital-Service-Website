package uk.ac.cf.spring.clientprojectteam3.quiz;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;


    public void storeAttempt(HttpSession session, QuizAttempt attempt) {
        session.setAttribute("quizAttempt", attempt);
    }

    public void recordAnswer(QuizAttempt attempt, int index, Integer answer) {
        if (answer != null) {
            attempt.getAnswers().put(index, answer);
        }
    }

    public void saveAttemptToSession(HttpSession session, QuizAttempt attempt) {
        session.setAttribute("quizAttempt", attempt);
    }


    public QuizAttempt loadAttemptFromSession(int quizId, HttpSession session) {
        QuizAttempt attempt = (QuizAttempt) session.getAttribute("quizAttempt");

        if (attempt == null) {
            attempt = new QuizAttempt();
            attempt.setQuizId(quizId);
        }
        return attempt;
    }

    public boolean indexValid(Quiz quiz, int index) {
        return index >= 0 && index < quiz.getQuestions().size();
    }

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
