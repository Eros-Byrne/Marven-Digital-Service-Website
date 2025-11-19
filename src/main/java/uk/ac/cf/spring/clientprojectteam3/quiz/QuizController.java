package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/quiz")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @GetMapping("")
    public String quiz(Model model) {
        return "redirect:/quiz/attempt/0/question/0";
    }

    @GetMapping("/attempt/{attemptId}/question/{index}")
    public String showQuestion(@PathVariable int attemptId,
                               @PathVariable int index,
                               Model model) {

        QuizDTO quiz = quizService.getQuizForAttempt(attemptId);
        if (index < 0 || index >= quiz.getQuestions().size()) {
            return "redirect:/quiz/attempt/" + attemptId + "/question/0"; // fallback
        }

        Map<String, Object> question = quiz.getQuestions().get(index);

        model.addAttribute("quizTitle", quiz.getTitle());
        model.addAttribute("question", question);
        model.addAttribute("index", index);
        model.addAttribute("total", quiz.getQuestions().size());
        model.addAttribute("attemptId", attemptId);

        return "quiz";
    }
}
