package uk.ac.cf.spring.clientprojectteam3.teams;

import java.util.List;

public interface TeamService {
    void createNewTeam(NewTeam newTeam);
    List<UserTeam> listOfTeamsForCurrentUser();
}
