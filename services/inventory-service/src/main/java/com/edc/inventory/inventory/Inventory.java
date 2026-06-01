package com.edc.inventory.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory")
public class Inventory {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "product_id", nullable = false, unique = true)
  private UUID productId;

  @Column(name = "product_name", nullable = false)
  private String productName;

  @Column(nullable = false)
  private int quantity;

  @Column(name = "reserved_quantity", nullable = false)
  private int reservedQuantity;

  @Version
  @Column(nullable = false)
  private int version;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  public Inventory() {}

  public Inventory(UUID productId, String productName, int quantity) {
    this.productId = productId;
    this.productName = productName;
    this.quantity = quantity;
    this.reservedQuantity = 0;
  }

  @PrePersist
  void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public UUID getId() { return id; }
  public UUID getProductId() { return productId; }
  public String getProductName() { return productName; }
  public int getQuantity() { return quantity; }
  public int getReservedQuantity() { return reservedQuantity; }
  public int getVersion() { return version; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }

  public void setProductId(UUID productId) { this.productId = productId; }
  public void setProductName(String productName) { this.productName = productName; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
  public void setReservedQuantity(int reservedQuantity) { this.reservedQuantity = reservedQuantity; }
}
