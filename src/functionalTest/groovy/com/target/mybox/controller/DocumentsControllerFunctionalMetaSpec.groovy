package com.target.mybox.controller

import com.target.mybox.FunctionalMetaSpec
import com.target.mybox.domain.Document
import com.target.mybox.repository.DocumentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class DocumentsControllerFunctionalMetaSpec extends FunctionalMetaSpec {

  @Autowired
  DocumentsRepository documentsRepository

  void cleanup() {
    documentsRepository.deleteAll()
  }

  void 'deleting a document deletes a document'() {

    given:
    Document document = new Document(id: 'd1', name: 'document.txt', text: 'the text', created: date1)
    documentsRepository.save([document])

    when:
    ResponseEntity<Map<String, Object>> response = delete("/documents/${document.id}")

    then: 'the document is deleted'
    response.statusCode == HttpStatus.GONE
    !response.body

    when: 'the document is retrieved'
    response = get("/documents/${document.id}")

    // this is intentional
    then: 'it is found'
    response.statusCode == HttpStatus.OK
    response.body

    when: 'deleting again'
    response = delete("/documents/${document.id}")

    then: 'the document is still gone'
    response.statusCode == HttpStatus.GONE
    !response.body
  }
}
