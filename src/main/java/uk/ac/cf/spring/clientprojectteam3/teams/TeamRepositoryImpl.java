package uk.ac.cf.spring.clientprojectteam3.teams;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private JdbcTemplate jdbc;
    private RowMapper<UserTeam> userTeamMapper;
    private RowMapper<TeamDetails> teamDetailsMapper;
    private RowMapper<TeamMember> teamMembersMapper;

    public TeamRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
        setUserTeamRowMapper();
        setTeamDetailsRowMapper();
        setTeamMembersRowMapper();
    }

    private void setUserTeamRowMapper() {
        userTeamMapper = (rs, i) -> new UserTeam(
                rs.getLong("team_id"),
                rs.getString("team_name"),
                rs.getBoolean("is_manager"),
                rs.getLong("join_code"),
                rs.getLong("members_count")
        );
    }

    private void setTeamDetailsRowMapper() {
        teamDetailsMapper = (rs, i) -> new TeamDetails(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getLong("join_code"),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    private void setTeamMembersRowMapper() {
        teamMembersMapper = (rs, i) -> new TeamMember(
                rs.getLong("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getBoolean("manager")
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
                select t.team_id, t.team_name, tm.is_manager, t.join_code,
                       (select count(*) from team_members tm where tm.team_id = t.team_id) as members_count
                from teams t
                join team_members tm on t.team_id = tm.team_id
                where tm.user_id = ?
                order by tm.is_manager desc, t.team_name""";

        return jdbc.query(sql, userTeamMapper, userId);
    }

    public TeamDetails getTeamDetails(Long teamId) {
        String sql = """
                select team_id as id, team_name as name, team_description as description, join_code
                from teams
                where team_id = ?""";

        return jdbc.queryForObject(sql, teamDetailsMapper, teamId);
    }

    public List<TeamMember> getTeamMembers(Long teamId) {
        String sql = """
                select u.user_id, u.name, u.email, tm.is_manager as manager
                from users u
                JOIN team_members tm ON tm.user_id = u.user_id
                where team_id = ?
                order by manager desc , name""";

        return jdbc.query(sql, teamMembersMapper, teamId);
    }

    public void promoteTeamMember(Long teamId, Long userId) {
        String sql = """
                update team_members set is_manager = true
                where team_id = ? and user_id = ?""";

        jdbc.update(sql, teamId, userId);
    }

    public void demoteTeamMember(Long teamId, Long userId) {
        String sql = """
                update team_members set is_manager = false
                where team_id = ? and user_id = ?""";

        jdbc.update(sql, teamId, userId);
    }

    public Integer numberOfManagers(Long teamId) {
        String sql = """
                select count(*) from team_members
                where team_id = ? and is_manager = true""";

        return jdbc.queryForObject(sql, Integer.class, teamId);
    }

    public Boolean isUserManager(Long userId, Long teamId) {
        String sql = """
                select is_manager from team_members
                where team_id = ? and user_id = ?""";

        return jdbc.queryForObject(sql, Boolean.class, teamId, userId);
    }

    public List<TopMemberForOutcome> getTopMembersForOutcomes(Long teamId, Long outcomeId) {
//        Created by ai :)

        String sql = """
        SELECT
            u.user_id,
            u.name,
            u.email,
            -- percentage: sum of latest attempt scores / (questions * 5) * 100
            COALESCE(
              (SUM(COALESCE(a.score, 0)) / NULLIF(COUNT(qq.question_id) * 5, 0)) * 100.0,
              0
            ) AS latest_percentage
        FROM team_members tm
        JOIN users u            ON u.user_id = tm.user_id
        JOIN user_attempt ua    ON ua.user_id = u.user_id
        JOIN quiz q             ON q.quiz_id = ua.quiz_id
        JOIN quiz_questions qq  ON qq.quiz_id = q.quiz_id
        JOIN capabilities cap   ON cap.capability_id = qq.capability_id
        JOIN outcomes o         ON o.outcome_id = cap.outcome_id
        LEFT JOIN answer a      ON a.user_attempt_id = ua.user_attempt_id
                               AND a.question_id     = qq.question_id
        WHERE tm.team_id   = ?
          AND o.outcome_id = ?
          AND ua.complete  = 1
          AND ua.attempt   = (
              SELECT MAX(ua2.attempt)
              FROM user_attempt ua2
              WHERE ua2.user_id = u.user_id
                AND ua2.quiz_id = q.quiz_id
          )
        GROUP BY u.user_id, u.name, u.email
        ORDER BY latest_percentage DESC, u.name ASC
        LIMIT 3
        """;

            return jdbc.query(sql, (rs, i) -> new TopMemberForOutcome(
                    rs.getLong("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDouble("latest_percentage")  // store the % here
            ), teamId, outcomeId);
        }

    public boolean isCodeAlreadyPresent(long code) {
        String sql = "select join_code from teams where join_code = ?";

        List<Map<String, Object>> join_code = jdbc.query(sql, new ColumnMapRowMapper(), code);
        return !join_code.isEmpty();
    }

    public void setTeamCode(long teamID, long joinCode) {
        String sql = "update teams set join_code = ? where team_id = ?";

        jdbc.update(sql, joinCode, teamID);
    }

    public boolean addTeamMember(long joinCode, Long userID, boolean isManager) {
        String sqlSelectTeam = "select count(team_id) from teams where join_code = ?";
        Object joinCodePresentCheck = jdbc.queryForMap(sqlSelectTeam, joinCode).get("count(team_id)");
        if((Long)joinCodePresentCheck == 0) {
            return false;
        }

        String sqlSelectTeamID = "select team_id from teams where join_code = ? limit 1";
        Long teamID = (Long) jdbc.queryForMap(sqlSelectTeamID, joinCode).get("team_id");

        String sqlCheckDuplicate = "select * from team_members where user_id=? and team_id=?";
        List<Map<String, Object>> checkDuplicate = jdbc.query(sqlCheckDuplicate, new ColumnMapRowMapper(), userID, teamID);
        if(!checkDuplicate.isEmpty()) {
            return false;
        }


        String sqlInsert = "insert into team_members (team_id, user_id, is_manager) values (?, ?, ?)";

        jdbc.update(sqlInsert, teamID, userID, isManager);
        return true;
    }

    @Override
    public boolean leaveTeam(Long teamID, Long userID) {
        String sqlTeamMemberDetails = "select is_manager from team_members where team_id=? and user_id=?";
        List<Map<String, Object>> memberDetail = jdbc.query(sqlTeamMemberDetails, new ColumnMapRowMapper(), teamID, userID);
        if(memberDetail.isEmpty()) {
            return false;
        }
        if((Boolean) memberDetail.getFirst().get("is_manager")) {
            String sqlSelectTeam = "select * from team_members where team_id = ? and is_manager = 1";
            List<Map<String, Object>> managerMembers = jdbc.query(sqlSelectTeam, new ColumnMapRowMapper(), teamID);
            if(managerMembers.size() == 1) {
                return false;
            }
        }

        String sqlInsert = "delete from team_members where team_id = ? and user_id = ?";

        jdbc.update(sqlInsert, teamID, userID);

        return true;
    }

    public void deleteTeam(Long teamID) {
        String deleteTeamMembersSql = "delete from team_members where team_id = ?";
        jdbc.update(deleteTeamMembersSql, teamID);

        String deleteTeamSql = "delete from teams where team_id = ?";
        jdbc.update(deleteTeamSql, teamID);
    }
}
