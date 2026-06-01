package com.edc.auth.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(nullable = false, unique = true, length = 512)
  private String token;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  public RefreshToken() {}

  public RefreshToken(UUID userId, String token, LocalDateTime expiresAt) {
    this.userId = userId;
    this.token = token;
    this.expiresAt = expiresAt;
  }

  @PrePersist
  void onCreate() {
    createdAt = LocalDateTime.now();
  }

  public UUID getId() { return id; }
  public UUID getUserId() { return userId; }
  public String getToken() { return token; }
  public LocalDateTime getExpiresAt() { return expiresAt; }
  public LocalDateTime getCreatedAt() { return createdAt; }

  public void setToken(String token) { this.token = token; }
  public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}
