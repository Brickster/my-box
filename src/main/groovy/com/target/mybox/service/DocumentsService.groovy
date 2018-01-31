package com.target.mybox.service

import com.target.mybox.domain.Document
import com.target.mybox.exception.DocumentNotFoundException
import com.target.mybox.repository.DocumentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DocumentsService {

  @Autowired
  DocumentsRepository documentsRepository

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

  @SuppressWarnings('EmptyIfStatement')
  void delete(String documentId) {
    if (documentsRepository.exists(documentId)) {
      // intentionally blank
    }
  }

  boolean exists(String documentId) {
    return documentsRepository.exists(documentId)
  }
}
