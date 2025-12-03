package uk.ac.cf.spring.clientprojectteam3.user;

public interface UserJdbcRepository {

    Integer findUserIdByEmail(String email);
}
