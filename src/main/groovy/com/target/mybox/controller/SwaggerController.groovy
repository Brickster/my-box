package com.target.mybox.controller

import groovy.transform.CompileStatic
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

import java.nio.charset.Charset

@CompileStatic
@RestController
class SwaggerController {

  @GetMapping(value = '/swagger.yml', produces = MediaType.TEXT_PLAIN_VALUE)
  ResponseEntity<String> getSwaggerYml() {

    InputStream inputStream = getClass().getResourceAsStream('/public/my-box-v1.yml')
    String fileContents = StreamUtils.copyToString(inputStream, Charset.defaultCharset())

    return new ResponseEntity<>(fileContents, HttpStatus.OK)
  }
}
