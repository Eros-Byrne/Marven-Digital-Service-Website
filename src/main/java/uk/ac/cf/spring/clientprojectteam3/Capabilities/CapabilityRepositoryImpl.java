package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CapabilityRepositoryImpl implements CapabilityRepository {

    private JdbcTemplate jdbc;
    private RowMapper<Capability> capabilityMapper;

    public CapabilityRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
        setCapabilityRowMapper();

    }

    private void setCapabilityRowMapper() {
        capabilityMapper = (rs, i) -> new Capability(
                rs.getLong("capability_id"),
                rs.getString("title"),
                rs.getString("description")
        );
    }

    public Capability getCapability(long id) {
        String sql = "select * from capabilities where capability_id = ?";
        return jdbc.queryForObject(sql, capabilityMapper, id);
    }


}
