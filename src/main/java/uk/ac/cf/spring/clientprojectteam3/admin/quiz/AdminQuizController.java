package uk.ac.cf.spring.clientprojectteam3.admin.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityRepository;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityService;
import uk.ac.cf.spring.clientprojectteam3.quiz.Quiz;

import java.util.List;

@Controller
@RequestMapping("/admin/quizzes")
public class AdminQuizController {

    @Autowired
    private AdminQuizService adminQuizService;

    @Autowired
    private CapabilityService capabilityService;

    @GetMapping("")
    public ModelAndView adminQuizzes(){
        ModelAndView mav = new ModelAndView("admin/quiz-questions-list");
        List<Quiz> quizzes = adminQuizService.getQuizzes();
        mav.addObject("quizzes", quizzes);
        for (Quiz quiz : quizzes) {
            adminQuizService.setQuestionsForQuiz(quiz);
        }
        return mav;
    }

    @PostMapping("/delete-question/{id}")
    public ModelAndView deleteQuestion(@PathVariable("id") int id){
        adminQuizService.deleteQuizQuestion(id);
        return new ModelAndView("redirect:/admin/quizzes");
    }

    @PostMapping("/create")
    public ModelAndView createQuizForm(){
        ModelAndView mav = new ModelAndView("admin/create-quiz");
        mav.addObject("outcomes", capabilityService.getAllOutcomes());
        return mav;
    }

    @PostMapping("/create/add")
    public ModelAndView createQuiz(
            @ModelAttribute("title") String title,
            @ModelAttribute("outcomeId") Long outcomeId,
            @ModelAttribute("time") int time,
            @ModelAttribute("description") String description
            ){
        adminQuizService.createQuiz(title, outcomeId, description, time);
        return new ModelAndView("redirect:/admin/quizzes");
    }

    @PostMapping("/add-question/{id}")
    public ModelAndView addQuestionForm(@PathVariable("id") int id){
        ModelAndView mav = new ModelAndView("admin/create-question");
        Long outcomeId = capabilityService.getOutcomeIdByQuizId(id);
        mav.addObject("capabilities", capabilityService.getAllCapabilitiesByOutcomeId(outcomeId));
        mav.addObject("quizId", id);
        return mav;
    }

    @PostMapping("/add-question/submit/{id}")
    public ModelAndView addQuestion(
            @PathVariable("id") int quizId,
            @ModelAttribute("capabilityId") int capabilityId,
            @ModelAttribute("text") String text
            ){
        ModelAndView mav = new ModelAndView("redirect:/admin/quizzes");
        adminQuizService.createQuestion(quizId, capabilityId, text);
        return mav;
    }
}
