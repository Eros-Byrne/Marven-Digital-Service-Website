package uk.ac.cf.spring.clientprojectteam3.teams;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.cf.spring.clientprojectteam3.capabilities.Outcome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
    public ModelAndView userTeams(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("teams/teams-list");
        List<UserTeam> teams = teamService.listOfTeamsForCurrentUser();
        mv.addObject("teams", teams);
        Map<String, ?> errorMap = RequestContextUtils.getInputFlashMap(request);
        if(errorMap != null) {
            errorMap.forEach(mv::addObject);
        }
        return mv;
    }

    @PostMapping("/teams/join")
    public ModelAndView joinTeam(@RequestParam(value = "joinCode", required = true) Long joinCode, RedirectAttributes ra) {
        ModelAndView mv = new ModelAndView("redirect:/teams");
        if(!teamService.addNewTeamMember(joinCode)){
            ra.addFlashAttribute("joinError", "Invalid join code");
            return mv;
        }
        return mv;
    }

    @GetMapping("/teams/{id}")
    public ModelAndView userTeam(@PathVariable("id") Long teamId,
                                 @RequestParam(value = "outcomeId", required = false) Long outcomeId) {
        ModelAndView mv = new ModelAndView("teams/single-team-page");
        mv.addObject("teamId", teamId);
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

    @PostMapping("/teams/leave")
    public ResponseEntity<String> leaveTeam(@RequestParam("teamID") Long teamID) {
        Stream<UserTeam> managerTeamsWithTeamID = teamService.listOfTeamsForCurrentUser().stream().filter((userTeam -> userTeam.getTeamId().equals(teamID)));
        if(managerTeamsWithTeamID.findAny().isPresent()){
            if(teamService.leaveTeam(teamID)){
                return new ResponseEntity<String>("Successfully left team", HttpStatus.OK);
            }
            return new ResponseEntity<String>("You are the last manager", HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/teams/joinCode/{teamID}")
    public ResponseEntity<Long> joinCode(@PathVariable("teamID") Long teamID) {
        List<UserTeam> teams = teamService.listOfTeamsForCurrentUser();
        for(UserTeam team : teams){
            if(team.getTeamId().equals(teamID)){
                return new ResponseEntity<Long>(team.getJoinCode(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<Long>(-1L, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/teams/resetCode")
    @ResponseStatus(HttpStatus.OK)
    public void resetCode(@RequestParam("teamID") Long teamID) {
        Stream<UserTeam> managerTeamsWithTeamID = teamService.listOfTeamsForCurrentUser().stream().filter((userTeam -> userTeam.getTeamId().equals(teamID) && userTeam.getIsManager()));
        if(managerTeamsWithTeamID.findAny().isPresent()){
            teamService.regenerateTeamCode(teamID);
            return;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/teams/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTeam(@RequestParam("teamID") Long teamID) {
        Stream<UserTeam> managerTeamsWithTeamID = teamService.listOfTeamsForCurrentUser().stream().filter((userTeam -> userTeam.getTeamId().equals(teamID) && userTeam.getIsManager()));
        if(managerTeamsWithTeamID.findAny().isPresent()){
            teamService.deleteTeam(teamID);
            return;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}
