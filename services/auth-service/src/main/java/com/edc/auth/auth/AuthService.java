package com.edc.auth.auth;

import com.edc.auth.event.UserEventPublisher;
import com.edc.auth.jwt.JwtUtil;
import com.edc.auth.token.RefreshToken;
import com.edc.auth.token.RefreshTokenRepository;
import com.edc.auth.user.User;
import com.edc.auth.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final UserEventPublisher eventPublisher;
  private final long refreshExpirationMs;

  public AuthService(
      UserRepository userRepository,
      RefreshTokenRepository refreshTokenRepository,
      PasswordEncoder passwordEncoder,
      JwtUtil jwtUtil,
      UserEventPublisher eventPublisher,
      @Value("${app.jwt.refresh-expiration-ms:604800000}") long refreshExpirationMs) {
    this.userRepository = userRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
    this.eventPublisher = eventPublisher;
    this.refreshExpirationMs = refreshExpirationMs;
  }

  @Transactional
  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EmailAlreadyExistsException("Email already registered");
    }

    var user = new User(
        request.name(),
        request.email(),
        passwordEncoder.encode(request.password()),
        "USER");
    try {
      user = userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new EmailAlreadyExistsException("Email already registered");
    }

    var token = jwtUtil.generateToken(user.getId(), user.getEmail(), List.of(user.getRole()));
    var refreshToken = createRefreshToken(user.getId());

    eventPublisher.publishRegistered(user);

    return new AuthResponse(token, refreshToken, user.getName(), user.getEmail());
  }

  public AuthResponse login(LoginRequest request) {
    var user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new InvalidCredentialsException("Invalid email or password");
    }

    var token = jwtUtil.generateToken(user.getId(), user.getEmail(), List.of(user.getRole()));
    var refreshToken = createRefreshToken(user.getId());

    eventPublisher.publishLogin(user);

    return new AuthResponse(token, refreshToken, user.getName(), user.getEmail());
  }

  @Transactional
  public TokenResponse refresh(RefreshRequest request) {
    var stored = refreshTokenRepository.findByToken(request.refreshToken())
        .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

    if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
      refreshTokenRepository.delete(stored);
      throw new InvalidCredentialsException("Refresh token expired");
    }

    var user = userRepository.findById(stored.getUserId())
        .orElseThrow(() -> new InvalidCredentialsException("User not found"));

    refreshTokenRepository.delete(stored);

    var newToken = jwtUtil.generateToken(user.getId(), user.getEmail(), List.of(user.getRole()));
    return new TokenResponse(newToken);
  }

  @Transactional
  public void logout(String refreshTokenValue) {
    refreshTokenRepository.findByToken(refreshTokenValue).ifPresent(refreshTokenRepository::delete);
  }

  public MeResponse me(UUID userId) {
    var user = userRepository.findById(userId)
        .orElseThrow(() -> new InvalidCredentialsException("User not found"));
    return new MeResponse(
        user.getId().toString(),
        user.getName(),
        user.getEmail(),
        user.getRole()
    );
  }

  private String createRefreshToken(UUID userId) {
    var expiresAt = LocalDateTime.now().plusNanos(refreshExpirationMs * 1_000_000);
    var token = UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString();
    refreshTokenRepository.save(new RefreshToken(userId, token, expiresAt));
    return token;
  }
}
