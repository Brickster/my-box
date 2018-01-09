package com.target.mybox.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

import java.time.Instant

@org.springframework.data.mongodb.core.mapping.Document(collection = 'documents')
class Document {
  String id
  String name
  String text
  @CreatedDate
  Instant created
  @LastModifiedDate
  Instant lastModified
}
