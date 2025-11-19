package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QuizRepositoryImpl implements QuizRepository {

    @Override
    public QuizDTO getQuiz() {
        List<Map<String, Object>> questions = new ArrayList<>();

        Map<String, Object> q1 = new HashMap<>();
        q1.put("id", 1);
        q1.put("text", "Question 1");
        questions.add(q1);

        Map<String, Object> q2 = new HashMap<>();
        q2.put("id", 2);
        q2.put("text", "Question 2");
        questions.add(q2);

        return new QuizDTO("Basic Quiz", questions);
    }

}
