package uk.ac.cf.spring.clientprojectteam3.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(User user) {
        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public void updatePassword(Integer userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updateEmail(Integer userId, String newEmail) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public void updateName(Integer userId, String newName) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setName(newName);
        userRepository.save(user);
    }
    @Override
    public void updatePhone(Integer userId, String newPhone) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPhone(newPhone);
        userRepository.save(user);
    }
}

