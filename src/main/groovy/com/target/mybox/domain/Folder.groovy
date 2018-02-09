package com.target.mybox.domain

import com.target.mybox.annotation.JsonIsoFormat
import groovy.transform.CompileStatic
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate

import javax.validation.constraints.NotBlank
import java.time.Instant

@CompileStatic
@org.springframework.data.mongodb.core.mapping.Document(collection = 'folders')
class Folder {
  @Id
  String id
  @NotBlank
  String name
  @JsonIsoFormat
  @CreatedDate
  Instant created
  @JsonIsoFormat
  @LastModifiedDate
  Instant lastModified
}
