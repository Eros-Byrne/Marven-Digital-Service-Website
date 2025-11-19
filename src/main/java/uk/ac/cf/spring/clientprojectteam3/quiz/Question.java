package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Question {
    private long questionId;
    private long quizId;
    private String title;
    private String text;
    private long skillId;
}
