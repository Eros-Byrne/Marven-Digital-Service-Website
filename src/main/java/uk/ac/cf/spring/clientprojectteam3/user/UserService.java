package uk.ac.cf.spring.clientprojectteam3.user;

public interface UserService {
    void registerUser(User user);
    User findByEmail(String email);
    void updatePassword(Integer userId, String newPassword);
    void updateEmail(Integer userId, String newEmail);
    void updateName(Integer userId, String newName);
    void updatePhone(Integer userId, String newPhone);
    boolean checkPassword(User user, String rawPassword);
    Integer getCurrentUserId();
}
