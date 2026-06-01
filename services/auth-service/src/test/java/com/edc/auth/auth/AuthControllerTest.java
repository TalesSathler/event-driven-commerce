package com.edc.auth.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edc.auth.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockitoBean private AuthService authService;

  @Test
  void shouldRegister() throws Exception {
    var request = new RegisterRequest("John Doe", "john@example.com", "password123");
    var response = new AuthResponse("token", "refresh", "John Doe", "john@example.com");
    when(authService.register(any())).thenReturn(response);

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").value("token"))
        .andExpect(jsonPath("$.name").value("John Doe"));
  }

  @Test
  void shouldReturn400OnInvalidRegister() throws Exception {
    var request = new RegisterRequest("", "invalid-email", "12");

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldLogin() throws Exception {
    var request = new LoginRequest("john@example.com", "password123");
    var response = new AuthResponse("token", "refresh", "John Doe", "john@example.com");
    when(authService.login(any())).thenReturn(response);

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("token"));
  }

  @Test
  void shouldReturn401OnInvalidLogin() throws Exception {
    var request = new LoginRequest("john@example.com", "wrong");
    when(authService.login(any())).thenThrow(new InvalidCredentialsException("Invalid email or password"));

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldRefresh() throws Exception {
    var request = new RefreshRequest("refresh-token");
    var response = new TokenResponse("new-token");
    when(authService.refresh(any())).thenReturn(response);

    mockMvc.perform(post("/api/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("new-token"));
  }

  @Test
  void shouldLogout() throws Exception {
    var request = new RefreshRequest("some-refresh-token");
    doNothing().when(authService).logout("some-refresh-token");

    mockMvc.perform(post("/api/auth/logout")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn401OnRefreshWithInvalidToken() throws Exception {
    var request = new RefreshRequest("invalid-token");
    when(authService.refresh(any())).thenThrow(new InvalidCredentialsException("Invalid refresh token"));

    mockMvc.perform(post("/api/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }
}
