package com.edc.inventory.inventory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edc.inventory.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InventoryController.class)
@Import(SecurityConfig.class)
class InventoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private InventoryService inventoryService;

  @MockitoBean
  private JwtDecoder jwtDecoder;

  @Test
  void shouldReturnAllInventory() throws Exception {
    var productId = UUID.randomUUID();
    var response = new InventoryResponse(productId, "Mouse", 100, 0, 100, 0);
    when(inventoryService.findAll()).thenReturn(List.of(response));

    mockMvc.perform(get("/api/inventory")
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_USER"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].productName").value("Mouse"))
        .andExpect(jsonPath("$[0].availableQuantity").value(100));
  }

  @Test
  void shouldReturnInventoryByProductId() throws Exception {
    var productId = UUID.randomUUID();
    var response = new InventoryResponse(productId, "Keyboard", 50, 0, 50, 0);
    when(inventoryService.findByProductId(productId)).thenReturn(response);

    mockMvc.perform(get("/api/inventory/{productId}", productId)
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_USER"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productName").value("Keyboard"));
  }

  @Test
  void shouldReturn404WhenNotFound() throws Exception {
    var productId = UUID.randomUUID();
    when(inventoryService.findByProductId(productId)).thenThrow(new InventoryNotFoundException(productId));

    mockMvc.perform(get("/api/inventory/{productId}", productId)
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_USER"))))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldAdjustQuantity() throws Exception {
    var productId = UUID.randomUUID();
    var response = new InventoryResponse(productId, "Mouse", 50, 0, 50, 1);
    when(inventoryService.adjustQuantity(any(), any())).thenReturn(response);

    mockMvc.perform(put("/api/inventory/{productId}", productId)
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new InventoryAdjustRequest(50))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.quantity").value(50));
  }

  @Test
  void shouldReturn400ForNegativeQuantity() throws Exception {
    var productId = UUID.randomUUID();

    mockMvc.perform(put("/api/inventory/{productId}", productId)
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"quantity\": -1}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn401WithoutToken() throws Exception {
    mockMvc.perform(get("/api/inventory"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldReturn403ForUserOnWriteOperations() throws Exception {
    mockMvc.perform(put("/api/inventory/{productId}", UUID.randomUUID())
            .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_USER")))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"quantity\": 50}"))
        .andExpect(status().isForbidden());
  }
}
