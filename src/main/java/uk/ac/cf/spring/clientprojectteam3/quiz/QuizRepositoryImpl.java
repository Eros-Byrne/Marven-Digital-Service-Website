package uk.ac.cf.spring.clientprojectteam3.quiz;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
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
                rs.getString("title"),
                rs.getString("text"),
                rs.getLong("skill_id")
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
    public List<Answers> getAnswers(long quizId, long userId) {
        List<Answers> answers = jdbcTemplate.query(
                "select * from user_answers where quiz_id = ? and user_id = ?"
                , answerRowMapper
                , quizId, userId);
        return answers;
    }

    @Override
    public AttemptDTO getAttempt(long quizId, long userId, int attemptNumber) {
        Quiz quiz = jdbcTemplate.query(
                "select * from quiz where quiz.quiz_id = ?"
                , quizRowMapper
                , quizId).getFirst();//populate questions in quiz object
        List<Question> questions = jdbcTemplate.query(
                "select * from quiz_questions where quiz_id = ?"
                , questionRowMapper
                , quizId);
        quiz.setQuestions(questions);
        List<Answers> answers = jdbcTemplate.query(
                "select * from user_answers where quiz_id = ? and user_id = ?",
                answerRowMapper,
                quizId, userId
        );

        HashMap<Question, Integer> questionAnswerPairs = new HashMap<>();
        for(Answers answer : answers) {
            for(Map.Entry<Long, Integer> questionScorePair : (answer.getAnswers().entrySet())){
                Question question = quiz.getQuestions().stream().filter(questionSearch -> questionSearch.getQuestionId() == questionScorePair.getKey()).findFirst().orElse(null);
                questionAnswerPairs.put(question, questionScorePair.getValue());
            }
        }

        return new AttemptDTO(quiz, userId, questionAnswerPairs, attemptNumber);
    }

    @Override
    public void addAnswer(long quizId, long userId, int attemptNumber, long questionId, int quizScore) {
        List<Answers> answers = jdbcTemplate.query(
                "select * from user_answers where quiz_id = ? and user_id = ? and attempt_number = ?"
                , answerRowMapper
                , quizId, userId, attemptNumber);
        if(answers.isEmpty()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(Long.toString(questionId), Integer.toString(quizScore));

            jdbcTemplate.update("INSERT INTO user_answers (quiz_id, user_id, attempt_number, answer_json) VALUES (?, ?, ?, ?)", quizId, userId, attemptNumber, jsonObject.toString());
        }else{
            JsonObject jsonObject = new JsonObject();
            answers.getFirst().getAnswers().entrySet().stream().forEach(entry -> {
                jsonObject.addProperty(Long.toString(entry.getKey()), Integer.toString(entry.getValue()));
            });
            jsonObject.addProperty(Long.toString(questionId), Integer.toString(quizScore));
            Answers answer = answers.getFirst();
            jdbcTemplate.update("UPDATE user_answers SET answer_json = ? WHERE quiz_id = ? and user_id = ? and attempt_number = ?", jsonObject.toString(), quizId, userId, attemptNumber);
        }
    }

    @Override
    public Quiz getQuiz(long quizId) {
        return jdbcTemplate.queryForObject("SELECT quiz_id, name, description, time_estimate FROM quiz WHERE quiz_id=?", quizRowMapper, quizId);
    }
}
