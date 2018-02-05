package com.target.mybox.controller

import com.target.mybox.IntegrationSpec
import com.target.mybox.domain.Document
import com.target.mybox.repository.DocumentsRepository
import org.springframework.beans.factory.annotation.Autowired

class DocumentControllerIntegrationSpec extends IntegrationSpec {

  @Autowired
  DocumentsController documentsController

  @Autowired
  DocumentsRepository documentsRepository

  void cleanup() {
    documentsRepository.deleteAll()
  }

  void 'postDocument'() {

    given:
    Document document = new Document(name: 'file.txt', text: 'the text')

    when:
    Document createdDocument = documentsController.postDocument(document)

    then:
    createdDocument.id
    createdDocument.name == document.name
    createdDocument.text == document.text
    createdDocument.created
    createdDocument.lastModified
  }
}
