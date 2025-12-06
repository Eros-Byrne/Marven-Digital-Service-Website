package uk.ac.cf.spring.clientprojectteam3.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


@Repository
public class UserJdbcRepositoryImpl implements UserJdbcRepository {

    private final JdbcTemplate jdbc;

    public UserJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }
    private static final RowMapper<User> USER_ROW_MAPPER =
            (rs, rowNum) -> new User(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password")
            );

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbc.query(
                "SELECT * FROM users WHERE email = ?",
                USER_ROW_MAPPER,
                email
        ).stream().findFirst();
    }

    @Override
    public Optional<User> findById(Integer userId) {
        return jdbc.query(
                "SELECT * FROM users WHERE user_id = ?",
                USER_ROW_MAPPER,
                userId
        ).stream().findFirst();
    }


    @Override
    public Integer findUserIdByEmail(String email) {
        String sql = "select user_id from users where email = ?";

        return jdbc.queryForObject(sql, Integer.class, email);
    }

    @Override
    public void save(User user) {
        jdbc.update(
                "INSERT INTO users (name, email, phone, password) VALUES (?,?,?,?)",
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword()
        );
    }

    @Override
    public void update(User user) {
        jdbc.update(
                """
                UPDATE users
                   SET name=?, email=?, phone=?, password=?
                 WHERE user_id=?
                """,
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                user.getUserid()
        );
    }
}
