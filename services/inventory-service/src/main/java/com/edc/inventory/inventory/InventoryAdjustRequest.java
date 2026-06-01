package com.edc.inventory.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryAdjustRequest(
    @NotNull @Min(0) Integer quantity
) {}
