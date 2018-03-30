package com.target.mybox.service

import com.target.mybox.domain.FolderContent
import com.target.mybox.exception.DocumentNotFoundException
import com.target.mybox.exception.FolderAlreadyContainsDocumentException
import com.target.mybox.exception.FolderNotFoundException
import com.target.mybox.repository.FolderContentsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

class FolderContentsServiceSpec extends Specification {

  FolderContentsService folderContentsService = new FolderContentsService(
      folderContentsRepository: Mock(FolderContentsRepository),
      foldersService: Mock(FoldersService),
      documentsService: Mock(DocumentsService)
  )

  String folderId = 'f1'
  String documentId = 'd1'

  void 'getAllByFolder'() {

    given:
    Pageable pageable = new PageRequest(0, 10)
    Page<FolderContent> folderContents = new PageImpl<>([new FolderContent()])

    when:
    Page<FolderContent> actual = folderContentsService.getAllByFolder(folderId, pageable)

    then:
    1 * folderContentsService.foldersService.exists(folderId) >> true
    1 * folderContentsService.folderContentsRepository.findAllByFolderId(folderId, pageable) >> folderContents
    0 * _

    actual.is(folderContents)
  }

  void 'getAllByFolder when folder does not exist'() {

    when:
    folderContentsService.getAllByFolder(folderId, new PageRequest(0, 10))

    then:
    1 * folderContentsService.foldersService.exists(folderId) >> false
    0 * _

    thrown(FolderNotFoundException)
  }

  void 'create'() {

    given:
    FolderContent folderContent = new FolderContent(folderId: folderId, documentId: documentId)
    FolderContent saved = new FolderContent()

    when:
    FolderContent actual = folderContentsService.create(folderContent)

    then:
    1 * folderContentsService.foldersService.exists(folderId) >> true
    1 * folderContentsService.documentsService.exists(documentId) >> true
    1 * folderContentsService.folderContentsRepository.existsByFolderIdAndDocumentId(folderId, documentId) >> false
    1 * folderContentsService.folderContentsRepository.save(folderContent) >> saved
    0 * _

    actual.is(saved)
  }

  void 'create when folder does not exist'() {

    given:
    FolderContent folderContent = new FolderContent(folderId: folderId, documentId: documentId)

    when:
    folderContentsService.create(folderContent)

    then:
    1 * folderContentsService.foldersService.exists(folderId) >> false
    0 * _

    thrown(FolderNotFoundException)
  }

  void 'create when document does not exist'() {

    given:
    FolderContent folderContent = new FolderContent(folderId: folderId, documentId: documentId)

    when:
    folderContentsService.create(folderContent)

    then:
    1 * folderContentsService.foldersService.exists(folderId) >> true
    1 * folderContentsService.documentsService.exists(documentId) >> false
    0 * _

    thrown(DocumentNotFoundException)
  }

  void 'create throws exception when document is already in the folder'() {

    given:
    FolderContent folderContent = new FolderContent(folderId: folderId, documentId: documentId)

    when:
    folderContentsService.create(folderContent)

    then:
    1 * folderContentsService.foldersService.exists(folderId) >> true
    1 * folderContentsService.documentsService.exists(documentId) >> true
    1 * folderContentsService.folderContentsRepository.existsByFolderIdAndDocumentId(folderId, documentId) >> true
    0 * _

    thrown(FolderAlreadyContainsDocumentException)
  }

  void 'delete'() {

    when:
    folderContentsService.delete(folderId, documentId)

    then:
    1 * folderContentsService.foldersService.exists(folderId) >> true
    1 * folderContentsService.folderContentsRepository.deleteByFolderIdAndDocumentId(folderId, documentId)
    0 * _
  }

  void 'delete when folder does not exist'() {

    when:
    folderContentsService.delete(folderId, documentId)

    then:
    1 * folderContentsService.foldersService.exists(folderId) >> null
    0 * _

    thrown(FolderNotFoundException)
  }

  void 'delete by document ID'() {

    when:
    folderContentsService.deleteByDocumentId(documentId)

    then:
    1 * folderContentsService.folderContentsRepository.deleteByDocumentId(documentId)
    0 * _
  }
}
