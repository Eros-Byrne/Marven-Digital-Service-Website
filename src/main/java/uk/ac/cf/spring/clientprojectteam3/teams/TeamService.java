package uk.ac.cf.spring.clientprojectteam3.teams;

import uk.ac.cf.spring.clientprojectteam3.capabilities.Outcome;

import java.util.List;

public interface TeamService {
    void createNewTeam(NewTeam newTeam);
    List<UserTeam> listOfTeamsForCurrentUser();
    TeamDetails getTeamDetailsForTeam(Long teamId);
    void promoteTeamMember(Long teamId, Long userId);
    void demoteManager(Long teamId, Long userId);
    Boolean isTheCurrentUserAManager(Long teamId);
    List<Outcome> listEnabledOutcomes();
    List<TopMemberForOutcome> getTopMembersForOutcome(Long teamId, Long outcomeId);

    boolean addNewTeamMember(long joinCode);

    long regenerateTeamCode(long teamID);

    boolean leaveTeam(Long teamID);

    void deleteTeam(Long teamID);
}
