package uk.ac.cf.spring.clientprojectteam3.teams;

import org.springframework.stereotype.Service;
import uk.ac.cf.spring.clientprojectteam3.security.CurrentUserService;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final CurrentUserService currentUserService;

    public TeamServiceImpl(TeamRepository aTeamRepository, CurrentUserService aCurrentUserService) {
        this.teamRepository = aTeamRepository;
        this.currentUserService = aCurrentUserService;
    }

    public void createNewTeam(NewTeam newTeam) {
        Long teamId = teamRepository.createTeam(newTeam);
        Long userId = currentUserService.getCurrentUserId().longValue();

        teamRepository.setUserAsManager(userId, teamId);
    }

}
