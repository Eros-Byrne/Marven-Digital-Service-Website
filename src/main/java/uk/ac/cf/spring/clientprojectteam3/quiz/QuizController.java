
package uk.ac.cf.spring.clientprojectteam3.quiz;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

@Controller
@RequestMapping("/quiz/{quizId}")
public class QuizController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private UserService currentUserService;


    @GetMapping("/attempt/question/{index}")
    public ModelAndView showQuestion(@PathVariable int index,
                                     @PathVariable int quizId,
                                     HttpSession session) {
        long userId = currentUserService.getCurrentUserId();
        int attemptId = quizService.getOrStartAttempt(userId, quizId);
        QuizContext ctx = quizContext(quizId, attemptId, session);

        if (!quizService.indexValid(ctx.quiz(), index)) {
            return new ModelAndView("redirect:/quiz/" + quizId + "/attempt/question/0");
        }

        ModelAndView mav = new ModelAndView("quizzes/quiz");

        mav.addObject("quizTitle", ctx.quiz().getName());
        mav.addObject("questionNum", index + 1);
        mav.addObject("question", ctx.quiz().getQuestions().get( index));
        mav.addObject("selectedAnswer", ctx.attempt().getAnswers().get(index));
        mav.addObject("attemptId", quizService.getAttemptNumber(ctx.attempt().getAttemptId()));
        mav.addObject("index", index);
        mav.addObject("total", ctx.quiz().getQuestions().size());
        mav.addObject("answeredCount", ctx.attempt().getAnswers().size());

        quizService.saveAttemptToSession(session, ctx.attempt());
        return mav;
    }

    @PostMapping("/attempt/question/{index}/answer")
    public String answer(@PathVariable int quizId,
                         @PathVariable int index,
                         @RequestParam(required = false) Integer answer,
                         @RequestParam String nav,
                         HttpSession session) {

        long userId = currentUserService.getCurrentUserId();
        int attemptId = quizService.getOrStartAttempt(userId, quizId);
        QuizAttempt attempt = quizService.loadAttemptFromSession(quizId, attemptId, session);

        attempt.setCurrentQuestionIndex(index);
        quizService.recordAnswer(attempt, index, answer);
        quizService.saveAttemptToSession(session, attempt);
        // switch statement to determine redirect based on user input.
        return switch (nav) {
            case "next" -> "redirect:/quiz/" + quizId + "/attempt/question/" + (index + 1);
            case "prev" -> "redirect:/quiz/" + quizId + "/attempt/question/" + (index - 1);
            case "submit" -> "redirect:/quiz/" + quizId + "/attempt/submit";
            case "saveclose" -> "redirect:/quiz/" + quizId + "/attempt/save-close";
            default -> "redirect:/quiz/" + quizId + "/attempt/question/0";
        };
    }


    @GetMapping("/attempt/submit")
    public String submit(@PathVariable int quizId,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        long userId = currentUserService.getCurrentUserId();
        int attemptId = quizService.getOrStartAttempt(userId, quizId);
        QuizContext ctx = quizContext(quizId, attemptId, session);

        if (!quizService.isComplete(ctx.attempt(), ctx.quiz())) {
            int firstUnanswered = quizService.firstUnansweredIndex(ctx.attempt(), ctx.quiz());
            redirectAttributes.addFlashAttribute("errorMessage", "Please answer all questions before submitting.");
            return "redirect:/quiz/" + quizId + "/attempt/question/" + firstUnanswered;
        }
        quizService.submitAttempt(userId, attemptId, ctx.attempt());
        session.removeAttribute("quizAttempt");
        redirectAttributes.addFlashAttribute("successMessage", "Quiz submitted successfully!");

        // CHANGED: Redirect to summary page instead of quiz-list
        return "redirect:/summary/quiz/" + quizId + "/attempt/"+attemptId;
    }

    @GetMapping("/attempt/save-close")
    public String saveClose(@PathVariable int quizId,

                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        long userId = currentUserService.getCurrentUserId();
        int attemptId = quizService.getOrStartAttempt(userId, quizId);
        QuizContext ctx = quizContext(quizId, attemptId, session);

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
        QuizAttempt attempt = quizService.loadAttemptFromSession(quizId, attemptId, session);
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