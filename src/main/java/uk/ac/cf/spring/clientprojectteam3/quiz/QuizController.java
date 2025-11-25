package uk.ac.cf.spring.clientprojectteam3.quiz;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/quiz/{quizId}")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @GetMapping("")
    public String quiz(Model model) {
        return "redirect:/quiz/1/attempt/0/question/0";
    }

    @GetMapping("/attempt/{attemptId}/question/{index}")
    public String showQuestion(@PathVariable int attemptId,
                               @PathVariable int index,
                               @PathVariable int quizId,
                               Model model,
                               HttpSession session) {
        QuizAttempt attempt = isThereAQuizAttemptInSession(session) ? getAttemptFromSession(session) : new QuizAttempt();
        attempt.setQuizId(quizId);

        Quiz quiz = quizService.getQuizForAttempt(quizId, attemptId);

        if (index < 0 || index >= quiz.getQuestions().size()) {
            return "redirect:/quiz/"+ quizId +"/attempt/" + attemptId + "/question/0"; // fallback
        }

        Question question = quiz.getQuestions().get(index);
        Integer selectedAnswer = attempt.getAnswers().get(index);


        model.addAttribute("quizTitle", quiz.getName());
        model.addAttribute("questionNum", index+1);
        model.addAttribute("question", question);
        model.addAttribute("index", index);
        model.addAttribute("total", quiz.getQuestions().size());
        model.addAttribute("attemptId", attemptId);
        model.addAttribute("selectedAnswer", selectedAnswer);

        session.setAttribute("quizAttempt", attempt);
        return "quiz";
    }

    @PostMapping("/attempt/{attemptId}/question/{index}/answer")
    public String saveAnswerToSession(@PathVariable int quizId,
                                      @PathVariable int attemptId,
                                      @PathVariable int index,
                                      @RequestParam(name="answer", required=false) Integer answer,
                                      @RequestParam(name="nav") String nav,
                                      HttpSession session) {
        QuizAttempt attempt;
        if(isThereAQuizAttemptInSession(session)){
            attempt = getAttemptFromSession(session);
        } else {
            System.out.println("NEW ATTEMPT");
            attempt = new QuizAttempt();
            attempt.setQuizId(quizId);
        }

        attempt.setCurrentQuestionIndex(index);
        putAnswerInSession(index, session, attempt, answer);

        int newIndex;
        switch (nav) {
            case "next": newIndex = index + 1; break;
            case "prev": newIndex = index - 1; break;
            case "saveclose": return "redirect:/dashboard";
            case "submit":
                System.out.println(session.getAttribute("quizAttempt"));
                return "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/submit";
            default: return "redirect:/quiz/1/attempt/0/question/0";
        }
        return "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/" + (newIndex);
    }

    private void putAnswerInSession(Integer index, HttpSession session, QuizAttempt attempt, Integer answer) {
        if (answer != null) {
            attempt.getAnswers().put(index, answer);
        }
        session.setAttribute("quizAttempt", attempt);
    }

    @GetMapping("/attempt/{attemptId}/submit")
    public String submitQuiz(@PathVariable int quizId,
                             @PathVariable int attemptId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        QuizAttempt attempt = getAttemptFromSession(session);
        if (attempt == null) return "redirect:/quiz-list";

        Quiz quiz = quizService.getQuizForAttempt(quizId, attemptId);
        // checks if quiz is complete
        if (!quizService.isComplete(attempt, quiz)) {
            // finds first unanswered question to take the user back to
            int firstUnanswered = quizService.firstUnansweredIndex(attempt, quiz);
            redirectAttributes.addFlashAttribute("errorMessage", "Please answer all questions before submitting.");
            return "redirect:/quiz/" + quizId + "/attempt/" + attemptId + "/question/" + firstUnanswered;
        }

        quizService.submitAttempt(attemptId, attempt); // saves all answers and marks complete
        session.removeAttribute("quizAttempt");

        return "redirect:/quiz-list";
    }


    private QuizAttempt getAttemptFromSession(HttpSession session) {
        return (QuizAttempt) session.getAttribute("quizAttempt");
    }

    private boolean isThereAQuizAttemptInSession(HttpSession session) {
        return session.getAttribute("quizAttempt") != null;
    }
}
