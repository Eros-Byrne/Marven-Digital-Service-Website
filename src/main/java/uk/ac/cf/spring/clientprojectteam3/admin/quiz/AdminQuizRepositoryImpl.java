package uk.ac.cf.spring.clientprojectteam3.admin.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import uk.ac.cf.spring.clientprojectteam3.quiz.Quiz;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class AdminQuizRepositoryImpl implements AdminQuizRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private RowMapper<Quiz> quizRowMapper;

    public AdminQuizRepositoryImpl() {
        setRowMappers();
    }

    public void setRowMappers(){
        quizRowMapper = (rs, i) -> new Quiz(
                rs.getLong("quiz_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("time_estimate")
        );
    }

    @Override
    public List<Quiz> findAllQuizzes() {
        return jdbcTemplate.query("select * from quiz", quizRowMapper);
    }

    @Override
    public String getCapabilityById(long capabilityId) {
        return jdbcTemplate.queryForObject("select title from capabilities where capability_id=?", String.class, capabilityId);
    }

    @Override
    public String getCapabilityColourById(long capabilityId) {
        return jdbcTemplate.queryForObject("select colour from capabilities where capability_id=?", String.class, capabilityId);
    }

    @Override
    public void deleteQuestion(int questionId) {
        String sql = "UPDATE quiz_questions SET disabled = 1 WHERE question_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, questionId);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Question marked as disabled successfully.");
            } else {
                System.out.println("No question found with id: " + questionId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long createQuiz(String title, Long outcomeId, String description, int timeEstimate) {
        String sql = "INSERT INTO quiz (name, description, time_estimate, outcome_id) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setInt(3, timeEstimate);
            ps.setLong(4, outcomeId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }


    @Override
    public void createQuestion(int quizId, int capabilityId, String text) {
        String sql = """
        INSERT INTO quiz_questions (quiz_id, capability_id, text)
        VALUES (?, ?, ?)
    """;

        jdbcTemplate.update(sql, quizId, Long.parseLong(String.valueOf(capabilityId)), text);
    }
}
