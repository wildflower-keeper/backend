package org.wildflowergardening.backend.api.kernel.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.interceptor.HomelessAuthInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.interceptor.ShelterAdminAuthInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.interceptor.ShelterPublicAuthInterceptor;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final ShelterAdminAuthInterceptor shelterAdminAuthInterceptor;
  private final HomelessAuthInterceptor homelessAuthInterceptor;
  private final ShelterPublicAuthInterceptor shelterPublicAuthInterceptor;

/*  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean(){
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000", "http://127.0.0.1:3000",
            "http://localhost:80", "http://127.0.0.1:80",
            "http://localhost:8080", "http://127.0.0.1:8080",
            "https://wildflower-gardening.com", "https://www.wildflower-gardening.com",
            "https://wildflower-gardening.com:443", "https://www.wildflower-gardening.com:443",
            "http://wildflower-gardening.com", "http://www.wildflower-gardening.com",
            "http://wildflower-gardening.com:80", "http://www.wildflower-gardening.com:80",
            "https://api.wildflower-gardening.com"
    ));

    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("auth-token", "Content-Type", "X-Requested-With", "Accept", "Origin"));
//    configuration.setAllowCredentials(true); // 인증 정보 허용 설정 추가
    configuration.setMaxAge(3600L); // 캐시 유효 기간 설정

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    FilterRegistrationBean<CorsFilter> filterBean = new FilterRegistrationBean<>(new CorsFilter(source));
    filterBean.setOrder(0); // 필터 순서 설정
    filterBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
    return filterBean;
  }*/


  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns(
            "http://localhost:3000", "http://127.0.0.1:3000",
            "http://localhost:80", "http://127.0.0.1:80",
            "http://localhost:8080", "http://127.0.0.1:8080",
            "https://wildflower-gardening.com", "https://www.wildflower-gardening.com",
            "https://wildflower-gardening.com:443", "https://www.wildflower-gardening.com:443",
            "http://wildflower-gardening.com", "http://www.wildflower-gardening.com",
            "http://wildflower-gardening.com:80", "http://www.wildflower-gardening.com:80",
            "https://api.wildflower-gardening.com"
        )
        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        .allowedHeaders("auth-token", "Content-Type", "X-Requested-With", "Accept", "Origin")
        .maxAge(3_600);
  }

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
