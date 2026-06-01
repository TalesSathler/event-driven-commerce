package com.edc.product.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductRequest(
    @NotBlank String name,
    String description,
    @NotNull @Positive BigDecimal price,
    @NotNull @Min(0) Integer quantity
) {}
