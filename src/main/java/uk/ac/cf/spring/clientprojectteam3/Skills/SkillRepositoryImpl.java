package uk.ac.cf.spring.clientprojectteam3.Skills;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SkillRepositoryImpl implements SkillRepository {

    private JdbcTemplate jdbc;
    private RowMapper<Skill> skillRowMapper;

    public SkillRepositoryImpl(JdbcTemplate ajdbc) {
        this.jdbc = ajdbc;
        setSkillRowMapper();
    }

    private void setSkillRowMapper() {
        skillRowMapper = (rs, i) -> new Skill(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

    public List<Skill> getSkills() {
        String sql = "select * from skills";
        return jdbc.query(sql, skillRowMapper);
    }
}
