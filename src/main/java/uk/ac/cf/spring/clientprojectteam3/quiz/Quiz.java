package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quiz {
    private long quizId;
    private String name;
    private String description;
    private int timeEstimate;
}
