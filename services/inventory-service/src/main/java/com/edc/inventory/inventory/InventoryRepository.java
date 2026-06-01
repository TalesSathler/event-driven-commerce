package com.edc.inventory.inventory;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

  Optional<Inventory> findByProductId(UUID productId);
}
