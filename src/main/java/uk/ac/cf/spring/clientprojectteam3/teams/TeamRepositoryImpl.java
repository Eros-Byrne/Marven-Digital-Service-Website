package uk.ac.cf.spring.clientprojectteam3.teams;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private JdbcTemplate jdbc;

    public TeamRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    public Long createTeam(NewTeam newTeam) {

        // Inserts new team into database and gets the team_id stored in the keyholder
        String createTeamSql = "insert into teams (team_name, team_description) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(createTeamSql, new String[] {"team_id"});
            ps.setString(1, newTeam.getTeamName());
            ps.setString(2, newTeam.getTeamDescription());
            return ps;
        }, keyHolder);

        // Extracts the id for the new team
        return keyHolder.getKey().longValue();
    }

    public void setUserAsManager(Long userId, Long teamId) {
        String sql = "insert into team_members (team_id, user_id, is_manager) values (?, ?, ?)";

        jdbc.update(sql, teamId, userId, true);
    }
}
