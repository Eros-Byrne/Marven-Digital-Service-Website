package uk.ac.cf.spring.clientprojectteam3.teams;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
        mv = new ModelAndView("redirect:/teams");
        return mv;
    }

    @GetMapping("/teams")
    public ModelAndView userTeams() {
        ModelAndView mv = new ModelAndView("teams/teams-list");
        List<UserTeam> teams = teamService.listOfTeamsForCurrentUser();
        mv.addObject("teams", teams);
        return mv;
    }


}
