package uk.ac.cf.spring.clientprojectteam3.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserJdbcRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public void updatePassword(Integer userId, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.update(user);
    }

    @Override
    public void updateEmail(Integer userId, String newEmail) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setEmail(newEmail);
        userRepository.update(user);
    }

    @Override
    public void updateName(Integer userId, String newName) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setName(newName);
        userRepository.update(user);
    }

    @Override
    public void updatePhone(Integer userId, String newPhone) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPhone(newPhone);
        userRepository.update(user);
    }

    @Override
    public Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        // AI Generated code - START

        String email = null;
        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof String s && !"anonymousUser".equals(s)) {
            email = s;
        }

        return (email != null) ? userRepository.findUserIdByEmail(email) : null;

        // AI Generated code - END
    }
}
