package uk.ac.cf.spring.clientprojectteam3.teams;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private JdbcTemplate jdbc;
    private RowMapper<UserTeam> userTeamMapper;

    public TeamRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
        setUserTeamRowMapper();
    }

    private void setUserTeamRowMapper() {
        userTeamMapper = (rs, i) -> new UserTeam(
                rs.getLong("team_id"),
                rs.getString("team_name"),
                rs.getBoolean("is_manager"),
                rs.getLong("members_count")
        );
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

    public List<UserTeam> getAllTeamsForAUser(Long userId) {
        String sql = """
                select t.team_id, t.team_name, tm.is_manager,
                       (select count(*) from team_members tm where tm.team_id = t.team_id) as members_count
                from teams t
                join team_members tm on t.team_id = tm.team_id
                where tm.user_id = ?
                order by tm.is_manager desc, t.team_name""";

        return jdbc.query(sql, userTeamMapper, userId);

    }
}
