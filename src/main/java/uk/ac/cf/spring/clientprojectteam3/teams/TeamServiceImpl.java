package uk.ac.cf.spring.clientprojectteam3.teams;

import org.springframework.stereotype.Service;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityService;
import uk.ac.cf.spring.clientprojectteam3.capabilities.Outcome;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;
    private final CapabilityService capabilityService;

    public TeamServiceImpl(TeamRepository aTeamRepository, UserService aCurrentUserService, CapabilityService aCapabilityService) {
        this.teamRepository = aTeamRepository;
        this.userService = aCurrentUserService;
        this.capabilityService = aCapabilityService;
    }

    public void createNewTeam(NewTeam newTeam) {
        Long teamId = teamRepository.createTeam(newTeam);
        Long userId = userService.getCurrentUserId().longValue();

        teamRepository.setUserAsManager(userId, teamId);
    }

    public List<UserTeam> listOfTeamsForCurrentUser() {
        Long userID = userService.getCurrentUserId().longValue();

        return teamRepository.getAllTeamsForAUser(userID);
    }

    public TeamDetails getTeamDetailsForTeam(Long teamId) {
        TeamDetails teamDetails = teamRepository.getTeamDetails(teamId);

        List<TeamMember> allTeamMembers = teamRepository.getTeamMembers(teamId);

        List<TeamMember> managers = allTeamMembers
                .stream()
                .filter(TeamMember::isManager)
                .toList();

        List<TeamMember> members = allTeamMembers
                .stream()
                .filter(teamMember -> !teamMember.isManager())
                .toList();

        teamDetails.setManagers(managers);
        teamDetails.setMembers(members);

        return teamDetails;
    }

    public void promoteTeamMember(Long teamId, Long userId) {
        teamRepository.promoteTeamMember(teamId, userId);
    }

    public void demoteManager(Long teamId, Long userId) {
        Integer numberOfManagers = teamRepository.numberOfManagers(teamId);

        if (numberOfManagers <= 1) {
//            Ai helped with type of exception
            throw new IllegalStateException("A team can't have 0 managers");
        }

        teamRepository.demoteTeamMember(teamId, userId);
    }

    public Boolean isTheCurrentUserAManager(Long teamId) {
        Long userId = userService.getCurrentUserId().longValue();
        return teamRepository.isUserManager(userId, teamId);
    }

    public List<Outcome> listEnabledOutcomes() {
        return capabilityService.getAllOutcomes();
    }

    public List<TopMemberForOutcome> getTopMembersForOutcome(Long teamId, Long outcomeId) {
        return teamRepository.getTopMembersForOutcomes(teamId, outcomeId);
    }
}
