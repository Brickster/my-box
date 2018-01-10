package com.target.mybox.service

import com.target.mybox.domain.FolderContent
import com.target.mybox.exception.DocumentNotFoundException
import com.target.mybox.exception.FolderNotFoundException
import com.target.mybox.repository.FolderContentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FolderContentsService {

  @Autowired
  FolderContentsRepository folderContentsRepository

  @Autowired
  FoldersService foldersService

  @Autowired
  DocumentsService documentsService

  List<FolderContent> getAllByFolder(String folderId) {
    if (!foldersService.exists(folderId)) {
      throw new FolderNotFoundException()
    }
    return folderContentsRepository.findAllByFolderId(folderId)
  }

  FolderContent create(FolderContent folderContent) {
    if (!foldersService.exists(folderContent.folderId)) {
      throw new FolderNotFoundException()
    }
    if (!documentsService.exists(folderContent.documentId)) {
      throw new DocumentNotFoundException()
    }
    return folderContentsRepository.save(folderContent)
  }

  void delete(String folderId, String documentId) {
    if (!foldersService.exists(folderId)) {
      throw new FolderNotFoundException()
    }
    folderContentsRepository.deleteByFolderIdAndDocumentId(folderId, documentId)
  }
}
