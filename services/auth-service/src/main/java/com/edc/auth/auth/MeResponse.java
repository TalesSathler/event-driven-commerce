package com.edc.auth.auth;

public record MeResponse(
    String id,
    String name,
    String email,
    String role
) {}
