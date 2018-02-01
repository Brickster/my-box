package com.target.mybox.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class SwaggerControllerSpec extends Specification {

  SwaggerController swaggerController = new SwaggerController()

  void 'getSwaggerYml'() {

    when:
    ResponseEntity<String> responseEntity = swaggerController.getSwaggerYml()

    then:
    responseEntity.body.contains('swagger: "2.0"')
    responseEntity.statusCode == HttpStatus.OK
  }
}
