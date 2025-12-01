package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

    public CapabilityRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
        setCapabilityRowMapper();
        setResourceRowMapper();
        setSkillRowMapper();
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
}
