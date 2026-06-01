package com.edc.product.product;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  ResponseEntity<List<ProductResponse>> getAll() {
    return ResponseEntity.ok(productService.findAll());
  }

  @GetMapping("/{id}")
  ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(productService.findById(id));
  }

  @PostMapping
  ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
    var response = productService.create(request);
    return ResponseEntity.created(URI.create("/api/products/" + response.id())).body(response);
  }

  @PutMapping("/{id}")
  ResponseEntity<ProductResponse> update(@PathVariable UUID id, @Valid @RequestBody ProductRequest request) {
    return ResponseEntity.ok(productService.update(id, request));
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> delete(@PathVariable UUID id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(ProductNotFoundException.class)
  ResponseEntity<String> handleNotFound(ProductNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }
}
