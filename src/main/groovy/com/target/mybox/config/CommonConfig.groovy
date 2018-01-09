package com.target.mybox.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing

@Configuration
@EnableMongoAuditing
class CommonConfig {

  @Bean
  ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper()
    objectMapper.findAndRegisterModules()
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    return objectMapper
  }
}
