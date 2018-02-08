package com.target.mybox.service

import com.target.mybox.domain.Document
import com.target.mybox.exception.DocumentNotFoundException
import com.target.mybox.repository.DocumentsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant

class DocumentsServiceSpec extends Specification {

  DocumentsService documentsService = new DocumentsService(
      documentsRepository: Mock(DocumentsRepository),
      folderContentsService: Mock(FolderContentsService)
  )

  String documentId = 'd1'

  void 'getAll'() {

    given:
    Pageable pageable = new PageRequest(0, 10)
    List<Document> documents = [new Document()]

    when:
    Page<Document> actual = documentsService.getAll(pageable)

    then:
    1 * documentsService.documentsRepository.findAll(pageable) >> new PageImpl<Document>(documents)
    0 * _

    actual.content == documents
  }

  void 'get'() {

    given:
    Document found = new Document()

    when:
    Document document = documentsService.get(documentId)

    then:
    1 * documentsService.documentsRepository.findOne(documentId) >> found
    0 * _

    document.is(found)
  }

  void 'get when document is not found'() {

    when:
    documentsService.get(documentId)

    then:
    1 * documentsService.documentsRepository.findOne(documentId) >> null
    0 * _

    thrown(DocumentNotFoundException)
  }

  void 'create'() {

    given:
    Document document = new Document()
    Document saved = new Document()

    when:
    Document actual = documentsService.create(document)

    then:
    1 * documentsService.documentsRepository.save(document) >> saved
    0 * _

    actual.is(saved)
  }

  void 'update'() {

    given:
    Document document = new Document(id: documentId)
    Document existing = new Document(created: Instant.now())
    Document saved = new Document()

    when:
    Document actual = documentsService.update(document)

    then:
    1 * documentsService.documentsRepository.findOne(documentId) >> existing
    1 * documentsService.documentsRepository.save({ it.is(document) && it.created == existing.created }) >> saved
    0 * _

    actual.is(saved)
  }

  void 'update when document does not exist'() {

    given:
    Document document = new Document(id: documentId)

    when:
    documentsService.update(document)

    then:
    1 * documentsService.documentsRepository.findOne(documentId) >> null
    0 * _

    thrown(DocumentNotFoundException)
  }

  // this confirms that a bug exists. DO NOT FIX.
  @Unroll
  void 'delete never deletes the document'() {

    when:
    documentsService.delete(documentId)

    then:
    1 * documentsService.documentsRepository.exists(documentId) >> exists
    deleteContentsInterations * documentsService.folderContentsService.deleteByDocumentId(documentId)
    0 * documentsService.documentsRepository.delete(documentId)  // delete should never happen. This is intentional.
    0 * _

    where:
    exists | deleteContentsInterations
    true   | 1
    false  | 0
  }

  void 'exists'() {

    when:
    boolean exists = documentsService.exists(documentId)

    then:
    1 * documentsService.documentsRepository.exists(documentId) >> true
    0 * _

    exists
  }
}
