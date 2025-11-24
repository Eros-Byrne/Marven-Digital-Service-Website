package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class QuizListController {

    private final QuizRepository quizRepository;

    @Autowired
    public QuizListController(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @GetMapping("/quiz-list")
    public String showQuizList(Model model) {
        try {
            List<Quiz> quizzes = quizRepository.getQuizNames();

            if (quizzes == null || quizzes.isEmpty()) {
                model.addAttribute("noQuizzes", true);
                return "quiz-list";
            }

            model.addAttribute("quizzes", quizzes);
            model.addAttribute("noQuizzes", false);
            return "quiz-list";

        } catch (Exception e) {
            model.addAttribute("error", "Failed to load quizzes.");
            return "quiz-list";
        }
    }
}
