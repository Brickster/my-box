package com.target.mybox.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate

import java.time.Instant

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
