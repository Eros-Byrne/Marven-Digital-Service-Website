package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;

@Data
@AllArgsConstructor
public class Answers {
    private long quizId;
    private long userId;
    private HashMap<Long, Integer> answers;
    private int attemptNumber;
}
