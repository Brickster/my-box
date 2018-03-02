package com.target.mybox

import com.target.mybox.config.CommonConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

import java.time.Instant

@SpringBootTest(classes = [Application], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = [Application])
class FunctionalSpec extends Specification {

  public static final String ISO_FORMAT = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d{3}Z$/

  @Value('${server.port:8080}')
  int port

  @Value('${server.contextPath}')
  String contextPath

  RestTemplate restTemplate

  String date1Iso = '2016-01-02T03:04:05.000Z'
  Instant date1 = Instant.ofEpochMilli(1451703845000)
  String date2Iso = '2016-11-12T13:14:15.016Z'
  Instant date2 = Instant.ofEpochMilli(1478956455016)

  void setup() {
    restTemplate = new RestTemplateBuilder()
        .messageConverters(new MappingJackson2HttpMessageConverter(new CommonConfig().getObjectMapper()))
        .requestFactory(new HttpComponentsClientHttpRequestFactory())
        .errorHandler(new Only5xxErrorHandler())
        .build()
  }

  private URI uri(String path = '') {
    URI uri = new URI("http://localhost:${port}${contextPath}${path}")
    return uri
  }

  ResponseEntity<List<Map<String, Object>>> getList(String api, Map<String, Object> queryParameters = null) {
    return getInternal(api, queryParameters, List)
  }

  ResponseEntity<Map<String, Object>> getError(String api, Map<String, Object> queryParameters = null) {
    return get(api, queryParameters)
  }

  ResponseEntity<Map<String, Object>> get(String api, Map<String, Object> queryParameters = null) {
    return getInternal(api, queryParameters, Map)
  }

  private <T> ResponseEntity<T> getInternal(String path, Map<String, Object> queryParameters, Class<T> clazz) {
    UriComponentsBuilder builder = new UriComponentsBuilder().uri(uri(path))
    queryParameters.each { k, v ->
      if (v != null) {
        builder.queryParam(k, v)
      }
    }
    return restTemplate.getForEntity(builder.build().toUri(), clazz)
  }

  ResponseEntity<Map<String, Object>> post(String api, Object resource = null) {
    return restTemplate.postForEntity(uri(api), resource, Map)
  }

  ResponseEntity<Map<String, Object>> postError(String api, Object resource = null) {
    return post(api, resource)
  }

  public <T> ResponseEntity<Map<String, Object>> put(String api, T resource = null) {
    return restTemplate.exchange(uri(api), HttpMethod.PUT, new HttpEntity<T>(resource), Map)
  }

  ResponseEntity<Map<String, Object>> patch(String api, Map resource = null) {
    return restTemplate.exchange(uri(api), HttpMethod.PATCH, new HttpEntity<Map>(resource), Map)
  }

  ResponseEntity<Map<String, Object>> delete(String api) {
    return restTemplate.exchange(uri(api), HttpMethod.DELETE, null, Map)
  }

  private static class Only5xxErrorHandler implements ResponseErrorHandler {

    @Override
    boolean hasError(ClientHttpResponse response) throws IOException {
      return response.statusCode.is5xxServerError()
    }

    @Override
    void handleError(ClientHttpResponse response) throws IOException {
    }
  }
}
