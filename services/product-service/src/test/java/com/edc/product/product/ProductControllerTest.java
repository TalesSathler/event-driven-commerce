package com.edc.product.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.edc.product.config.SecurityConfig;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ProductService productService;

  @MockitoBean
  private JwtDecoder jwtDecoder;

  private final UUID productId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    when(jwtDecoder.decode(any())).thenThrow(new RuntimeException("JWT decode should not be called in tests"));
  }

  @Test
  void shouldReturnAllProducts() throws Exception {
    var response = new ProductResponse(productId, "Mouse", "Wireless", BigDecimal.valueOf(29.99), 100,
        LocalDateTime.now(), LocalDateTime.now());
    when(productService.findAll()).thenReturn(List.of(response));

    mockMvc.perform(get("/api/products")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_user"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Mouse"));
  }

  @Test
  void shouldReturnProductById() throws Exception {
    var response = new ProductResponse(productId, "Keyboard", "Mechanical", BigDecimal.valueOf(89.99), 50,
        LocalDateTime.now(), LocalDateTime.now());
    when(productService.findById(productId)).thenReturn(response);

    mockMvc.perform(get("/api/products/{id}", productId)
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_user"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Keyboard"));
  }

  @Test
  void shouldReturn404WhenProductNotFound() throws Exception {
    when(productService.findById(productId)).thenThrow(new ProductNotFoundException(productId));

    mockMvc.perform(get("/api/products/{id}", productId)
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_user"))))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldCreateProduct() throws Exception {
    var request = new ProductRequest("Mouse", "Wireless", BigDecimal.valueOf(29.99), 100);
    var response = new ProductResponse(productId, "Mouse", "Wireless", BigDecimal.valueOf(29.99), 100,
        LocalDateTime.now(), LocalDateTime.now());
    when(productService.create(any())).thenReturn(response);

    mockMvc.perform(post("/api/products")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_admin")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Mouse"));
  }

  @Test
  void shouldUpdateProduct() throws Exception {
    var request = new ProductRequest("Updated", "Desc", BigDecimal.valueOf(39.99), 50);
    var response = new ProductResponse(productId, "Updated", "Desc", BigDecimal.valueOf(39.99), 50,
        LocalDateTime.now(), LocalDateTime.now());
    when(productService.update(any(), any())).thenReturn(response);

    mockMvc.perform(put("/api/products/{id}", productId)
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_admin")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated"));
  }

  @Test
  void shouldDeleteProduct() throws Exception {
    mockMvc.perform(delete("/api/products/{id}", productId)
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_admin"))))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn400ForInvalidInput() throws Exception {
    var invalid = new ProductRequest("", null, BigDecimal.valueOf(-1), -1);

    mockMvc.perform(post("/api/products")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_admin")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalid)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn401WithoutToken() throws Exception {
    mockMvc.perform(get("/api/products"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldReturn403ForUserOnWriteOperations() throws Exception {
    var request = new ProductRequest("Mouse", "Wireless", BigDecimal.valueOf(29.99), 100);

    mockMvc.perform(post("/api/products")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_user")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }
}
