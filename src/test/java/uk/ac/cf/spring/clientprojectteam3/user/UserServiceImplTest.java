package uk.ac.cf.spring.clientprojectteam3.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserJdbcRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void password_is_encoded_on_register() {
        User user = new User();
        user.setPassword("plain");

        when(passwordEncoder.encode("plain")).thenReturn("encoded");

        service.registerUser(user);

        assertThat(user.getPassword()).isEqualTo("encoded");
        verify(userRepository).save(user);
    }

    @Test
    void findByEmail_returns_user_when_present() {
        User user = new User();
        user.setEmail("test@email.com");

        when(userRepository.findByEmail("test@email.com"))
                .thenReturn(java.util.Optional.of(user));

        User result = service.findByEmail("test@email.com");

        assertThat(result).isSameAs(user);
    }

    @Test
    void findByEmail_returns_null_when_not_found() {
        when(userRepository.findByEmail("missing@email.com"))
                .thenReturn(java.util.Optional.empty());

        User result = service.findByEmail("missing@email.com");

        assertThat(result).isNull();
    }

    @Test
    void checkPassword_returns_true_when_encoder_matches() {
        User user = new User();
        user.setPassword("encoded");

        when(passwordEncoder.matches("raw", "encoded")).thenReturn(true);

        boolean matches = service.checkPassword(user, "raw");

        assertThat(matches).isTrue();
    }

    @Test
    void checkPassword_returns_false_when_encoder_does_not_match() {
        User user = new User();
        user.setPassword("encoded");

        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        boolean matches = service.checkPassword(user, "wrong");

        assertThat(matches).isFalse();
    }

    @Test
    void updatePassword_encodes_and_updates_user() {
        User user = new User();
        user.setUserid(1);
        user.setPassword("old");

        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNew");

        service.updatePassword(1, "newPass");

        assertThat(user.getPassword()).isEqualTo("encodedNew");
        verify(userRepository).update(user);
    }

    @Test
    void updateEmail_updates_user_email() {
        User user = new User();
        user.setUserid(1);
        user.setEmail("old@email.com");

        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));

        service.updateEmail(1, "new@email.com");

        assertThat(user.getEmail()).isEqualTo("new@email.com");
        verify(userRepository).update(user);
    }

    @Test
    void updateName_updates_user_name() {
        User user = new User();
        user.setUserid(1);
        user.setName("Old Name");

        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));

        service.updateName(1, "New Name");

        assertThat(user.getName()).isEqualTo("New Name");
        verify(userRepository).update(user);
    }

    @Test
    void updatePhone_updates_user_phone() {
        User user = new User();
        user.setUserid(1);
        user.setPhone("0000000000");

        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));

        service.updatePhone(1, "07123456789");

        assertThat(user.getPhone()).isEqualTo("07123456789");
        verify(userRepository).update(user);
    }

    @Test
    void getCurrentUserId_returns_null_when_no_authentication() {
        SecurityContextHolder.clearContext(); // no auth set

        Integer result = service.getCurrentUserId();

        assertThat(result).isNull();
        verifyNoInteractions(userRepository);
    }

    @Test
    void getCurrentUserId_returns_null_when_not_authenticated() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        Integer result = service.getCurrentUserId();

        assertThat(result).isNull();
        verifyNoInteractions(userRepository);
    }

    @Test
    void getCurrentUserId_returns_id_for_UserDetails_principal() {
        UserDetails principal = org.springframework.security.core.userdetails.User
                .withUsername("user@email.com")
                .password("ignored")
                .roles("USER")
                .build();

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(principal);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        when(userRepository.findUserIdByEmail("user@email.com"))
                .thenReturn(42);

        Integer result = service.getCurrentUserId();

        assertThat(result).isEqualTo(42);
    }

    @Test
    void getCurrentUserId_returns_id_for_string_principal() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("user@email.com");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        when(userRepository.findUserIdByEmail("user@email.com"))
                .thenReturn(7);

        Integer result = service.getCurrentUserId();

        assertThat(result).isEqualTo(7);
    }

    @Test
    void getCurrentUserId_returns_null_for_anonymousUser_string() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("anonymousUser");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        Integer result = service.getCurrentUserId();

        assertThat(result).isNull();
        verifyNoInteractions(userRepository);
    }
}
