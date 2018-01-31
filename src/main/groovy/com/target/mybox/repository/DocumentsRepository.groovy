package com.target.mybox.repository

import com.target.mybox.domain.Document
import org.springframework.data.mongodb.repository.MongoRepository

interface DocumentsRepository extends MongoRepository<Document, String> {

}
