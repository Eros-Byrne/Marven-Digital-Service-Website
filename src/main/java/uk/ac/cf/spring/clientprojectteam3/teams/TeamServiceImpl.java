package uk.ac.cf.spring.clientprojectteam3.teams;

import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository aTeamRepository) {
        teamRepository = aTeamRepository;
    }

    public void createNewTeam(NewTeam newTeam) {
        Long teamId = teamRepository.createTeam(newTeam);
        Long userId = getUserId();

        teamRepository.setUserAsManager(userId, teamId);
    }

    private Long getUserId() {
        return 1L;
    }
}
