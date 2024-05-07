package org.wildflowergardening.backend.core.wildflowergardening.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.core.kernel.config.YamlPropertySourceFactory;

@Component
@PropertySource(
    value = "classpath:application-core-${spring.profiles.active:local}.yaml",
    factory = YamlPropertySourceFactory.class
)
@Getter
public class AccountAdmin {

  @Value("${admin-account.email}")
  private String email;

  @Value("${admin-account.password}")
  private String password;
}
