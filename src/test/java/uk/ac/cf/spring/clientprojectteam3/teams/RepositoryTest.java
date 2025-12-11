package uk.ac.cf.spring.clientprojectteam3.teams;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void shouldReturnTeamsForUserOrderedByRole() {
        List<UserTeam> teams = teamRepo.getAllTeamsForAUser(1L);

        assertEquals(2, teams.size());
        assertEquals("Test team 2", teams.getFirst().getTeamName());
        assertEquals(true, teams.getFirst().getIsManager());
        assertEquals("Test team 1", teams.get(1).getTeamName());
        assertEquals(false, teams.get(1).getIsManager());
    }

    @Test
    public void shouldDeleteTeam(){
        teamRepo.deleteTeam(2L);

        List<Map<String, Object>> teamMembers = jdbc.query("select * from team_members", new ColumnMapRowMapper());
        assertEquals(1, teamMembers.size());
        assertEquals(1L, teamMembers.getFirst().get("user_id"));
        List<Map<String, Object>> teams = jdbc.query("select * from teams", new ColumnMapRowMapper());
        assertEquals(1, teams.size());
        assertEquals("Test team 1", teams.getFirst().get("team_name"));
    }

    @Test
    public void shouldntLeave(){
        assertFalse(teamRepo.leaveTeam(2L,1L));//can't leave as last manager
    }

    @Test
    public void shouldJoinTeam(){
        teamRepo.addTeamMember(123456789, 2L, false);
        List<Map<String, Object>> teamMembers = jdbc.query("select * from team_members where team_id=?", new ColumnMapRowMapper(), 2L);
        assertEquals(2, teamMembers.size());
    }
}
