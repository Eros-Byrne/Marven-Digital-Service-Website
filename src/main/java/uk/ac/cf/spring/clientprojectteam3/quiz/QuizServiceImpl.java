package uk.ac.cf.spring.clientprojectteam3.quiz;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;


    // session attempt handling.

    public void saveAttemptToSession(HttpSession session, QuizAttempt attempt) {
        session.setAttribute("quizAttempt", attempt);
    }

    public QuizAttempt loadAttemptFromSession(int quizId, int attemptId, HttpSession session) {
        QuizAttempt attempt = (QuizAttempt) session.getAttribute("quizAttempt");

        if (attempt == null || attempt.getAttemptId() != attemptId) {
            // Create a new session attempt or replace if the attemptId changed
            attempt = new QuizAttempt();
            attempt.setQuizId(quizId);
            attempt.setAttemptId(attemptId);

            // Load saved answers from DB
            Map<Integer, Integer> savedAnswers = quizRepository.getAttemptAnswers(attemptId);
            List<Question> questions = quizRepository.getQuestions(quizId);

            Map<Integer, Integer> answersByIndex = new HashMap<>();
            for (int i = 0; i < questions.size(); i++) {
                long qId = questions.get(i).getQuestionId();
                if (savedAnswers.containsKey((int) qId)) {
                    answersByIndex.put(i, savedAnswers.get((int) qId));
                }
            }

            attempt.setAnswers(answersByIndex);
        }

        return attempt;
    }


    // quiz loading.

    @Override
    public int getOrStartAttempt(long userId, int quizId) {
        Integer latestAttemptId = quizRepository.getLatestAttemptId(userId, quizId);

        if (latestAttemptId == null) {
            // Create a new attempt
            return Math.toIntExact(startAttempt(userId, quizId));
        }

        int completed = quizRepository.getAttemptStatus(latestAttemptId);
        if(completed == 1){
            return Math.toIntExact(startAttempt(userId, quizId));
        }

        return latestAttemptId;
    }

    public Quiz getQuizForAttempt(long quizId, int attemptId) {
        Quiz quiz = quizRepository.getQuiz(quizId);
        quiz.setQuizId(quizId);
        List<Question> quizQuestions = quizRepository.getQuestions(quizId);
        quiz.setQuestions(quizQuestions);
        return quiz;
    }

    // answer recording.

    public void recordAnswer(QuizAttempt attempt, int index, Integer answer) {
        if (answer != null) {
            attempt.getAnswers().put(index, answer);
        }
    }

    public boolean indexValid(Quiz quiz, int index) {
        // checks the question index is valid.
        return index >= 0 && index < quiz.getQuestions().size();
    }

    public boolean isComplete(QuizAttempt attempt, Quiz quiz) {
        // checks the quiz has all questions answered
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            if (!attempt.getAnswers().containsKey(i)) return false;
        }
        return true;
    }

    // persisting attempts.

    public long startAttempt(long userId, long quizId) {
        // creates the user attempt when they start the quiz.
        return quizRepository.createUserAttempt(userId, quizId);
    }

    @Override
    public void saveIncompleteAttempt(long userId, int attemptId, QuizAttempt attempt) {
        saveAnswers(attemptId, attempt);
        quizRepository.markAttemptIncomplete(attemptId);
    }

    @Override
    public void deleteEmptyAttempt(int attemptId, HttpSession session) {
        session.removeAttribute("quizAttempt");
        quizRepository.deleteEmptyAttempt(attemptId);
    }

    @Override
    public void submitAttempt(long userId, long attemptId, QuizAttempt attempt) {
        saveAnswers(attemptId, attempt);
        quizRepository.markAttemptComplete(attemptId);
    }

    private void saveAnswers(long attemptId, QuizAttempt attempt) {
        Quiz quiz = getQuizForAttempt(attempt.getQuizId(), (int) attemptId);
        List<Question> questions = quiz.getQuestions();

        attempt.getAnswers().forEach((index, score) -> {
            long questionId = questions.get(index).getQuestionId();
            quizRepository.saveAnswer(attemptId, questionId, score);
        });
    }


    // lookups.

    public int getAttemptNumber(long userAttemptId) {
        return quizRepository.getAttemptNumber(userAttemptId); // already in your repo
    }

    @Override
    public List<QuizCardDTO> getQuizCards(Integer userId) {
        if(userId == null){
            return quizRepository.getBlankQuizCards();
        } else {
            return quizRepository.getQuizCardsByUserId(userId);
        }
    }

    public int firstUnansweredIndex(QuizAttempt attempt, Quiz quiz) {
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            if (!attempt.getAnswers().containsKey(i)) return i;
        }
        return -1; // all answered
    }




}
