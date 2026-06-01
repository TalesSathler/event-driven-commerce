package com.edc.product.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    int quantity,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

  public static ProductResponse from(Product product) {
    return new ProductResponse(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getQuantity(),
        product.getCreatedAt(),
        product.getUpdatedAt()
    );
  }
}
