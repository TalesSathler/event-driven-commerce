package com.edc.auth.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(summary = "Register a new user", description = "Creates a new user account and returns JWT tokens")
  @ApiResponse(responseCode = "201", description = "User registered successfully",
      content = @Content(schema = @Schema(implementation = AuthResponse.class)))
  @ApiResponse(responseCode = "400", description = "Invalid input or email already exists",
      content = @Content)
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
    return authService.register(request);
  }

  @Operation(summary = "Login", description = "Authenticates a user and returns JWT tokens")
  @ApiResponse(responseCode = "200", description = "Login successful",
      content = @Content(schema = @Schema(implementation = AuthResponse.class)))
  @ApiResponse(responseCode = "401", description = "Invalid email or password",
      content = @Content)
  @PostMapping("/login")
  public AuthResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
  }

  @Operation(summary = "Refresh token", description = "Issues a new access token using a valid refresh token")
  @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
      content = @Content(schema = @Schema(implementation = TokenResponse.class)))
  @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token",
      content = @Content)
  @PostMapping("/refresh")
  public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
    return authService.refresh(request);
  }

  @Operation(summary = "Logout", description = "Invalidates a refresh token, ending the session")
  @ApiResponse(responseCode = "204", description = "Logged out successfully")
  @SecurityRequirement(name = "bearer")
  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout(@Valid @RequestBody RefreshRequest request) {
    authService.logout(request.refreshToken());
  }

  @Operation(summary = "Get current user", description = "Returns the authenticated user's profile information")
  @ApiResponse(responseCode = "200", description = "User profile retrieved",
      content = @Content(schema = @Schema(implementation = MeResponse.class)))
  @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
      content = @Content)
  @SecurityRequirement(name = "bearer")
  @GetMapping("/me")
  public MeResponse me(@AuthenticationPrincipal Jwt jwt) {
    var userId = UUID.fromString(jwt.getSubject());
    return authService.me(userId);
  }
}
