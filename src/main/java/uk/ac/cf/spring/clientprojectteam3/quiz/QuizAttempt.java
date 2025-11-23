package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class QuizAttempt {
    private long quizId;
    private int currentQuestionIndex;
    private Map<Integer, Integer> answers = new HashMap<>();

}
