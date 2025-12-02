package uk.ac.cf.spring.clientprojectteam3.quiz;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/quiz/{quizId}")
public class QuizController {
    @Autowired
    private QuizService quizService;


    @GetMapping("/attempt/{attemptId}/question/{index}")
    public ModelAndView showQuestion(@PathVariable int attemptId,
                                     @PathVariable int index,
                                     @PathVariable int quizId,
                                     HttpSession session) {
        if (attemptId == 0) {
            return startAttempt(getUserId(), quizId, index);
        }

        QuizContext ctx = quizContext(quizId, attemptId, session);

        if (!quizService.indexValid(ctx.quiz(), index)) {
            return new ModelAndView("redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/0");
        }

        ModelAndView mav = new ModelAndView("quizzes/quiz");

        mav.addObject("quizTitle", ctx.quiz().getName());
        mav.addObject("questionNum", index + 1);
        mav.addObject("question", ctx.quiz().getQuestions().get(index));
        mav.addObject("selectedAnswer", ctx.attempt().getAnswers().get(index));
        mav.addObject("attemptId", attemptId);
        mav.addObject("index", index);
        mav.addObject("total", ctx.quiz().getQuestions().size());
        mav.addObject("answeredCount", ctx.attempt().getAnswers().size());

        quizService.saveAttemptToSession(session, ctx.attempt());
        return mav;
    }

    public ModelAndView startAttempt(long userId, long quizId, long index) {
        long newAttemptId = quizService.startAttempt(userId, quizId);
        return new ModelAndView(
                "redirect:/quiz/" + quizId + "/attempt/" + newAttemptId + "/question/" + index
        );
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
        // switch statement to determine redirect based on user input.
        return switch (nav) {
            case "next" -> "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/" + (index + 1);
            case "prev" -> "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/" + (index - 1);
            case "submit" -> "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/submit";
            case "saveclose" -> "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/save-close";
            default -> "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/0";
        };
    }


    @GetMapping("/attempt/{attemptId}/submit")
    public String submit(@PathVariable int quizId,
                         @PathVariable int attemptId,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        QuizContext ctx = quizContext(quizId, attemptId, session);

        if (!quizService.isComplete(ctx.attempt(), ctx.quiz())) {
            int firstUnanswered = quizService.firstUnansweredIndex(ctx.attempt(), ctx.quiz());
            redirectAttributes.addFlashAttribute("errorMessage", "Please answer all questions before submitting.");
            return "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/" + firstUnanswered;
        }
        long userId = getUserId();
        quizService.submitAttempt(userId, attemptId, ctx.attempt());
        session.removeAttribute("quizAttempt");
        redirectAttributes.addFlashAttribute("successMessage", "Quiz submitted successfully!");
        return "redirect:/quiz-list";
    }

    protected long getUserId() {
        // TODO: dummy user until accounts are implemented
        return 1L;
    }

    @GetMapping("/attempt/{attemptId}/save-close")
    public String saveClose(@PathVariable int quizId,
                         @PathVariable int attemptId,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        QuizContext ctx = quizContext(quizId, attemptId, session);

        long userId = getUserId();
        if(ctx.attempt().getAnswers().isEmpty()) {
            quizService.deleteEmptyAttempt(attemptId, session);
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Quiz saved successfully!");
            quizService.saveIncompleteAttempt(userId, attemptId, ctx.attempt());
        }
        session.removeAttribute("quizAttempt");
        return "redirect:/quiz-list";
    }

    private record QuizContext(QuizAttempt attempt, Quiz quiz) {}

    private QuizContext quizContext(int quizId, int attemptId, HttpSession session) {
        QuizAttempt attempt = quizService.loadAttemptFromSession(quizId, session);
        Quiz quiz = quizService.getQuizForAttempt(quizId,attemptId);
        return new QuizContext(attempt, quiz);
    }

    @GetMapping("**")
    public String handleUnknownPaths(@PathVariable int quizId,
                                     RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", "Quiz not found");
        return "redirect:/quiz-list";
    }
}
