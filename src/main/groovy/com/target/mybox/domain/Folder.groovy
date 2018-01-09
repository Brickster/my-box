package com.target.mybox.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate

import java.time.Instant

@org.springframework.data.mongodb.core.mapping.Document(collection = 'folders')
class Folder {
  @Id
  String id
  String name
  @CreatedDate
  Instant created
  @LastModifiedDate
  Instant lastModified
}
