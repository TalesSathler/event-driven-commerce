package com.edc.product.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.edc.product.event.ProductEventPublisher;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private ProductEventPublisher eventPublisher;

  @InjectMocks
  private ProductService productService;

  @Test
  void shouldReturnAllProducts() {
    var product = new Product("Mouse", "Wireless", BigDecimal.valueOf(29.99), 100);
    when(productRepository.findAll()).thenReturn(List.of(product));

    var result = productService.findAll();

    assertThat(result).hasSize(1);
    assertThat(result.getFirst().name()).isEqualTo("Mouse");
  }

  @Test
  void shouldReturnProductById() {
    var id = UUID.randomUUID();
    var product = new Product("Keyboard", "Mechanical", BigDecimal.valueOf(89.99), 50);
    when(productRepository.findById(id)).thenReturn(Optional.of(product));

    var result = productService.findById(id);

    assertThat(result.name()).isEqualTo("Keyboard");
  }

  @Test
  void shouldThrowWhenProductNotFound() {
    var id = UUID.randomUUID();
    when(productRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.findById(id))
        .isInstanceOf(ProductNotFoundException.class);
  }

  @Test
  void shouldCreateProduct() {
    var request = new ProductRequest("Mouse", "Wireless", BigDecimal.valueOf(29.99), 100);
    var product = new Product("Mouse", "Wireless", BigDecimal.valueOf(29.99), 100);
    when(productRepository.save(any())).thenReturn(product);

    var result = productService.create(request);

    assertThat(result.name()).isEqualTo("Mouse");
    verify(eventPublisher).publishCreated(any());
  }

  @Test
  void shouldUpdateProduct() {
    var id = UUID.randomUUID();
    var existing = new Product("Old Name", "Desc", BigDecimal.valueOf(10), 5);
    var request = new ProductRequest("New Name", "New Desc", BigDecimal.valueOf(20), 10);

    when(productRepository.findById(id)).thenReturn(Optional.of(existing));
    when(productRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var result = productService.update(id, request);

    assertThat(result.name()).isEqualTo("New Name");
    assertThat(result.price()).isEqualByComparingTo(BigDecimal.valueOf(20));
    verify(eventPublisher).publishUpdated(any());
  }

  @Test
  void shouldThrowWhenUpdatingNonExistent() {
    var id = UUID.randomUUID();
    var request = new ProductRequest("Name", "Desc", BigDecimal.valueOf(10), 5);
    when(productRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.update(id, request))
        .isInstanceOf(ProductNotFoundException.class);
  }

  @Test
  void shouldDeleteProduct() {
    var id = UUID.randomUUID();
    when(productRepository.existsById(id)).thenReturn(true);

    productService.delete(id);

    verify(productRepository).deleteById(id);
    verify(eventPublisher).publishDeleted(id);
  }

  @Test
  void shouldThrowWhenDeletingNonExistent() {
    var id = UUID.randomUUID();
    when(productRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> productService.delete(id))
        .isInstanceOf(ProductNotFoundException.class);
  }
}
