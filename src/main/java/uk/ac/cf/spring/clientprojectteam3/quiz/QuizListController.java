package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static javafx.beans.binding.Bindings.when;

@Controller
public class QuizListController {

    private final QuizRepository quizRepo;
    @Autowired
    private QuizController quizController;

    @Autowired
    public QuizListController(QuizRepository quizRepo) {
        this.quizRepo = quizRepo;
    }

    @GetMapping("/quiz-list")
    public String showQuizList(Model model) {
        try {
            long user_id = quizController.getUserId();
            List<QuizCardDTO> quizzes = quizRepo.getQuizCardsByUserId(user_id);
            if (quizzes == null || quizzes.isEmpty()) {
                model.addAttribute("noQuizzes", true);
            } else {
                model.addAttribute("quizzes", quizzes);
            }
        } catch (Exception e) {
            model.addAttribute("dbError", "Could not load quizzes. Please try again later.");
        }

        return "quizzes/quiz-list";
    }
}


