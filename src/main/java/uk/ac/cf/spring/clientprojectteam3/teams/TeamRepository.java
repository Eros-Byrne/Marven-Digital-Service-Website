package uk.ac.cf.spring.clientprojectteam3.teams;

public interface TeamRepository {

    Long createTeam(NewTeam newTeam);
    void setUserAsManager(Long userId, Long teamId);
}
