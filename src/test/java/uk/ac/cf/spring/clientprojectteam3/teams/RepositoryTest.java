package uk.ac.cf.spring.clientprojectteam3.teams;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@ActiveProfiles("test")
@Import(TeamRepositoryImpl.class)
public class RepositoryTest {

    @Autowired
    private TeamRepository teamRepo;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    public void createTeamShouldInsertNewTeam() {
        NewTeam newTeam = new NewTeam("Test Team", "Test Description");

        Long teamId = teamRepo.createTeam(newTeam);

        // got the idea of using count to check for this from ai but incorporated it myself
        Integer count = jdbc.queryForObject(
                "select count(*) from teams where team_id = ? and team_name = ? and team_description = ?",
                Integer.class, teamId, "Test Team", "Test Description");

        assertEquals(1, count);
    }

    @Test
    public void createTeamShouldInsertAndReturnId() {
        NewTeam newTeam = new NewTeam("Test Team", "Test Description");
        Long teamId = teamRepo.createTeam(newTeam);

        assertNotNull(teamId);
    }

    @Test
    public void setUserAsManagerShouldInsert() {
        NewTeam newTeam = new NewTeam("Test Team", "Test Description");
        Long teamId = teamRepo.createTeam(newTeam);

        teamRepo.setUserAsManager(1L, teamId);

        Integer count = jdbc.queryForObject(
                "select count(*) from team_members where team_id = ? and user_id = ? and is_manager = ?",
                Integer.class, teamId, 1L, true);

        assertEquals(1, count);
    }
}
