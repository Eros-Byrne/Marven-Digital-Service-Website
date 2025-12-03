package uk.ac.cf.spring.clientprojectteam3.teams;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService aTeamService) {
        teamService = aTeamService;
    }

    @GetMapping("/teams/create")
    public ModelAndView createTeam() {
        ModelAndView mv = new ModelAndView("teams/create_team_form");
        mv.addObject("team", new NewTeam());
        return mv;
    }

    @PostMapping("/teams/create")
    public ModelAndView createNewTeam(@ModelAttribute("team") NewTeam newTeam) {
        ModelAndView mv;
        teamService.createNewTeam(newTeam);
        // NEED TO REDIRECT TO /teams ONCE IT HAS BEEN IMPLEMENTED
        mv = new ModelAndView("redirect:/");
        return mv;
    }


}
