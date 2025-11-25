package uk.ac.cf.spring.clientprojectteam3.quiz;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/quiz/{quizId}")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @GetMapping("")
    public String quiz(Model model) {
        return "redirect:/quiz-list";
    }

    @GetMapping("/attempt/{attemptId}/question/{index}")
    public ModelAndView showQuestion(@PathVariable int attemptId,
                                     @PathVariable int index,
                                     @PathVariable int quizId,
                                     HttpSession session) {

        QuizAttempt attempt = quizService.loadAttemptFromSession(quizId, session);
        Quiz quiz = quizService.getQuizForAttempt(quizId, attemptId);

        if (!quizService.indexValid(quiz, index)) {
            return new ModelAndView("redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/0");
        }

        ModelAndView mav = new ModelAndView("quiz");

        mav.addObject("quizTitle", quiz.getName());
        mav.addObject("questionNum", index + 1);
        mav.addObject("question", quiz.getQuestions().get(index));
        mav.addObject("selectedAnswer", attempt.getAnswers().get(index));
        mav.addObject("attemptId", attemptId);
        mav.addObject("index", index);
        mav.addObject("total", quiz.getQuestions().size());

        quizService.storeAttempt(session, attempt);
        return mav;
    }


    @PostMapping("/attempt/{attemptId}/question/{index}/answer")
    public String answer(@PathVariable int quizId,
                         @PathVariable int attemptId,
                         @PathVariable int index,
                         @RequestParam(required = false) Integer answer,
                         @RequestParam String nav,
                         HttpSession session) {

        QuizAttempt attempt = quizService.loadAttemptFromSession(quizId, session);

        attempt.setCurrentQuestionIndex(index);
        quizService.recordAnswer(attempt, index, answer);
        quizService.saveAttemptToSession(session, attempt);

        return switch (nav) {
            case "next" -> "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/" + (index + 1);
            case "prev" -> "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/" + (index - 1);
            case "submit" -> "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/submit";
            case "saveclose" -> "redirect:/dashboard";
            default -> "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/0";
        };
    }


    @GetMapping("/attempt/{attemptId}/submit")
    public String submit(@PathVariable int quizId,
                         @PathVariable int attemptId,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        QuizAttempt attempt = quizService.loadAttemptFromSession(quizId, session);
        Quiz quiz = quizService.getQuizForAttempt(quizId, attemptId);

        if (!quizService.isComplete(attempt, quiz)) {
            int firstUnanswered = quizService.firstUnansweredIndex(attempt, quiz);
            redirectAttributes.addFlashAttribute("errorMessage", "Please answer all questions before submitting.");
            return "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/" + firstUnanswered;
        }

        quizService.submitAttempt(attemptId, attempt);
        session.removeAttribute("quizAttempt");

        return "redirect:/quiz-list";
    }
}
