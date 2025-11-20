package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    private long quizId;
    private String name;
    private String description;
    private int timeEstimate;
    private List<Question> questions;

    public Quiz(long quizId, String name, String description, int timeEstimate) {
        this.quizId = quizId;
        this.name = name;
        this.description = description;
        this.timeEstimate = timeEstimate;
    }

}
