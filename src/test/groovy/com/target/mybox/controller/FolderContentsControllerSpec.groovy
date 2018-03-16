package com.target.mybox.controller

import com.target.mybox.domain.FolderContent
import com.target.mybox.service.FolderContentsService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

class FolderContentsControllerSpec extends Specification {

  FolderContentsController folderContentsController = new FolderContentsController(
      folderContentsService: Mock(FolderContentsService)
  )

  String folderId = 'f1'
  String documentId = 'd1'

  void 'getFolderContents'() {

    given:
    Pageable pageable = new PageRequest(0, 10)
    List<FolderContent> expected = [new FolderContent()]

    when:
    List<FolderContent> actual = folderContentsController.getFolderContents(folderId, pageable)

    then:
    1 * folderContentsController.folderContentsService.getAllByFolder(folderId, pageable) >> new PageImpl<>(expected)
    0 * _

    actual == expected
  }

  void 'postFolderContent'() {

    given:
    FolderContent expected = new FolderContent()

    when:
    FolderContent actual = folderContentsController.postFolderContent(folderId, documentId)

    then:
    1 * folderContentsController.folderContentsService.create({
      it.folderId == folderId && it.documentId == documentId
    }) >> expected
    0 * _

    actual.is(expected)
  }

  void 'deleteFolderContent'() {

    when:
    folderContentsController.deleteFolderContent(folderId, documentId)

    then:
    1 * folderContentsController.folderContentsService.delete(folderId, documentId)
    0 * _
  }
}
