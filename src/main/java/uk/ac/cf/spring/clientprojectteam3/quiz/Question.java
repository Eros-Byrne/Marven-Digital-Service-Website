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
    private String capabilityName;
    private String capabilityColour;
    private int disabled;

    public Question(long questionId, String text) {
        this.questionId = questionId;
        this.text = text;
    }

    public Question(long questionId, long quizId, String text, long capabilityId) {
        this.questionId = questionId;
        this.text = text;
        this.quizId = quizId;
        this.capabilityId = capabilityId;
    }

}
