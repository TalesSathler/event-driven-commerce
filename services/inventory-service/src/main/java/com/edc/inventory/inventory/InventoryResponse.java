package com.edc.inventory.inventory;

import java.util.UUID;

public record InventoryResponse(
    UUID productId,
    String productName,
    int quantity,
    int reservedQuantity,
    int availableQuantity,
    int version
) {

  public static InventoryResponse from(Inventory inventory) {
    return new InventoryResponse(
        inventory.getProductId(),
        inventory.getProductName(),
        inventory.getQuantity(),
        inventory.getReservedQuantity(),
        inventory.getQuantity() - inventory.getReservedQuantity(),
        inventory.getVersion()
    );
  }
}
