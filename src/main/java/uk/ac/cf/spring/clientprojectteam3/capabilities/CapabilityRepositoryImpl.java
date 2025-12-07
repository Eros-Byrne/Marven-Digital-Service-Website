package uk.ac.cf.spring.clientprojectteam3.capabilities;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
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
        String sql = "SELECT * FROM outcomes";
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
                group by o.outcome_id
                order by o.title""";

        return jdbc.query(sql, adminOutcomeMapper);
    }
}
