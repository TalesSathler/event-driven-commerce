package com.edc.inventory.config;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .cors(cors -> {})
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, "/api/inventory/**").hasAnyAuthority("SCOPE_USER", "SCOPE_ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/inventory/**").hasAuthority("SCOPE_ADMIN")
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .anyRequest().authenticated())
        .anonymous(anonymous -> anonymous.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
        .exceptionHandling(e -> e.authenticationEntryPoint((request, response, authException) -> {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType("application/json");
          response.getWriter().write("{\"error\":\"Unauthorized\"}");
        }))
        .build();
  }

  @Bean
  Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
    var converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      var roles = jwt.getClaimAsStringList("roles");
      if (roles == null) return List.of();
      return roles.stream().<GrantedAuthority>map(r -> new SimpleGrantedAuthority("SCOPE_" + r)).toList();
    });
    return converter;
  }
}
