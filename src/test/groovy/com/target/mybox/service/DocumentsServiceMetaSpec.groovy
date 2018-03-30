package com.target.mybox.service

import com.target.mybox.repository.DocumentsRepository
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.Validator

class DocumentsServiceMetaSpec extends Specification {

  DocumentsService documentsService = new DocumentsService(
      documentsRepository: Mock(DocumentsRepository),
      folderContentsService: Mock(FolderContentsService),
      validator: Mock(Validator),
      faulty: true
  )

  String documentId = 'd1'

  @Unroll
  void 'delete never deletes the document'() {

    when:
    documentsService.delete(documentId)

    then:
    1 * documentsService.documentsRepository.exists(documentId) >> exists
    deleteContentsInterations * documentsService.folderContentsService.deleteByDocumentId(documentId)
    0 * documentsService.documentsRepository.delete(documentId)
    0 * _

    where:
    exists | deleteContentsInterations
    true   | 1
    false  | 0
  }
}
