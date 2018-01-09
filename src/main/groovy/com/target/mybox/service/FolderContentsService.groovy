package com.target.mybox.service

import com.target.mybox.domain.FolderContent
import com.target.mybox.repository.FolderContentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FolderContentsService {

  @Autowired
  FolderContentsRepository folderContentsRepository

  List<FolderContent> getAllByFolder(String folderId) {
    return folderContentsRepository.findAllByFolderId(folderId)
  }

  FolderContent create(FolderContent folderContent) {
    // TODO: check that folder and document exist
    // TODO: check that the folder doesn't already contain the document
    return folderContentsRepository.save(folderContent)
  }

  void delete(String folderId, String documentId) {
    folderContentsRepository.deleteByFolderIdAndDocumentId(folderId, documentId)
  }
}
