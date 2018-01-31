package com.target.mybox.controller

import com.target.mybox.domain.Document
import com.target.mybox.exception.PageMustBePositiveException
import com.target.mybox.service.DocumentsService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

class DocumentsControllerSpec extends Specification {

  DocumentsController documentsController = new DocumentsController(
      documentsService: Mock(DocumentsService)
  )

  String documentId = 'd1'

  void 'getDocuments'() {

    given:
    Pageable pageable = new PageRequest(1, 10)
    List<Document> expected = [new Document()]

    when:
    List<Document> actual = documentsController.getDocuments(pageable)

    then:
    1 * documentsController.documentsService.getAll(pageable) >> new PageImpl<>(expected)
    0 * _

    actual == expected
  }

  void 'getDocuments throws exception when page is negative'() {

    given:
    Pageable negativePage = Mock(Pageable)

    when:
    documentsController.getDocuments(negativePage)

    then:
    1 * negativePage.pageNumber >> -1
    thrown(PageMustBePositiveException)
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

  void 'deleteDocument'() {

    when:
    documentsController.deleteDocument(documentId)

    then:
    1 * documentsController.documentsService.delete(documentId)
    0 * _
  }
}
