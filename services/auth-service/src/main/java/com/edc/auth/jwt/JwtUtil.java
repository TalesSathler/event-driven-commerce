package com.edc.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private final PrivateKey privateKey;
  private final PublicKey publicKey;
  private final long accessExpirationMs;
  private final String issuer;

  @Autowired
  public JwtUtil(
      RsaKeyConfig rsaKeyConfig,
      @Value("${app.jwt.access-expiration-ms:86400000}") long accessExpirationMs,
      @Value("${app.jwt.issuer:auth-service}") String issuer) {
    this.privateKey = rsaKeyConfig.getPrivateKey();
    this.publicKey = rsaKeyConfig.getPublicKey();
    this.accessExpirationMs = accessExpirationMs;
    this.issuer = issuer;
  }

  public JwtUtil(PrivateKey privateKey, PublicKey publicKey, long accessExpirationMs, String issuer) {
    this.privateKey = privateKey;
    this.publicKey = publicKey;
    this.accessExpirationMs = accessExpirationMs;
    this.issuer = issuer;
  }

  public String generateToken(UUID userId, String email, List<String> roles) {
    var now = Instant.now();
    return Jwts.builder()
        .subject(userId.toString())
        .issuer(issuer)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plus(accessExpirationMs, ChronoUnit.MILLIS)))
        .claim("email", email)
        .claim("roles", roles)
        .signWith(privateKey, Jwts.SIG.RS256)
        .compact();
  }

  public Jws<Claims> validateToken(String token) {
    return Jwts.parser()
        .requireIssuer(issuer)
        .verifyWith(publicKey)
        .build()
        .parseSignedClaims(token);
  }

  public boolean isValidToken(String token) {
    try {
      validateToken(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public PublicKey getPublicKey() {
    return publicKey;
  }
}
