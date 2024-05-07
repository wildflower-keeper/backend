package org.wildflowergardening.backend.core.kernel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource(
    value = "classpath:application-core-${spring.profiles.active:local}.yaml",
    factory = YamlPropertySourceFactory.class
)
public class WebClientConfig {

  @Value("${test-services.skeleton.url}")
  private String skeletonUrl;

  @Bean
  WebClient testSkeletonWebClient() {
    return WebClient.builder()
        .baseUrl(skeletonUrl)
        .build();
  }
}
