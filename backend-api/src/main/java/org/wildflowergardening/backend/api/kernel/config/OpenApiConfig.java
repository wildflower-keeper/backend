package org.wildflowergardening.backend.api.kernel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    Server local = new Server();
    local.setDescription("local");
    local.setUrl("http://localhost:8080");

    Server prod = new Server();
    prod.setDescription("prod");
    prod.setUrl("https://api.wildflower-gardening.com");

    OpenAPI openAPI = new OpenAPI()
        .info(new Info()
            .title("들꽃가드닝 API")
            .description("들꽃가드닝 API")
            .version("1.0.0"));
    openAPI.setServers(Arrays.asList(local, prod));
    return openAPI;
  }

  @Bean
  public GroupedOpenApi wildflowerAdminGroup() {
    return GroupedOpenApi.builder()
        .group("들꽃지기 Admin API")
        .pathsToMatch("/api/v1/wildflower-admin/**")
        .build();
  }

  @Bean
  public GroupedOpenApi shelterAdminGroup() {
    return GroupedOpenApi.builder()
        .group("센터 Admin API")
        .pathsToMatch("/api/v1/shelter-admin/**")
        .build();
  }

  @Bean
  public GroupedOpenApi shelterPublicGroup() {
    return GroupedOpenApi.builder()
        .group("센터 공용 노숙인 서비스 API")
        .pathsToMatch("/api/v1/shelter-public/**")
        .build();
  }

  @Bean
  public GroupedOpenApi homelessGroup() {
    return GroupedOpenApi.builder()
        .group("노숙인 앱 API")
        .pathsToMatch("/api/v1/homeless-app/**")
        .build();
  }

  @Bean
  public GroupedOpenApi sharedGroup() {
    return GroupedOpenApi.builder()
        .group("공통 API")
        .pathsToMatch("/api/v1/shared/**")
        .build();
  }
}
