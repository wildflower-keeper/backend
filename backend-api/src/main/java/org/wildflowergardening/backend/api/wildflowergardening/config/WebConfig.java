package org.wildflowergardening.backend.api.wildflowergardening.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.HomelessAuthInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final HandlerInterceptor shelterAuthInterceptor;
  private final HomelessAuthInterceptor homelessAuthInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(shelterAuthInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns();

    registry.addInterceptor(homelessAuthInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns();
  }
}
