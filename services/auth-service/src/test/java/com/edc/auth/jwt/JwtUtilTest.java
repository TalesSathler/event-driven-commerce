package com.edc.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.KeyPairGenerator;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

  private JwtUtil jwtUtil;

  @BeforeEach
  void setUp() throws Exception {
    var generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    var pair = generator.generateKeyPair();
    jwtUtil = new JwtUtil(pair.getPrivate(), pair.getPublic(), 86400000L, "auth-service");
  }

  @Test
  void shouldGenerateAndValidateToken() {
    var userId = UUID.randomUUID();
    var email = "john@example.com";
    var roles = List.of("USER");

    var token = jwtUtil.generateToken(userId, email, roles);
    var claims = jwtUtil.validateToken(token);

    assertThat(claims.getPayload().getSubject()).isEqualTo(userId.toString());
    assertThat(claims.getPayload().get("email", String.class)).isEqualTo(email);
    assertThat(claims.getPayload().get("roles", List.class)).containsExactly("USER");
    assertThat(claims.getPayload().getIssuer()).isEqualTo("auth-service");
  }

  @Test
  void shouldRejectExpiredToken() throws Exception {
    var pair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    var expiredUtil = new JwtUtil(pair.getPrivate(), pair.getPublic(), -1000L, "auth-service");
    var token = expiredUtil.generateToken(UUID.randomUUID(), "test@test.com", List.of("USER"));

    assertThat(expiredUtil.isValidToken(token)).isFalse();
  }

  @Test
  void shouldRejectInvalidSignature() {
    var token = jwtUtil.generateToken(UUID.randomUUID(), "test@test.com", List.of("USER"));
    var parts = token.split("\\.");
    var tampered = parts[0] + "." + parts[1] + ".invalidsignature";

    assertThat(jwtUtil.isValidToken(tampered)).isFalse();
  }
}
