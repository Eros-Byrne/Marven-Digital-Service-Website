package uk.ac.cf.spring.clientprojectteam3.admin.capability;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminCapabilityRepositoryImpl implements AdminCapabilityRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<AdminCapability> rowMapper = (rs, rowNum) ->
            new AdminCapability(
                    rs.getLong("capability_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getLong("outcome_id"),
                    rs.getString("outcome_title")
            );

    @Override
    public List<AdminCapability> findByOutcomeId(Long outcomeId) {
        return jdbcTemplate.query("""
            SELECT c.*, o.title AS outcome_title
            FROM capabilities c
            JOIN outcomes o ON o.outcome_id = c.outcome_id
            WHERE c.outcome_id = ?
            ORDER BY c.capability_id
        """, rowMapper, outcomeId);
    }

    @Override
    public Optional<AdminCapability> findById(Long capabilityId) {
        return jdbcTemplate.query("""
            SELECT c.*, o.title AS outcome_title
            FROM capabilities c
            JOIN outcomes o ON o.outcome_id = c.outcome_id
            WHERE c.capability_id = ?
        """, rowMapper, capabilityId).stream().findFirst();
    }

    @Override
    public void create(Long outcomeId, String title, String description) {
        jdbcTemplate.update("""
            INSERT INTO capabilities (title, description, outcome_id)
            VALUES (?, ?, ?)
        """, title, description, outcomeId);
    }

    @Override
    public void update(Long capabilityId, String title, String description) {
        jdbcTemplate.update("""
            UPDATE capabilities
            SET title = ?, description = ?
            WHERE capability_id = ?
        """, title, description, capabilityId);
    }

    @Override
    public void delete(Long capabilityId) {

        jdbcTemplate.update("DELETE FROM capability_skills WHERE capability_id = ?", capabilityId);
        jdbcTemplate.update("DELETE FROM quiz_questions WHERE capability_id = ?", capabilityId);
        jdbcTemplate.update("DELETE FROM resources WHERE capability_id = ?", capabilityId);
        jdbcTemplate.update("DELETE FROM capabilities WHERE capability_id = ?", capabilityId);
    }
}
