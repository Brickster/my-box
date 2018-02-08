package com.target.mybox.repository

import com.target.mybox.domain.FolderContent
import groovy.transform.CompileStatic
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

@CompileStatic
interface FolderContentsRepository extends MongoRepository<FolderContent, String> {
  Page<FolderContent> findAllByFolderId(String folderId, Pageable pageable)
  long deleteByDocumentId(String documentId)
  long deleteByFolderIdAndDocumentId(String folderId, String documentId)
  boolean existsByFolderIdAndDocumentId(String folderId, String documentId)
}
