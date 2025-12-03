package io.github.xkhana.ayKhana.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.xkhana.ayKhana.infrastructure.ModelMapperUtilImpl;
import io.github.xkhana.ayKhana.util.ModelMapperUtil;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

  @Bean
  public ModelMapperUtil modelMapper() {
    return new ModelMapperUtilImpl();
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Bean
  public GeometryFactory geometryFactory() {
    return new GeometryFactory(new PrecisionModel(), 4326);
  }
}
