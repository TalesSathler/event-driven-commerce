package com.edc.inventory.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductEvent(
    UUID eventId,
    UUID productId,
    String name,
    BigDecimal price,
    Integer quantity,
    LocalDateTime timestamp
) {}
