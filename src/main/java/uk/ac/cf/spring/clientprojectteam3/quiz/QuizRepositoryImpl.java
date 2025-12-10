package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QuizRepositoryImpl implements QuizRepository {
    JdbcTemplate jdbcTemplate;
    RowMapper<QuizCardDTO> quizBlankCardRowMapper;
    RowMapper<Question> questionRowMapper;
    RowMapper<QuizCardDTO> quizUserCardRowMapper;
    RowMapper<Quiz> quizRowMapper;


    public QuizRepositoryImpl(JdbcTemplate aJdbc) {
        this.jdbcTemplate = aJdbc;
        setRowMappers();
    }

    public void setRowMappers() {
        quizBlankCardRowMapper = (rs, i) -> new QuizCardDTO(
                rs.getLong("quiz_id"),
                rs.getString("quiz_name"),
                rs.getString("quiz_description"),
                rs.getInt("time_estimate"),
                0,    // attemptNumber
                0,    // isCompleted?
                0,    // questionsCompleted
                rs.getInt("total_questions")
        );
        questionRowMapper = (rs, i) -> {
            Question q = new Question();
            q.setQuestionId(rs.getLong("question_id"));
            q.setQuizId(rs.getLong("quiz_id"));
            q.setText(rs.getString("text"));
            q.setCapabilityId(rs.getLong("capability_id"));
            q.setDisabled(rs.getInt("disabled"));
            try { q.setCapabilityName(rs.getString("capability_name")); } catch (Exception ignored) {}
            try { q.setCapabilityColour(rs.getString("capability_colour")); } catch (Exception ignored) {}
            return q;
        };
        quizUserCardRowMapper = (rs, rowNum) -> new QuizCardDTO(
                rs.getLong("quiz_id"),
                rs.getString("quiz_name"),
                rs.getString("quiz_description"),
                rs.getInt("time_estimate"),
                rs.getInt("attempt_number"),
                rs.getInt("completed"),
                rs.getInt("answered_questions"),
                rs.getInt("total_questions")
        );
        quizRowMapper = (rs, i) -> new Quiz(
                rs.getLong("quiz_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("time_estimate")
        );
    }

    private final RowMapper<QuizAttemptScore> quizAttemptScoreRowMapper = (rs, rowNum) -> new QuizAttemptScore(
            rs.getLong("user_attempt_id"),
            rs.getInt("attempt"),
            rs.getDouble("average_score")
    );

    private final RowMapper<QuizButtonInfo> quizButtonInfoRowMapper = (rs, rowNum) -> new QuizButtonInfo(
            rs.getLong("quiz_id"),
            rs.getString("quiz_name"),
            rs.getBoolean("completed")
    );

    @Override
    public List<QuizCardDTO> getBlankQuizCards() {

        String sql = """
        SELECT
            q.quiz_id,
            q.name AS quiz_name,
            q.description AS quiz_description,
            q.time_estimate,
            (
                SELECT COUNT(*) 
                FROM quiz_questions qq
                WHERE qq.quiz_id = q.quiz_id
                AND qq.disabled = 0
            ) AS total_questions
        FROM quiz q
        ORDER BY q.quiz_id
        """;

        return jdbcTemplate.query(sql, quizBlankCardRowMapper);
    }

    @Override
    public List<Question> getQuestions(long quizId) {
        return jdbcTemplate.query("select * from quiz_questions where quiz_id = ? and disabled = 0", questionRowMapper, quizId);
    }

    @Override
    public Quiz getQuiz(long quizId) {
        return jdbcTemplate.queryForObject("SELECT quiz_id, name, description, time_estimate FROM quiz WHERE quiz_id=?", quizRowMapper, quizId);
    }

    @Override
    public void saveAnswer(long attemptId, long questionId, Integer score) {
        System.out.println("SAVING ANSWER: " + attemptId + " " + questionId + " " + score);
        String sql = """
        INSERT INTO answer (question_id, user_attempt_id, score)
        VALUES (?, ?, ?)
        ON DUPLICATE KEY UPDATE score = VALUES(score)
        """;

        jdbcTemplate.update(sql, questionId, attemptId, score);
    }

    @Override
    public void markAttemptComplete(long userAttemptId) {
        String sql = """
            UPDATE user_attempt
            SET complete = 1
            WHERE user_attempt_id = ?
        """;

        jdbcTemplate.update(sql, userAttemptId);
    }

    @Override
    public long createUserAttempt(long userId, long quizId) {
        int nextAttempt = getNextAttemptNumber(userId, quizId);
        return insertUserAttempt(userId, quizId, nextAttempt);
    }

    private int getNextAttemptNumber(long userId, long quizId) {
        Integer maxAttempt = jdbcTemplate.queryForObject(
                "SELECT MAX(attempt) FROM user_attempt WHERE user_id = ? AND quiz_id = ?",
                Integer.class,
                userId,
                quizId
        );
        return (maxAttempt == null) ? 1 : maxAttempt + 1;
    }

    private long insertUserAttempt(long userId, long quizId, int attemptNumber) {
        String sql = """
        INSERT INTO user_attempt (user_id, quiz_id, attempt, complete)
        VALUES (?, ?, ?, 0)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, userId, java.sql.Types.BIGINT);
            ps.setObject(2, quizId, java.sql.Types.BIGINT);
            ps.setInt(3, attemptNumber);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to generate user_attempt_id");
        }

        System.out.println("CREATING USER ATTEMPT: id=" + key + ", quiz=" + quizId + ", attempt=" + attemptNumber);
        return key.longValue();
    }

    @Override
    public void markAttemptIncomplete(int userAttemptId) {
        String sql = """
            UPDATE user_attempt
            SET complete = 0
            WHERE user_attempt_id = ?
        """;

        jdbcTemplate.update(sql, userAttemptId);
    }

    @Override
    public void deleteEmptyAttempt(int userAttemptId) {
        System.out.println("DELETING EMPTY ATTEMPT: " + userAttemptId);
        String sql = """
            DELETE FROM user_attempt
            WHERE user_attempt_id = ?
        """;

        jdbcTemplate.update(sql, userAttemptId);
    }

    public List<QuizCardDTO> getQuizCardsByUserId(long userId) {
        String sql = """
        SELECT 
            q.quiz_id,
            q.name AS quiz_name,
            q.description AS quiz_description,
            q.time_estimate,

            COALESCE(ua.attempt, 0) AS attempt_number,
            COALESCE(ua.complete, 0) AS completed,

            (
                SELECT COUNT(*)
                FROM quiz_questions qq
                WHERE qq.quiz_id = q.quiz_id
                AND qq.disabled = 0
            ) AS total_questions,

            (
                SELECT COUNT(*)
                FROM answer a
                WHERE a.user_attempt_id = ua.user_attempt_id
            ) AS answered_questions

        FROM quiz q
        LEFT JOIN user_attempt ua
            ON ua.quiz_id = q.quiz_id
            AND ua.user_id = ?
            AND ua.attempt = (
                SELECT MAX(ua2.attempt)
                FROM user_attempt ua2
                WHERE ua2.quiz_id = q.quiz_id
                  AND ua2.user_id = ?
            )
        ORDER BY q.quiz_id
        """;

        return jdbcTemplate.query(
                sql,
                new Object[]{userId, userId},
                quizUserCardRowMapper
        );
    }

    @Override
    public Integer getLatestAttemptId(long userId, int quizId) {
        String sql = """
        SELECT user_attempt_id
        FROM user_attempt
        WHERE user_id = ? AND quiz_id = ?
        ORDER BY attempt DESC
        LIMIT 1
    """;
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId, quizId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getAttemptStatus(int attemptId) {
        String sql = """
        SELECT complete
        FROM user_attempt
        WHERE user_attempt_id = ?
        """;

        return jdbcTemplate.queryForObject(sql, Integer.class, attemptId);
    }


    @Override
    public Map<Integer, Integer> getAttemptAnswers(long attemptId) {
        String sql = """
            SELECT question_id, score
            FROM answer
            WHERE user_attempt_id = ?
        """;

        Map<Integer, Integer> answers = new HashMap<>();
        jdbcTemplate.query(sql, rs -> {
            answers.put(rs.getInt("question_id"), rs.getInt("score"));
        }, attemptId);

        return answers;
    }

    @Override
    public int getAttemptNumber(long attemptId) {
        String sql = """
            SELECT attempt
            FROM user_attempt
            WHERE user_attempt_id = ?
        """;

        try {
            Integer attempt = jdbcTemplate.queryForObject(sql, Integer.class, attemptId);
            return attempt != null ? attempt : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Long findLatestCompletedAttempt(long userId, long quizId) {
        String sql = """
            SELECT user_attempt_id
            FROM user_attempt
            WHERE user_id = ? AND quiz_id = ? AND complete = 1
            ORDER BY attempt DESC
            LIMIT 1
        """;

        try {
            return jdbcTemplate.queryForObject(sql, Long.class, userId, quizId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<QuizAttemptScore> getAllCompletedAttempts(long userId, long quizId) {
        String sql = """
            SELECT
                ua.user_attempt_id,
                ua.attempt,
                COALESCE(AVG(a.score), 0) * 20 AS average_score
            FROM user_attempt ua
            LEFT JOIN answer a ON ua.user_attempt_id = a.user_attempt_id
            WHERE ua.user_id = ?
              AND ua.quiz_id = ?
              AND ua.complete = 1
            GROUP BY ua.user_attempt_id, ua.attempt
            ORDER BY ua.attempt ASC
        """;

        return jdbcTemplate.query(sql, quizAttemptScoreRowMapper, userId, quizId);
    }

    @Override
    public List<QuizButtonInfo> getQuizButtonsInfo(long userId) {
        String sql = """
            SELECT
                q.quiz_id,
                q.name AS quiz_name,
                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM user_attempt ua
                        WHERE ua.quiz_id = q.quiz_id
                          AND ua.user_id = ?
                          AND ua.complete = 1
                    ) THEN TRUE
                    ELSE FALSE
                END AS completed
            FROM quiz q
            ORDER BY q.quiz_id ASC
        """;

        return jdbcTemplate.query(sql, quizButtonInfoRowMapper, userId);
    }
}