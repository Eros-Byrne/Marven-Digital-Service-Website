package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizCardDTO {

    private int quizId;
    private String quizName;
    private String quizDescription;
    private int timeEstimate;
    private int attemptNumber;
    private int completed;
}
