package uk.ac.cf.spring.clientprojectteam3.admin.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.cf.spring.clientprojectteam3.quiz.Question;
import uk.ac.cf.spring.clientprojectteam3.quiz.Quiz;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizRepository;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizRepositoryImpl;

import java.util.List;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import({AdminQuizRepositoryImpl.class, QuizRepositoryImpl.class})
@Transactional
public class RepositoryTests {

    @Autowired
    private AdminQuizRepository adminQuizRepo;

    @Autowired
    private QuizRepository quizRepo;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    public void shouldCreate2Quizzes() {
        boolean quizExists = false;
        adminQuizRepo.createQuiz("Quiz 1", 1L, "description 1", 10);
        adminQuizRepo.createQuiz("Quiz 2", 2L, "description 2", 15);

        List<Quiz> quizzes = adminQuizRepo.findAllQuizzes();
        for(Quiz quiz : quizzes) {
            if (quiz.getName().equals("Quiz 1")) {
                quizExists = true;
                break;
            }
        }
        assertTrue(quizExists);
        assertEquals(2, quizzes.size());
    }

    @Test
    public void shouldCreateAndDeleteQuestion() {
        long quizId = adminQuizRepo.createQuiz("Test Quiz", 1L, "desc", 10);

        adminQuizRepo.createQuestion(Math.toIntExact(quizId), 1, "question 1");

        List<Question> questions = quizRepo.getQuestions(quizId);
        Question created = questions.stream()
                .filter(q -> q.getText().equals("question 1"))
                .findFirst()
                .orElseThrow();
        long questionId = created.getQuestionId();


        assertEquals(0, created.getDisabled());

        adminQuizRepo.deleteQuestion((int) questionId);

        int updatedRows = jdbc.update(
                "UPDATE quiz_questions SET disabled = 1 WHERE question_id = ?",
                created.getQuestionId()
        );
        System.out.println("Rows updated: " + updatedRows);
        assertEquals(1, updatedRows);
    }





}
