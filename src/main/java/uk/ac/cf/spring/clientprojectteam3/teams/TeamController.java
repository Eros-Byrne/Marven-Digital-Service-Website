package uk.ac.cf.spring.clientprojectteam3.teams;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.cf.spring.clientprojectteam3.capabilities.Outcome;

import java.util.ArrayList;
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

    @GetMapping("/teams/{id}")
    public ModelAndView userTeam(@PathVariable("id") Long teamId,
                                 @RequestParam(value = "outcomeId", required = false) Long outcomeId) {
        ModelAndView mv = new ModelAndView("teams/single-team-page");
        TeamDetails details = teamService.getTeamDetailsForTeam(teamId);
        mv.addObject("teamDetails", details);
        Boolean isManager = teamService.isTheCurrentUserAManager(teamId);
        mv.addObject("isManager", isManager);


        List<Outcome> outcomes = teamService.listEnabledOutcomes();
        mv.addObject("outcomes", outcomes);

        // if outcomeid = null (user hasnt filtered) so outcome 1 is displayed
        Long selectedOutcomeId = outcomeId;
        if (selectedOutcomeId == null && !outcomes.isEmpty()) {
            selectedOutcomeId = outcomes.get(0).getId();
        }

        mv.addObject("selectedOutcomeId", selectedOutcomeId);

        List<TopMemberForOutcome> outcomeLeaders;
        if (selectedOutcomeId == null) {
            outcomeLeaders = new ArrayList<>();
        } else {
            outcomeLeaders = teamService.getTopMembersForOutcome(teamId, selectedOutcomeId);
        }

        mv.addObject("leaders", outcomeLeaders);

        return mv;
    }

    @PostMapping("/teams/{teamId}/members/{memberId}/promote")
    public ModelAndView promoteMember(@PathVariable("teamId") Long teamId,
                                      @PathVariable("memberId") Long memberId,
                                      RedirectAttributes ra) {
        ModelAndView mv;
        teamService.promoteTeamMember(teamId, memberId);
        ra.addFlashAttribute("promoted", "Member successfully promoted");
        mv = new ModelAndView("redirect:/teams/" + teamId);
        return mv;
    }

    @PostMapping("/teams/{teamId}/members/{memberId}/demote")
    public ModelAndView demoteManager(@PathVariable("teamId") Long teamId,
                                      @PathVariable("memberId") Long memberId,
                                      RedirectAttributes ra) {
//        AI helped with ra - all other logic was mine :)
        ModelAndView mv;
        try {
            teamService.demoteManager(teamId, memberId);
            ra.addFlashAttribute("demoted", "Member demoted successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        mv = new ModelAndView("redirect:/teams/" + teamId);
        return mv;
    }


}
