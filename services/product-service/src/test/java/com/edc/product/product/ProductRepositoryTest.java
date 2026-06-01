package com.edc.product.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
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
class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @Test
  void shouldSaveAndFindProduct() {
    var product = new Product("Wireless Mouse", "Ergonomic mouse", BigDecimal.valueOf(49.99), 100);
    var saved = productRepository.save(product);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getName()).isEqualTo("Wireless Mouse");
    assertThat(saved.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(49.99));
    assertThat(saved.getQuantity()).isEqualTo(100);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getUpdatedAt()).isNotNull();
  }

  @Test
  void shouldFindById() {
    var product = new Product("Keyboard", "Mechanical keyboard", BigDecimal.valueOf(89.99), 50);
    var saved = productRepository.save(product);

    var found = productRepository.findById(saved.getId());
    assertThat(found).isPresent();
    assertThat(found.get().getName()).isEqualTo("Keyboard");
  }

  @Test
  void shouldFindAll() {
    productRepository.save(new Product("Mouse", "Wireless", BigDecimal.valueOf(29.99), 100));
    productRepository.save(new Product("Keyboard", "Mechanical", BigDecimal.valueOf(89.99), 50));

    var all = productRepository.findAll();
    assertThat(all).hasSize(2);
  }

  @Test
  void shouldDeleteProduct() {
    var product = new Product("Monitor", "4K display", BigDecimal.valueOf(499.99), 10);
    var saved = productRepository.save(product);

    productRepository.deleteById(saved.getId());
    assertThat(productRepository.findById(saved.getId())).isEmpty();
  }

  @Test
  void shouldUpdateProduct() {
    var product = new Product("Mouse", "Old description", BigDecimal.valueOf(29.99), 100);
    var saved = productRepository.save(product);

    saved.setName("Updated Mouse");
    saved.setPrice(BigDecimal.valueOf(39.99));
    productRepository.save(saved);

    var updated = productRepository.findById(saved.getId()).orElseThrow();
    assertThat(updated.getName()).isEqualTo("Updated Mouse");
    assertThat(updated.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(39.99));
  }

  @Test
  void shouldSetTimestampsOnCreate() {
    var product = new Product("Webcam", "HD webcam", BigDecimal.valueOf(79.99), 30);
    var saved = productRepository.save(product);

    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getUpdatedAt()).isNotNull();
  }

  @Test
  void shouldUpdateTimestampOnUpdate() {
    var product = new Product("Tablet", "10 inch", BigDecimal.valueOf(299.99), 20);
    var saved = productRepository.save(product);

    var originalUpdatedAt = saved.getUpdatedAt();

    saved.setName("Updated Tablet");
    productRepository.save(saved);

    var updated = productRepository.findById(saved.getId()).orElseThrow();
    assertThat(updated.getUpdatedAt()).isNotNull();
  }
}
