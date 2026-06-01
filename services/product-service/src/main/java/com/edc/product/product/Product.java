package com.edc.product.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(name = "stock_quantity", nullable = false)
  private int quantity;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  public Product() {}

  public Product(String name, String description, BigDecimal price, int quantity) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.quantity = quantity;
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
  public String getName() { return name; }
  public String getDescription() { return description; }
  public BigDecimal getPrice() { return price; }
  public int getQuantity() { return quantity; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }

  public void setName(String name) { this.name = name; }
  public void setDescription(String description) { this.description = description; }
  public void setPrice(BigDecimal price) { this.price = price; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
}
