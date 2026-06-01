package com.edc.auth.user;

import static org.assertj.core.api.Assertions.assertThat;

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
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void shouldSaveAndFindUser() {
    var user = new User("John Doe", "john@example.com", "hashed-password", "USER");
    var saved = userRepository.save(user);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getName()).isEqualTo("John Doe");
    assertThat(saved.getEmail()).isEqualTo("john@example.com");
    assertThat(saved.getPassword()).isEqualTo("hashed-password");
    assertThat(saved.getRole()).isEqualTo("USER");
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getUpdatedAt()).isNotNull();
  }

  @Test
  void shouldFindByEmail() {
    var user = new User("Jane Doe", "jane@example.com", "hashed-password", "USER");
    userRepository.save(user);

    var found = userRepository.findByEmail("jane@example.com");
    assertThat(found).isPresent();
    assertThat(found.get().getName()).isEqualTo("Jane Doe");
  }

  @Test
  void shouldReturnEmptyForUnknownEmail() {
    var found = userRepository.findByEmail("unknown@example.com");
    assertThat(found).isEmpty();
  }

  @Test
  void shouldCheckEmailExists() {
    var user = new User("John", "john@example.com", "hash", "USER");
    userRepository.save(user);

    assertThat(userRepository.existsByEmail("john@example.com")).isTrue();
    assertThat(userRepository.existsByEmail("other@example.com")).isFalse();
  }
}
