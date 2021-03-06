package com.target.mybox.controller

import com.target.mybox.domain.Document
import com.target.mybox.service.DocumentsService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class DocumentsControllerSpec extends Specification {

  DocumentsController documentsController = new DocumentsController(
      documentsService: Mock(DocumentsService)
  )

  String documentId = 'd1'

  void 'getDocuments'() {

    given:
    List<Document> expected = [new Document()]
    Pageable pageable = new PageRequest(0, 10)

    when:
    List<Document> actual = documentsController.getDocuments(pageable)

    then:
    1 * documentsController.documentsService.getAll(pageable) >> new PageImpl<>(expected)
    0 * _

    actual == expected
  }

  void 'getDocument'() {

    given:
    Document expected = new Document()

    when:
    Document actual = documentsController.getDocument(documentId)

    then:
    1 * documentsController.documentsService.get(documentId) >> expected
    0 * _

    actual.is(expected)
  }

  void 'postDocument'() {

    given:
    Document document = new Document()
    Document savedDocument = new Document()

    when:
    Document actual = documentsController.postDocument(document)

    then:
    1 * documentsController.documentsService.create(document) >> savedDocument
    0 * _

    actual.is(savedDocument)
  }

  void 'putDocument'() {

    given:
    Document documentUpdate = new Document()
    Document savedDocument = new Document()

    when:
    Document actual = documentsController.putDocument(documentId, documentUpdate)

    then:
    1 * documentsController.documentsService.update({ it.id == documentId }) >> savedDocument
    0 * _

    actual.is(savedDocument)
  }

  void 'patch document'() {

    given:
    Map<String, String> documentUpdate = [:]
    Document expected = new Document()

    when:
    Document patchedDocument = documentsController.patchDocument(documentId, documentUpdate)

    then:
    1 * documentsController.documentsService.update(documentId, documentUpdate) >> expected
    0 * _

    patchedDocument.is(expected)
  }

  void 'deleteDocument'() {

    when:
    ResponseEntity<Void> response = documentsController.deleteDocument(documentId)

    then:
    1 * documentsController.documentsService.delete(documentId)
    0 * _

    response.statusCode == HttpStatus.NO_CONTENT
    !response.body
  }
}
