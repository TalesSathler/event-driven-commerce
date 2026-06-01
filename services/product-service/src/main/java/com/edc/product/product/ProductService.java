package com.edc.product.product;

import com.edc.product.event.ProductEventPublisher;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductEventPublisher eventPublisher;

  public ProductService(ProductRepository productRepository, ProductEventPublisher eventPublisher) {
    this.productRepository = productRepository;
    this.eventPublisher = eventPublisher;
  }

  public List<ProductResponse> findAll() {
    return productRepository.findAll().stream()
        .map(ProductResponse::from)
        .toList();
  }

  public ProductResponse findById(UUID id) {
    return productRepository.findById(id)
        .map(ProductResponse::from)
        .orElseThrow(() -> new ProductNotFoundException(id));
  }

  @Transactional
  public ProductResponse create(ProductRequest request) {
    var product = new Product(
        request.name(),
        request.description(),
        request.price(),
        request.quantity()
    );
    var saved = productRepository.save(product);
    eventPublisher.publishCreated(saved);
    return ProductResponse.from(saved);
  }

  @Transactional
  public ProductResponse update(UUID id, ProductRequest request) {
    var product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException(id));
    product.setName(request.name());
    product.setDescription(request.description());
    product.setPrice(request.price());
    product.setQuantity(request.quantity());
    var saved = productRepository.save(product);
    eventPublisher.publishUpdated(saved);
    return ProductResponse.from(saved);
  }

  @Transactional
  public void delete(UUID id) {
    if (!productRepository.existsById(id)) {
      throw new ProductNotFoundException(id);
    }
    productRepository.deleteById(id);
    eventPublisher.publishDeleted(id);
  }
}
