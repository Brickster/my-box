package com.target.mybox.service

import com.target.mybox.domain.Document
import com.target.mybox.repository.DocumentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DocumentsService {

  @Autowired
  DocumentsRepository documentsRepository

  List<Document> getAll() {
    return documentsRepository.findAll()
  }

  Document get(String documentId) {
    return documentsRepository.findOne(documentId)
  }

  Document create(Document document) {
    return documentsRepository.save(document)
  }

  Document update(Document document) {
    return documentsRepository.save(document)
  }

  void delete(String documentId) {
    if (documentsRepository.exists(documentId)) {
      documentsRepository.delete(documentId)
    }
  }
}
