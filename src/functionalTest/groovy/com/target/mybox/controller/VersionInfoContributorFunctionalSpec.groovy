package com.target.mybox.controller

import com.target.mybox.FunctionalSpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class VersionInfoContributorFunctionalSpec extends FunctionalSpec {

  @Value('${api.version}')
  String version

  void 'getting info returns current version'() {

    when:
    ResponseEntity<Map<String, Object>> response = get('/info')

    then:
    response.statusCode == HttpStatus.OK
    response.body != null
    response.body.size() == 1
    response.body['version'] == version
  }
}
