package com.target.mybox.controller

import com.target.mybox.service.DocumentsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class DocumentsControllerMetaSpec extends Specification {

  DocumentsController documentsController = new DocumentsController(
      documentsService: Mock(DocumentsService),
      faulty: true
  )

  String documentId = 'd1'

  void 'deleteDocument'() {

    when:
    ResponseEntity<Void> response = documentsController.deleteDocument(documentId)

    then:
    1 * documentsController.documentsService.delete(documentId)
    0 * _

    response.statusCode == HttpStatus.GONE
    !response.body
  }
}
