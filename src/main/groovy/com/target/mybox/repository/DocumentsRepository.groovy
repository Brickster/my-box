package com.target.mybox.repository

import com.target.mybox.domain.Document
import groovy.transform.CompileStatic
import org.springframework.data.mongodb.repository.MongoRepository

@CompileStatic
interface DocumentsRepository extends MongoRepository<Document, String> {

}
