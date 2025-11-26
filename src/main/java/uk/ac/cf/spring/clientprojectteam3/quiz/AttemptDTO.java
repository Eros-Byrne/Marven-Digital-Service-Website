package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class AttemptDTO {
    private long quizId;
    private long userId;
    private HashMap<Question, Integer> answers;
    private int attemptNumber;
}
