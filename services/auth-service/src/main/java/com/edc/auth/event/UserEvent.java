package com.edc.auth.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserEvent(
    UUID eventId,
    String eventType,
    UUID userId,
    String email,
    String role,
    LocalDateTime timestamp
) implements Serializable {}
