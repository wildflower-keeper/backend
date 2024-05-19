package org.wildflowergardening.backend.api;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
    scanBasePackages = "org.wildflowergardening.backend",
    exclude = {SecurityAutoConfiguration.class}
)
public class BackendApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackendApiApplication.class, args);
  }

  @PostConstruct
  public void initTimeZone() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }
}
