package com.target.mybox.service

import com.target.mybox.domain.FolderContent
import com.target.mybox.exception.FolderAlreadyContainsDocumentException
import com.target.mybox.repository.FolderContentsRepository
import spock.lang.Specification

class FolderContentsServiceMetaSpec extends Specification {

  FolderContentsService folderContentsService = new FolderContentsService(
      folderContentsRepository: Mock(FolderContentsRepository),
      foldersService: Mock(FoldersService),
      documentsService: Mock(DocumentsService),
      faulty: true
  )

  String folderId = 'f1'
  String documentId = 'd1'

  void 'create throws exception when document is already in the folder but adds it anyway'() {

    given:
    FolderContent folderContent = new FolderContent(folderId: folderId, documentId: documentId)

    when:
    folderContentsService.create(folderContent)

    then:
    1 * folderContentsService.foldersService.exists(folderId) >> true
    1 * folderContentsService.documentsService.exists(documentId) >> true
    1 * folderContentsService.folderContentsRepository.existsByFolderIdAndDocumentId(folderId, documentId) >> true
    1 * folderContentsService.folderContentsRepository.save(folderContent)
    0 * _

    thrown(FolderAlreadyContainsDocumentException)
  }
}
