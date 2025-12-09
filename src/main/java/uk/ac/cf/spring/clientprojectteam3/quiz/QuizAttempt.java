package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class QuizAttempt {
    private int userAttemptId;
    private long quizId;
    private long attemptId;
    private int currentQuestionIndex;
    private Map<Integer, Integer> answers = new HashMap<>();

}
