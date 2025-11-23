package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import uk.ac.cf.spring.clientprojectteam3.Skills.Skill;

import java.util.List;

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

    public Capability getCapability(Long id) {
        String sql = "select * from capabilities where capability_id = ?";
        return jdbc.queryForObject(sql, capabilityMapper, id);
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
}
