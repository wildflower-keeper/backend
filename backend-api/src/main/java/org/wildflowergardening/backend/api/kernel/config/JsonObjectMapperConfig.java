package org.wildflowergardening.backend.api.kernel.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JsonObjectMapperConfig {

  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS";
  public static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

  @Bean
  @Primary
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> {
      builder.serializers(new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
      builder.deserializers(new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));
    };
  }
}
