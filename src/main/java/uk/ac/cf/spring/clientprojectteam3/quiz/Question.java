package uk.ac.cf.spring.clientprojectteam3.quiz;

import lombok.Data;

@Data
public class Question {
    private Long id;
    private String question;
    private int answer;
}
