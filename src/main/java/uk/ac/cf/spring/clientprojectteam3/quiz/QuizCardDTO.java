package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizCardDTO {

    private long quizId;
    private String quizName;
    private String quizDescription;
    private int timeEstimate;
    private int attemptNumber;
    private int completed;
    private int questionsCompleted;
    private int totalQuestions;

    public QuizCardDTO(long quizId, String name, String description, int timeEstimate) {
        this.quizId = quizId;
        this.quizName = name;
        this.quizDescription = description;
        this.timeEstimate = timeEstimate;
    }
}
