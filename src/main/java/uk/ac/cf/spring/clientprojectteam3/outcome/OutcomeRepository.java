
package uk.ac.cf.spring.clientprojectteam3.outcome;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OutcomeRepository {

    private final JdbcTemplate jdbcTemplate;

    public OutcomeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Outcome> findAll() {
        String sql = "SELECT * FROM outcomes";
        return jdbcTemplate.query(sql, new OutcomeRowMapper());
    }

    public Outcome findById(Long id) {
        String sql = "SELECT * FROM outcomes WHERE outcome_id = ?";
        return jdbcTemplate.queryForObject(sql, new OutcomeRowMapper(), id);
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
}