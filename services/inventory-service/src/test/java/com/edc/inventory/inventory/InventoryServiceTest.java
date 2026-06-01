package com.edc.inventory.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

  @Mock
  private InventoryRepository inventoryRepository;

  @InjectMocks
  private InventoryService inventoryService;

  @Test
  void shouldReturnAllInventory() {
    var inventory = new Inventory(UUID.randomUUID(), "Mouse", 100);
    when(inventoryRepository.findAll()).thenReturn(List.of(inventory));

    var result = inventoryService.findAll();

    assertThat(result).hasSize(1);
    assertThat(result.getFirst().productName()).isEqualTo("Mouse");
  }

  @Test
  void shouldReturnInventoryByProductId() {
    var productId = UUID.randomUUID();
    var inventory = new Inventory(productId, "Keyboard", 50);
    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventory));

    var result = inventoryService.findByProductId(productId);

    assertThat(result.productName()).isEqualTo("Keyboard");
  }

  @Test
  void shouldThrowWhenProductNotFound() {
    var productId = UUID.randomUUID();
    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> inventoryService.findByProductId(productId))
        .isInstanceOf(InventoryNotFoundException.class);
  }

  @Test
  void shouldAdjustQuantity() {
    var productId = UUID.randomUUID();
    var inventory = new Inventory(productId, "Mouse", 100);
    var request = new InventoryAdjustRequest(50);

    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventory));
    when(inventoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var result = inventoryService.adjustQuantity(productId, request);

    assertThat(result.quantity()).isEqualTo(50);
    assertThat(result.availableQuantity()).isEqualTo(50);
  }

  @Test
  void shouldComputeAvailableQuantity() {
    var productId = UUID.randomUUID();
    var inventory = new Inventory(productId, "Mouse", 100);
    inventory.setReservedQuantity(30);
    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventory));

    var result = inventoryService.findByProductId(productId);

    assertThat(result.quantity()).isEqualTo(100);
    assertThat(result.reservedQuantity()).isEqualTo(30);
    assertThat(result.availableQuantity()).isEqualTo(70);
  }

  @Test
  void shouldThrowWhenAdjustingNonExistent() {
    var productId = UUID.randomUUID();
    var request = new InventoryAdjustRequest(50);
    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> inventoryService.adjustQuantity(productId, request))
        .isInstanceOf(InventoryNotFoundException.class);
  }
}
