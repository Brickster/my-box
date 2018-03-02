package com.target.mybox.service

import com.target.mybox.domain.Document
import com.target.mybox.exception.DocumentNotFoundException
import com.target.mybox.repository.DocumentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.Validator

//@CompileStatic
@Service
class DocumentsService {

  @Autowired
  DocumentsRepository documentsRepository

  @Autowired
  FolderContentsService folderContentsService

  Validator validator = Validation.buildDefaultValidatorFactory().getValidator()

  Page<Document> getAll(Pageable pageable) {
    return documentsRepository.findAll(pageable)
  }

  Document get(String documentId) {
    Document document = documentsRepository.findOne(documentId)
    if (!document) {
      throw new DocumentNotFoundException()
    }
    return document
  }

  Document create(Document document) {
    return documentsRepository.save(document)
  }

  Document update(Document document) {
    Document existingDocument = documentsRepository.findOne(document.id)
    if (!existingDocument) {
      throw new DocumentNotFoundException()
    }
    document.created = existingDocument.created
    return documentsRepository.save(document)
  }

  Document update(String documentId, Map<String, String> documentUpdate) {
    Document document = documentsRepository.findOne(documentId)
    if (!document) {
      throw new DocumentNotFoundException()
    }

    if (documentUpdate.containsKey(Document.NAME)) {
      document.name = documentUpdate[Document.NAME]
    }
    if (documentUpdate.containsKey(Document.TEXT)) {
      document.text = documentUpdate[Document.TEXT]
    }

    Set<ConstraintViolation<Document>> violations = validator.validate(document)
    if (violations) {
      throw new ConstraintViolationException(violations)
    }
    return documentsRepository.save(document)
  }

  @SuppressWarnings('EmptyIfStatement')
  void delete(String documentId) {
    if (documentsRepository.exists(documentId)) {
      // do not delete the document itself
      folderContentsService.deleteByDocumentId(documentId)
    }
  }

  boolean exists(String documentId) {
    return documentsRepository.exists(documentId)
  }
}
