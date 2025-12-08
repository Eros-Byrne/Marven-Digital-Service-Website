package uk.ac.cf.spring.clientprojectteam3.teams;

import org.springframework.stereotype.Service;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;

    public TeamServiceImpl(TeamRepository aTeamRepository, UserService aCurrentUserService) {
        this.teamRepository = aTeamRepository;
        this.userService = aCurrentUserService;
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
}
