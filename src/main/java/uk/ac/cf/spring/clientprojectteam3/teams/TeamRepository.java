package uk.ac.cf.spring.clientprojectteam3.teams;

import java.util.List;

public interface TeamRepository {

    Long createTeam(NewTeam newTeam);
    void setUserAsManager(Long userId, Long teamId);
    List<UserTeam> getAllTeamsForAUser(Long userId);
}
