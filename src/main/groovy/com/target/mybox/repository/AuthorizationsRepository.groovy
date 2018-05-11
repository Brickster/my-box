package com.target.mybox.repository

import com.target.mybox.domain.Authorization
import groovy.transform.CompileStatic
import org.springframework.data.mongodb.repository.MongoRepository

@CompileStatic
interface AuthorizationsRepository extends MongoRepository<Authorization, String> {
  Authorization findByHashedToken(String hashedToken)
}
