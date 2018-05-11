package com.target.mybox.controller

import com.target.mybox.domain.Authorization
import com.target.mybox.service.AuthorizationsService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@CompileStatic
@RequestMapping('/authorizations')
class AuthorizationsController {

  @Autowired
  AuthorizationsService authorizationsService

  @PostMapping
  Authorization postAuthorization() {

  }

  @GetMapping('/{id}')
  Authorization getAuthorization(@PathVariable String id) {
    Authorization authorization = authorizationsService.get(id)
    authorization.token = null
    return authorization
  }

  @DeleteMapping('/{id}')
  void deleteAuthorization(@PathVariable String id) {

  }

  @GetMapping
  List<Authorization> listAuthorizations(Pageable pageable) {
    Page<Authorization> authorizations = authorizationsService.list(pageable)
    authorizations.content.each { it.token == null }
    return authorizations.content
  }
}
