package com.target.mybox.domain

import com.target.mybox.annotation.JsonIsoFormat
import groovy.transform.CompileStatic
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

import javax.validation.constraints.NotBlank
import java.time.Instant

@CompileStatic
@org.springframework.data.mongodb.core.mapping.Document(collection = 'documents')
class Document {

  public static final String NAME = 'name'
  public static final String TEXT = 'text'

  String id
  @NotBlank
  String name
  @NotBlank
  String text
  @JsonIsoFormat
  @CreatedDate
  Instant created
  @JsonIsoFormat
  @LastModifiedDate
  Instant lastModified
}
