package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class SummaryController {

    private final QuizService quizService;

    @Autowired
    public SummaryController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/results")
    public String results(Model model) {
        int userID = 1;
        List<Map.Entry<Quiz, List<Map.Entry<Long, AttemptDTO>>>> attempts = new ArrayList<>();

        List<Quiz> quizNames = quizService.getQuizNames();
        //Creates lists to add pairs of attempt numbers and attempt data transfer objects. They are then paired with quiz objects
        for(Quiz quizName : quizNames){
            attempts.add(new AbstractMap.SimpleEntry<>(quizName, new ArrayList<>()));
            for(int attempt = 0; attempt < quizService.getCurrentAttempt(userID); attempt++){
                attempts.getLast().getValue().add(new AbstractMap.SimpleEntry<>((long)attempt, quizService.getAttemptForQuiz((int) quizName.getQuizId(), userID, attempt)));
                //attempts.getLast().getValue().add(new AbstractMap.SimpleEntry<Long, AttemptDTO>((long)attempt, new AttemptDTO()));
            }
        }

        model.addAttribute("attempts", attempts);

        return "results.html";
    }
}
