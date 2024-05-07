package org.wildflowergardening.backend.api.kernel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("들꽃가드닝 API")
            .description("들꽃가드닝 API")
            .version("1.0.0"));
  }
}
