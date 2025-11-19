package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QuizDTO {
    private String title;
    private List<Map<String, Object>> questions;

    public QuizDTO(String title, List<Map<String, Object>> questions) {
        this.title = title;
        this.questions = questions;
    }

    public QuizDTO() {}
}
