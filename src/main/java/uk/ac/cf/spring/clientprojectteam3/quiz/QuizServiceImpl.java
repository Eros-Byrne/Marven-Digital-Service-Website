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


    public Quiz getQuizForAttempt(long quizId, int attemptId) {
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

    public long startAttempt(long userId, int attemptNumber) {
        return quizRepository.createUserAttempt(userId, attemptNumber);
    }

    public int firstUnansweredIndex(QuizAttempt attempt, Quiz quiz) {
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            if (!attempt.getAnswers().containsKey(i)) return i;
        }
        return -1; // all answered
    }


    public void submitAttempt(long userId, long attemptId, QuizAttempt attempt) {

        // Load quiz questions so we can map index -> questionId
        Quiz quiz = getQuizForAttempt(attempt.getQuizId(), 0);
        List<Question> questions = quiz.getQuestions();

        attempt.getAnswers().forEach((index, score) -> {
            long questionId = questions.get(index).getQuestionId();
            quizRepository.saveAnswer(attemptId, questionId, score);
        });

        quizRepository.markAttemptComplete(attemptId);
    }

}
