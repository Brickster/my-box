package com.target.mybox.controller

import com.target.mybox.domain.Document
import com.target.mybox.exception.PageMustNotBeNegativeException
import com.target.mybox.exception.SizeMustBePositiveException
import com.target.mybox.service.DocumentsService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import spock.lang.Specification
import spock.lang.Unroll

class DocumentsControllerSpec extends Specification {

  DocumentsController documentsController = new DocumentsController(
      documentsService: Mock(DocumentsService)
  )

  String documentId = 'd1'

  void 'getDocuments'() {

    given:
    List<Document> expected = [new Document()]

    when:
    List<Document> actual = documentsController.getDocuments(1, 10)

    then:
    1 * documentsController.documentsService.getAll({ Pageable pageable ->
      pageable.pageNumber == 1 && pageable.pageSize == 10
    }) >> new PageImpl<>(expected)
    0 * _

    actual == expected
  }

  @Unroll
  void 'getDocuments throws exception when page is not greater than zero'() {

    when:
    documentsController.getDocuments(page, 10)

    then:
    0 * _
    thrown(PageMustNotBeNegativeException)

    where:
    page | _
    -1   | _
    -2   | _
    -10  | _
  }

  @Unroll
  void 'getDocuments throws exception when size is not positive'() {

    when:
    documentsController.getDocuments(0, size)

    then:
    0 * _
    thrown(SizeMustBePositiveException)

    where:
    size | _
    0    | _
    -1   | _
    -2   | _
    -10  | _
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
    documentsController.deleteDocument(documentId)

    then:
    1 * documentsController.documentsService.delete(documentId)
    0 * _
  }
}
