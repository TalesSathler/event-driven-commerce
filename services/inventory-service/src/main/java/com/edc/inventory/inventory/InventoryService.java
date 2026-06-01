package com.edc.inventory.inventory;

import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

  private final InventoryRepository inventoryRepository;

  public InventoryService(InventoryRepository inventoryRepository) {
    this.inventoryRepository = inventoryRepository;
  }

  public List<InventoryResponse> findAll() {
    return inventoryRepository.findAll().stream()
        .map(InventoryResponse::from)
        .toList();
  }

  public InventoryResponse findByProductId(UUID productId) {
    return inventoryRepository.findByProductId(productId)
        .map(InventoryResponse::from)
        .orElseThrow(() -> new InventoryNotFoundException(productId));
  }

  @Transactional
  public InventoryResponse adjustQuantity(UUID productId, InventoryAdjustRequest request) {
    var inventory = inventoryRepository.findByProductId(productId)
        .orElseThrow(() -> new InventoryNotFoundException(productId));

    inventory.setQuantity(request.quantity());

    try {
      return InventoryResponse.from(inventoryRepository.save(inventory));
    } catch (OptimisticLockingFailureException | OptimisticLockException e) {
      throw new OptimisticLockingFailureException("Concurrent modification detected for product: " + productId);
    }
  }
}
