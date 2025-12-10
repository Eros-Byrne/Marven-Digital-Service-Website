package uk.ac.cf.spring.clientprojectteam3.admin.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.cf.spring.clientprojectteam3.quiz.Question;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

    @SpringBootTest
    @ActiveProfiles("test")
    @Transactional
    class QuizContainerTests {

        @Autowired
        private QuizRepository quizRepo;
        @Autowired
        private AdminQuizRepository adminQuizRepo;

        @Test
        void shouldCreateQuizWithQuestions() {
            long quizId = adminQuizRepo.createQuiz("Sample Quiz", 1L, "description", 10);

            adminQuizRepo.createQuestion((int) quizId, 1, "Q1");
            adminQuizRepo.createQuestion((int) quizId, 2, "Q2");

            List<Question> questions = quizRepo.getQuestions(quizId);

            assertEquals(2, questions.size());

            Question q1 = questions.stream()
                    .filter(q -> q.getText().equals("Q1"))
                    .findFirst()
                    .orElseThrow();

            Question q2 = questions.stream()
                    .filter(q -> q.getText().equals("Q2"))
                    .findFirst()
                    .orElseThrow();

            assertEquals("Q1", q1.getText());
            assertEquals("Q2", q2.getText());

        }

    }
