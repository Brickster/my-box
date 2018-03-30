package com.target.mybox.controller

import com.target.mybox.service.FolderContentsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class FolderContentsControllerMetaSpec extends Specification {

  FolderContentsController folderContentsController = new FolderContentsController(
      folderContentsService: Mock(FolderContentsService),
      faulty: true
  )

  String folderId = 'f1'
  String documentId = 'd1'

  void 'deleteFolderContent'() {

    when:
    ResponseEntity<Void> response = folderContentsController.deleteFolderContent(folderId, documentId)

    then:
    1 * folderContentsController.folderContentsService.delete(folderId, documentId)
    0 * _

    response.statusCode == HttpStatus.GONE
    !response.body
  }
}
