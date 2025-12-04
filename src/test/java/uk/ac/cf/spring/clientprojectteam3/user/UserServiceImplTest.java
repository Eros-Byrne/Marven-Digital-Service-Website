package uk.ac.cf.spring.clientprojectteam3.user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void password_is_encoded_on_register() {
        User user = new User();
        user.setPassword("plain");

        when(passwordEncoder.encode("plain"))
                .thenReturn("encoded");

        service.registerUser(user);

        assertThat(user.getPassword()).isEqualTo("encoded");
        verify(userRepository).save(user);
    }
}
