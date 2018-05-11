package com.target.mybox.service

import com.target.mybox.domain.Authorization
import com.target.mybox.repository.AuthorizationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class AuthorizationsService {

  @Autowired
  AuthorizationsRepository authorizationsRepository

  Authorization create(Authorization authorization) {
    return authorizationsRepository.save(authorization)
  }

  Authorization get(String id) {
    authorizationsRepository.findOne(id)
  }

  Authorization getByToken(String token) {
    return authorizationsRepository.findByHashedToken(hash(token))
  }

  void delete(String id) {
    authorizationsRepository.delete(id)
  }

  Page<Authorization> list(Pageable pageable) {
    return authorizationsRepository.findAll(pageable)
  }

  private String hash(String s) {

  }
}
