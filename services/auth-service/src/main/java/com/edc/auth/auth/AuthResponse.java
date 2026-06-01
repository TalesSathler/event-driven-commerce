package com.edc.auth.auth;

public record AuthResponse(
    String token,
    String refreshToken,
    String name,
    String email) {
}
