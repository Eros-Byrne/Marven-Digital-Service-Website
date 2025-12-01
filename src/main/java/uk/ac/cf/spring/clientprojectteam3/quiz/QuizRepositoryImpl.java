package uk.ac.cf.spring.clientprojectteam3.quiz;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.function.Predicate;

@Repository
public class QuizRepositoryImpl implements QuizRepository {
    JdbcTemplate jdbcTemplate;
    RowMapper<Quiz> quizRowMapper;
    RowMapper<Question> questionRowMapper;
    RowMapper<Answers> answerRowMapper;
    RowMapper<AttemptDTO> attemptRowMapper;

    public QuizRepositoryImpl(JdbcTemplate aJdbc) {
        this.jdbcTemplate = aJdbc;

        setRowMappers();
    }

    public void setRowMappers() {
        quizRowMapper = (rs, i) -> new Quiz(
                rs.getLong("quiz_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("time_estimate")
        );
        questionRowMapper = (rs, i) -> new Question(
                rs.getLong("question_id"),
                rs.getLong("quiz_id"),
                rs.getString("text"),
                rs.getLong("capability_id")
        );
        answerRowMapper = (rs, i) -> {
            Gson gson = new Gson();
            Type empMapType = new TypeToken<Map<Long, Integer>>() {}.getType();
            Map<Long, Integer> scoresMap = gson.fromJson(rs.getString("answer_json"), empMapType);
            return new Answers(
                    rs.getLong("quiz_id"),
                    rs.getLong("user_id"),
                    new HashMap<>(scoresMap),
                    rs.getInt("attempt_number")
            );
        };
    }


    @Override
    public List<Quiz> getQuizNames() {
        return jdbcTemplate.query("select * from quiz", quizRowMapper);
    }

    @Override
    public List<Question> getQuestions(long quizId) {
        return jdbcTemplate.query("select * from quiz_questions where quiz_id = ?", questionRowMapper, quizId);
    }


    @Override
    public Quiz getQuiz(long quizId) {
        return jdbcTemplate.queryForObject("SELECT quiz_id, name, description, time_estimate FROM quiz WHERE quiz_id=?", quizRowMapper, quizId);
    }

    public void saveAnswer(long attemptId, long questionId, Integer score) {
        System.out.println("SAVING ANSWER: " + attemptId + " " + questionId + " " + score);
        String sql = """
        INSERT INTO answer (question_id, user_attempt_id, score)
        VALUES (?, ?, ?)
        ON DUPLICATE KEY UPDATE score = VALUES(score)
    """;

        jdbcTemplate.update(sql, questionId, attemptId, score);
    }

    public void markAttemptComplete(long userAttemptId) {
        String sql = """
            UPDATE user_attempt
            SET complete = 1
            WHERE user_attempt_id = ?
        """;

        jdbcTemplate.update(sql, userAttemptId);
    }


    public long createUserAttempt(long userId, int attemptNumber) {
    System.out.println("CREATING USER ATTEMPT: " + attemptNumber);
        String sql = """
            INSERT INTO user_attempt (user_id, attempt, complete)
            VALUES (?, ?, 0)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );
            ps.setObject(1, userId, java.sql.Types.BIGINT);
            ps.setInt(2, attemptNumber);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to generate user_attempt_id");
        }
        return key.longValue();
    }

    public void markAttemptIncomplete(int userAttemptId) {
        String sql = """
            UPDATE user_attempt
            SET complete = 0
            WHERE user_attempt_id = ?
        """;

        jdbcTemplate.update(sql, userAttemptId);
    }
}
