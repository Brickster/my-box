package com.target.mybox.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.CompileStatic
import org.springframework.data.annotation.CreatedDate

import java.time.Instant

@CompileStatic
@org.springframework.data.mongodb.core.mapping.Document(collection = 'folder_contents')
class FolderContent {
  @JsonIgnore
  String id
  String documentId
  @JsonIgnore
  String folderId
  @CreatedDate
  Instant created
}
