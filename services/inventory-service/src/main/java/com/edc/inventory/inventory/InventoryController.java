package com.edc.inventory.inventory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @Operation(summary = "List all inventory items", description = "Returns all inventory records")
  @ApiResponse(responseCode = "200", description = "List of inventory items")
  @GetMapping
  ResponseEntity<List<InventoryResponse>> getAll() {
    return ResponseEntity.ok(inventoryService.findAll());
  }

  @Operation(summary = "Get inventory by product ID", description = "Returns inventory for a specific product")
  @ApiResponse(responseCode = "200", description = "Inventory found")
  @ApiResponse(responseCode = "404", description = "Product not found in inventory", content = @Content)
  @GetMapping("/{productId}")
  ResponseEntity<InventoryResponse> getByProductId(@PathVariable UUID productId) {
    return ResponseEntity.ok(inventoryService.findByProductId(productId));
  }

  @Operation(summary = "Adjust stock quantity", description = "Updates inventory quantity for a product (admin only)")
  @ApiResponse(responseCode = "200", description = "Quantity adjusted")
  @ApiResponse(responseCode = "400", description = "Invalid quantity or would go negative", content = @Content)
  @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
  @PutMapping("/{productId}")
  ResponseEntity<InventoryResponse> adjustQuantity(
      @PathVariable UUID productId,
      @Valid @RequestBody InventoryAdjustRequest request) {
    return ResponseEntity.ok(inventoryService.adjustQuantity(productId, request));
  }

  @ExceptionHandler(InventoryNotFoundException.class)
  ResponseEntity<String> handleNotFound(InventoryNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(OptimisticLockingFailureException.class)
  ResponseEntity<String> handleOptimisticLocking(OptimisticLockingFailureException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }
}
