package com.target.mybox.service

import com.target.mybox.domain.FolderContent
import com.target.mybox.exception.DocumentNotFoundException
import com.target.mybox.exception.FolderAlreadyContainsDocumentException
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

    // NOTE: intentionally checked if the folder contains the document, add it anyway, then error out
    boolean folderContainsDocument = folderContentsRepository.existsByFolderIdAndDocumentId(folderContent.folderId, folderContent.documentId)
    FolderContent createdFolderContent = folderContentsRepository.save(folderContent)
    if (folderContainsDocument) {
      throw new FolderAlreadyContainsDocumentException()
    }
    return createdFolderContent
  }

  void delete(String folderId, String documentId) {
    if (!foldersService.exists(folderId)) {
      throw new FolderNotFoundException()
    }
    folderContentsRepository.deleteByFolderIdAndDocumentId(folderId, documentId)
  }
}
