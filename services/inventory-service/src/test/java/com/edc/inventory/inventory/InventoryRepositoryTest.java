package com.edc.inventory.inventory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class InventoryRepositoryTest {

  @Autowired
  private InventoryRepository inventoryRepository;

  @Test
  void shouldSaveAndFindInventory() {
    var productId = UUID.randomUUID();
    var inventory = new Inventory(productId, "Wireless Mouse", 100);
    var saved = inventoryRepository.save(inventory);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getProductId()).isEqualTo(productId);
    assertThat(saved.getProductName()).isEqualTo("Wireless Mouse");
    assertThat(saved.getQuantity()).isEqualTo(100);
    assertThat(saved.getReservedQuantity()).isEqualTo(0);
    assertThat(saved.getVersion()).isEqualTo(0);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getUpdatedAt()).isNotNull();
  }

  @Test
  void shouldFindByProductId() {
    var productId = UUID.randomUUID();
    inventoryRepository.save(new Inventory(productId, "Keyboard", 50));

    var found = inventoryRepository.findByProductId(productId);
    assertThat(found).isPresent();
    assertThat(found.get().getProductName()).isEqualTo("Keyboard");
  }

  @Test
  void shouldReturnEmptyForUnknownProductId() {
    var found = inventoryRepository.findByProductId(UUID.randomUUID());
    assertThat(found).isEmpty();
  }

  @Test
  void shouldEnforceUniqueProductId() {
    var productId = UUID.randomUUID();
    inventoryRepository.save(new Inventory(productId, "Mouse", 100));

    var duplicate = new Inventory(productId, "Mouse Duplicate", 50);
    var thrown = org.junit.jupiter.api.Assertions.assertThrows(
        Exception.class, () -> inventoryRepository.saveAndFlush(duplicate));
    assertThat(thrown).isNotNull();
  }

  @Test
  void shouldFindAll() {
    inventoryRepository.save(new Inventory(UUID.randomUUID(), "Mouse", 100));
    inventoryRepository.save(new Inventory(UUID.randomUUID(), "Keyboard", 50));

    var all = inventoryRepository.findAll();
    assertThat(all).hasSize(2);
  }

  @Test
  void shouldDelete() {
    var inventory = new Inventory(UUID.randomUUID(), "Monitor", 10);
    var saved = inventoryRepository.save(inventory);

    inventoryRepository.deleteById(saved.getId());
    assertThat(inventoryRepository.findById(saved.getId())).isEmpty();
  }

  @Test
  void shouldIncrementVersionOnUpdate() {
    var inventory = new Inventory(UUID.randomUUID(), "Tablet", 20);
    var saved = inventoryRepository.save(inventory);

    assertThat(saved.getVersion()).isEqualTo(0);

    saved.setQuantity(30);
    var updated = inventoryRepository.saveAndFlush(saved);

    assertThat(updated.getVersion()).isEqualTo(1);
  }
}
