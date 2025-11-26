package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private long questionId;
    private long quizId;
    private String text;
    private long capabilityId;
}
