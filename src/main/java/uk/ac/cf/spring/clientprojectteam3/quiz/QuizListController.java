package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class QuizListController {

    private final QuizRepository quizRepo;

    @Autowired
    public QuizListController(QuizRepository quizRepo) {
        this.quizRepo = quizRepo;
    }

    @GetMapping("/quiz-list")
    public String showQuizList(Model model) {
        try {
            List<Quiz> quizzes = quizRepo.getQuizNames();

            if (quizzes.isEmpty()) {
                model.addAttribute("noQuizzes", true);
            } else {
                model.addAttribute("quizzes", quizzes);
            }
        } catch (Exception e) {
            model.addAttribute("dbError", "Could not load quizzes. Please try again later.");
        }

        return "quiz-list";
    }
}


