package org.wildflowergardening.backend.api.wildflowergardening.application.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.HomelessUserContext;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.UserContext;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

class HomelessAppJwtProviderTest {

  private HomelessAppJwtProvider homelessAppJwtProvider = new HomelessAppJwtProvider(
      "TestTokenSecretTestTokenSecretTestTokenSecretTestTokenSecretTestTokenSecretTestTokenSecretTestTokenSecret"
  );

  @Test
  void tokenTest() {
    String token = homelessAppJwtProvider.createToken(HomelessUserContext.builder()
        .homelessId(100L)
        .homelessName("홍길동")
        .shelterId(1L)
        .build());

    UserContext userContext = homelessAppJwtProvider.getUserContextFrom(token);
    assertThat(userContext.getUserId()).isEqualTo(100L);
    assertThat(userContext.getUserRole()).isEqualTo(UserRole.HOMELESS);
    assertThat(userContext.getUsername()).isEqualTo("홍길동");
  }
}