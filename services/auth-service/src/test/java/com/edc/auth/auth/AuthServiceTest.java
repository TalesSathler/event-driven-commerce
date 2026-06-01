package com.edc.auth.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.edc.auth.event.UserEventPublisher;
import com.edc.auth.jwt.JwtUtil;
import com.edc.auth.token.RefreshToken;
import com.edc.auth.token.RefreshTokenRepository;
import com.edc.auth.user.User;
import com.edc.auth.user.UserRepository;
import java.lang.reflect.Field;
import java.security.KeyPairGenerator;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private RefreshTokenRepository refreshTokenRepository;
  @Mock private UserEventPublisher eventPublisher;

  private AuthService authService;
  private JwtUtil jwtUtil;
  private BCryptPasswordEncoder encoder;

  @BeforeEach
  void setUp() throws Exception {
    var pair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    jwtUtil = new JwtUtil(pair.getPrivate(), pair.getPublic(), 86400000L, "auth-service");
    encoder = new BCryptPasswordEncoder();
    authService = new AuthService(userRepository, refreshTokenRepository,
        encoder, jwtUtil, eventPublisher, 604800000L);
  }

  private User createUserWithId(String name, String email, String rawPassword, String role) throws Exception {
    var user = new User(name, email, encoder.encode(rawPassword), role);
    var idField = User.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(user, UUID.randomUUID());
    return user;
  }

  @Test
  void shouldRegisterUser() throws Exception {
    var request = new RegisterRequest("John Doe", "john@example.com", "password123");
    when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
    when(userRepository.save(any())).thenAnswer(i -> {
      var u = i.<User>getArgument(0);
      var saved = new User(u.getName(), u.getEmail(), u.getPassword(), "USER");
      var idField = User.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(saved, UUID.randomUUID());
      return saved;
    });
    when(refreshTokenRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    var response = authService.register(request);

    assertThat(response.name()).isEqualTo("John Doe");
    assertThat(response.email()).isEqualTo("john@example.com");
    assertThat(response.token()).isNotBlank();
    assertThat(response.refreshToken()).isNotBlank();
  }

  @Test
  void shouldThrowOnDuplicateEmail() {
    var request = new RegisterRequest("John", "john@example.com", "password123");
    when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

    assertThatThrownBy(() -> authService.register(request))
        .isInstanceOf(EmailAlreadyExistsException.class);
  }

  @Test
  void shouldLoginWithValidCredentials() throws Exception {
    var request = new LoginRequest("john@example.com", "password123");
    var user = createUserWithId("John Doe", "john@example.com", "password123", "USER");
    when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
    when(refreshTokenRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    var response = authService.login(request);

    assertThat(response.token()).isNotBlank();
    assertThat(response.name()).isEqualTo("John Doe");
  }

  @Test
  void shouldThrowOnInvalidPassword() throws Exception {
    var request = new LoginRequest("john@example.com", "wrongpassword");
    var user = createUserWithId("John Doe", "john@example.com", "password123", "USER");
    when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

    assertThatThrownBy(() -> authService.login(request))
        .isInstanceOf(InvalidCredentialsException.class);
  }

  @Test
  void shouldRefreshToken() throws Exception {
    var userId = UUID.randomUUID();
    var user = createUserWithId("John Doe", "john@example.com", "hash", "USER");
    var refreshToken = new RefreshToken(userId, "refresh-token-value", LocalDateTime.now().plusDays(7));
    when(refreshTokenRepository.findByToken("refresh-token-value")).thenReturn(Optional.of(refreshToken));
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    var response = authService.refresh(new RefreshRequest("refresh-token-value"));

    assertThat(response.token()).isNotBlank();
  }

  @Test
  void shouldThrowOnExpiredRefreshToken() {
    var refreshToken = new RefreshToken(UUID.randomUUID(), "expired-token", LocalDateTime.now().minusDays(1));
    when(refreshTokenRepository.findByToken("expired-token")).thenReturn(Optional.of(refreshToken));

    assertThatThrownBy(() -> authService.refresh(new RefreshRequest("expired-token")))
        .isInstanceOf(InvalidCredentialsException.class);
  }
}
