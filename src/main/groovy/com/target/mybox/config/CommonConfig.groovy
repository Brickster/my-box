package com.target.mybox.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@CompileStatic
@Configuration
@EnableMongoAuditing
class CommonConfig extends WebMvcConfigurerAdapter {

  @Value('${swagger-ui.url}')
  String swaggerUiUrl

  @Bean
  ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper()
    objectMapper.findAndRegisterModules()
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    return objectMapper
  }

  @Override
  void addCorsMappings(CorsRegistry registry) {
    registry.addMapping('/**').allowedOrigins(swaggerUiUrl)
  }

  @Override
  void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new PageableResolver(new SortResolver()))
  }
}
