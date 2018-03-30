package com.target.mybox.config

import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import spock.lang.Specification

class CommonConfigSpec extends Specification {

  CommonConfig commonConfig = new CommonConfig(swaggerUiUrl: 'swagger-ui.com', faulty: true)

  void 'get object mapper'() {
    expect:
    commonConfig.getObjectMapper()
  }

  void 'add CORS mappings'() {

    given:
    CorsRegistry corsRegistry = Mock(CorsRegistry)
    CorsRegistration corsRegistration = Mock(CorsRegistration)

    when:
    commonConfig.addCorsMappings(corsRegistry)

    then:
    1 * corsRegistry.addMapping('/**') >> corsRegistration
    1 * corsRegistration.allowedOrigins(commonConfig.swaggerUiUrl)
    0 * _
  }

  void 'add PageableResolver'() {

    given:
    List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = []

    when:
    commonConfig.addArgumentResolvers(handlerMethodArgumentResolvers)

    then:
    handlerMethodArgumentResolvers.size() == 1
    PageableResolver.isAssignableFrom(handlerMethodArgumentResolvers.first().class)
    (handlerMethodArgumentResolvers.first() as PageableResolver).faulty == commonConfig.faulty
  }
}
