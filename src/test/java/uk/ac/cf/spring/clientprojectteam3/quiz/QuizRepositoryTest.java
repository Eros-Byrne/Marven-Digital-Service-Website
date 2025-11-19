package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

@SpringBootTest
public class QuizRepositoryTest {
    @Autowired
    QuizRepo quizRepo;

    @Test
    public void AddAnswerTest() throws Exception {
        quizRepo.addAnswer(1, 1, 1, 1, 5);
        quizRepo.addAnswer(1, 1, 1, 2, 1);
        quizRepo.addAnswer(1, 1, 1, 3, 6);

        quizRepo.addAnswer(1, 1, 2, 1, 50);
        quizRepo.addAnswer(1, 1, 2, 2, 10);
        quizRepo.addAnswer(1, 1, 2, 3, 60);

        List<Answers> answers = quizRepo.getAnswers(1, 1);
        Assertions.assertEquals(2, answers.size());
        Assertions.assertEquals(3, answers.get(0).getAnswers().size());
        Assertions.assertEquals(5, (int) answers.get(0).getAnswers().get(1L));
        Assertions.assertEquals(60, (int) answers.get(1).getAnswers().get(3L));
    }
}
