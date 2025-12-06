package uk.ac.cf.spring.clientprojectteam3.user;
import java.util.Optional;

public interface UserJdbcRepository {

    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer userId);

    Integer findUserIdByEmail(String email);
    void save(User user);
    void update(User user);
}
