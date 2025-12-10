package uk.ac.cf.spring.clientprojectteam3.teams;

import java.util.List;

public interface TeamRepository {

    Long createTeam(NewTeam newTeam);
    void setUserAsManager(Long userId, Long teamId);
    List<UserTeam> getAllTeamsForAUser(Long userId);
    TeamDetails getTeamDetails(Long teamId);
    List<TeamMember> getTeamMembers(Long teamId);
    void promoteTeamMember(Long teamId, Long userId);
    void demoteTeamMember(Long teamId, Long userId);
    Integer numberOfManagers(Long teamId);
    Boolean isUserManager(Long userId, Long teamId);
    List<TopMemberForOutcome> getTopMembersForOutcomes(Long teamId, Long outcomeId);
}
