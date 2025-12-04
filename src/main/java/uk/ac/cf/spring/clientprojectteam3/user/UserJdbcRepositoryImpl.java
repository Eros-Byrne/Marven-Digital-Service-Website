package uk.ac.cf.spring.clientprojectteam3.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepositoryImpl implements UserJdbcRepository {

    private final JdbcTemplate jdbc;

    public UserJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }


    public Integer findUserIdByEmail(String email) {
        String sql = "select user_id from users where email = ?";

        return jdbc.queryForObject(sql, Integer.class, email);
    }
}
