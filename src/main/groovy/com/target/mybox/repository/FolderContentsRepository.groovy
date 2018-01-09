package com.target.mybox.repository

import com.target.mybox.domain.FolderContent
import org.springframework.data.mongodb.repository.MongoRepository

interface FolderContentsRepository extends MongoRepository<FolderContent, String> {
  List<FolderContent> findAllByFolderId(String folderId)
  long deleteByFolderIdAndDocumentId(String folderId, String documentId)
}