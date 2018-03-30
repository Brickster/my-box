package com.target.mybox.service

import com.target.mybox.domain.FolderContent
import com.target.mybox.exception.DocumentNotFoundException
import com.target.mybox.exception.FolderAlreadyContainsDocumentException
import com.target.mybox.exception.FolderNotFoundException
import com.target.mybox.repository.FolderContentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

//@CompileStatic
@Service
class FolderContentsService {

  @Value('${faulty}')
  boolean faulty

  @Autowired
  FolderContentsRepository folderContentsRepository

  @Autowired
  FoldersService foldersService

  @Autowired
  DocumentsService documentsService

  Page<FolderContent> getAllByFolder(String folderId, Pageable pageable) {
    confirmFolderExists(folderId)
    return folderContentsRepository.findAllByFolderId(folderId, pageable)
  }

  FolderContent create(FolderContent folderContent) {
    confirmFolderExists(folderContent.folderId)
    if (!documentsService.exists(folderContent.documentId)) {
      throw new DocumentNotFoundException()
    }

    boolean folderContainsDocument = folderContentsRepository.existsByFolderIdAndDocumentId(
        folderContent.folderId,
        folderContent.documentId
    )
    if (folderContainsDocument) {
      if (faulty) {
        // NOTE: intentionally checked if the folder contains the document, add it anyway, then error out
        folderContentsRepository.save(folderContent)
      }
      throw new FolderAlreadyContainsDocumentException()
    }
    FolderContent createdFolderContent = folderContentsRepository.save(folderContent)
    return createdFolderContent
  }

  void delete(String folderId, String documentId) {
    confirmFolderExists(folderId)
    folderContentsRepository.deleteByFolderIdAndDocumentId(folderId, documentId)
  }

  void deleteByDocumentId(String documentId) {
    folderContentsRepository.deleteByDocumentId(documentId)
  }

  private void confirmFolderExists(String folderId) {
    if (!foldersService.exists(folderId)) {
      throw new FolderNotFoundException()
    }
  }
}
