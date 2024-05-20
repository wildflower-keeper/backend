package org.wildflowergardening.backend.api.wildflowergardening.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.HomelessAuthInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.ShelterAdminAuthInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.ShelterPublicAuthInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final ShelterAdminAuthInterceptor shelterAdminAuthInterceptor;
  private final HomelessAuthInterceptor homelessAuthInterceptor;
  private final ShelterPublicAuthInterceptor shelterPublicAuthInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(shelterAdminAuthInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns();

    registry.addInterceptor(homelessAuthInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns();

    registry.addInterceptor(shelterPublicAuthInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns();
  }
}
