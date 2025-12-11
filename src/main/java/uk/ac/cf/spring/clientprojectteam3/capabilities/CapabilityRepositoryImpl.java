package uk.ac.cf.spring.clientprojectteam3.capabilities;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import uk.ac.cf.spring.clientprojectteam3.admin.capability.AdminCapability;
import uk.ac.cf.spring.clientprojectteam3.admin.outcome.AdminOutcome;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CapabilityRepositoryImpl implements CapabilityRepository {

    private JdbcTemplate jdbc;
    private RowMapper<Capability> capabilityMapper;
    private RowMapper<Resource> resourceMapper;
    private RowMapper<Skill> skillMapper;
    private RowMapper<AdminOutcome> adminOutcomeMapper;

    public CapabilityRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
        setCapabilityRowMapper();
        setResourceRowMapper();
        setSkillRowMapper();
        setAdminRowMapper();
    }

    private void setCapabilityRowMapper() {
        capabilityMapper = (rs, i) -> new Capability(
                rs.getLong("capability_id"),
                rs.getString("title"),
                rs.getString("description")
        );
    }

    private void setResourceRowMapper() {
        resourceMapper = (rs, i) -> new Resource(
                rs.getLong("resource_id"),
                rs.getString("content"),
                Difficulty.valueOf(rs.getString("difficulty"))
        );
    }

    private void setSkillRowMapper() {
        skillMapper = (rs, i) -> new Skill(
                rs.getLong("skill_id"),
                rs.getString("name")
        );
    }

    private void setAdminRowMapper() {
        adminOutcomeMapper = (rs, i) -> new AdminOutcome(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getInt("capability_count")
        );
    }

    // CAPABILITY LOGIC
    public Optional<Capability> getCapability(Long id) {

        String sql = "select * from capabilities where capability_id = ?";

        try {
            Capability capability = jdbc.queryForObject(sql, capabilityMapper, id);

            return Optional.ofNullable(capability);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    public List<Resource> getResourcesForACapability(Long id) {
        String sql = "select * from resources where capability_id = ?";
        return jdbc.query(sql, resourceMapper, id);
    }

    public List<Skill> getSkillsForACapability(Long id) {
        String sql = "select * from skills where skill_id in (" +
                "select skill_id from capability_skills where capability_id = ?)";
        return jdbc.query(sql, skillMapper, id);
    }

    //OUTCOMES LOGIC

    public List<Outcome> findAllOutcomes() {
        String sql = "select * from outcomes where disabled = false";
        return jdbc.query(sql, new CapabilityRepositoryImpl.OutcomeRowMapper());
    }

    public Outcome findOutcomeById(Long id) {
        String sql = "SELECT * FROM outcomes WHERE outcome_id = ?";
        return jdbc.queryForObject(sql, new CapabilityRepositoryImpl.OutcomeRowMapper(), id);
    }

    private static class OutcomeRowMapper implements RowMapper<Outcome> {
        @Override
        public Outcome mapRow(ResultSet rs, int rowNum) throws SQLException {
            Outcome outcome = new Outcome();
            outcome.setId(rs.getLong("outcome_id"));
            outcome.setTitle(rs.getString("title"));
            return outcome;
        }
    }

    public List<Capability> findAllCapabilitiesForAnOutcome(Long outcomeId) {
        String sql = "select * from capabilities where outcome_id = ?";

        return jdbc.query(sql, capabilityMapper, outcomeId);
    }

    // ADMIN OUTCOMES LOGIC

    public List<AdminOutcome> findAllOutcomesWithNumberOfCapabilities() {

        String sql = """
                select o.outcome_id as id, o.title as title, count(c.capability_id) as capability_count
                from outcomes o
                left join capabilities c on c.outcome_id = o.outcome_id
                where o.disabled = false
                group by o.outcome_id
                order by o.title""";

        return jdbc.query(sql, adminOutcomeMapper);
    }

    public void CreateOutcome(String title) {
        String sql = "insert into outcomes (title) values (?)";

        jdbc.update(sql, title);
    }

    public void deleteOutcome(Long id) {
        String sql = "update outcomes set disabled = true where outcome_id = ?";

        jdbc.update(sql, id.intValue());
    }
    private RowMapper<AdminCapability> adminCapabilityMapper = (rs, rowNum) ->
            new AdminCapability(
                    rs.getLong("capability_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getLong("outcome_id"),
                    rs.getString("outcome_title")
            );
    @Override
    public List<AdminCapability> findCapabilitiesForOutcome(Long outcomeId) {

        String sql = """
        SELECT c.capability_id,
               c.title,
               c.description,
               c.outcome_id,
               o.title AS outcome_title
        FROM capabilities c
        JOIN outcomes o ON o.outcome_id = c.outcome_id
        WHERE c.outcome_id = ?
        ORDER BY c.capability_id
    """;

        return jdbc.query(sql, adminCapabilityMapper, outcomeId);
    }
    @Override
    public void createCapability(Long outcomeId, String title, String description) {
        jdbc.update(
                "INSERT INTO capabilities (title, description, outcome_id) VALUES (?, ?, ?)",
                title, description, outcomeId
        );
    }
    @Override
    public void deleteCapability(Long capabilityId) {

        jdbc.update("DELETE FROM capability_skills WHERE capability_id = ?", capabilityId);
        jdbc.update("DELETE FROM quiz_questions WHERE capability_id = ?", capabilityId);
        jdbc.update("DELETE FROM resources WHERE capability_id = ?", capabilityId);
        jdbc.update("DELETE FROM capabilities WHERE capability_id = ?", capabilityId);
    }
    @Override
    public AdminCapability findCapabilityById(Long capabilityId) {

        String sql = """
        SELECT c.capability_id,
               c.title,
               c.description,
               c.outcome_id,
               o.title AS outcome_title
        FROM capabilities c
        JOIN outcomes o ON o.outcome_id = c.outcome_id
        WHERE c.capability_id = ?
    """;

        return jdbc.queryForObject(sql, adminCapabilityMapper, capabilityId);
    }
    @Override
    public void updateCapability(Long capabilityId, String title, String description) {
        jdbc.update(
                "UPDATE capabilities SET title = ?, description = ? WHERE capability_id = ?",
                title, description, capabilityId
        );
    }

    @Override
    public AdminOutcome findAdminOutcomeById(Long id) {

        String sql = """
        SELECT o.outcome_id AS id,
               o.title,
               COUNT(c.capability_id) AS capability_count
        FROM outcomes o
        LEFT JOIN capabilities c ON c.outcome_id = o.outcome_id
        WHERE o.outcome_id = ?
        GROUP BY o.outcome_id, o.title
    """;

        return jdbc.queryForObject(sql, adminOutcomeMapper, id);
    }

    @Override
    public void updateOutcome(Long id, String title) {

        String sql = "UPDATE outcomes SET title = ? WHERE outcome_id = ?";

        jdbc.update(sql, title, id);
    }







    @Override
    public List<Capability> getAllCapabilitiesByOutcomeId(Long id) {
        return jdbc.query("SELECT * FROM capabilities WHERE outcome_id = ?", capabilityMapper, id);
    }

    @Override
    public Long getOutcomeIdByQuizId(int id) {
        return jdbc.queryForObject("SELECT outcome_id FROM quiz WHERE quiz_id = ?", Long.class, id);

    }
}
