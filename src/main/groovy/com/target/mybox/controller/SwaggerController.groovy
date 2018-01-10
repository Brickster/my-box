package com.target.mybox.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

import java.nio.charset.Charset

@RestController
class SwaggerController {

  @GetMapping(value = '/swagger.yml', produces = MediaType.TEXT_PLAIN_VALUE)
  ResponseEntity<String> getSwaggerYml() {

    InputStream inputStream = getClass().getResourceAsStream("/public/my-box-v1.yml")
    String fileContents = StreamUtils.copyToString(inputStream, Charset.defaultCharset())

    HttpHeaders httpHeaders = new HttpHeaders()
    httpHeaders.add('Access-Control-Allow-Origin', '*')
    httpHeaders.add('Access-Control-Allow-Methods', 'GET')
    ResponseEntity<String> response = new ResponseEntity<>(fileContents, httpHeaders, HttpStatus.OK)

    return response
  }
}
