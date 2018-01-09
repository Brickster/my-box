package com.target.mybox.controller

import com.target.mybox.domain.Document
import com.target.mybox.service.DocumentsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/documents')
class DocumentsController {

  @Autowired
  DocumentsService documentsService

  @GetMapping
  List<Document> getDocuments() {
    return documentsService.getAll()
  }

  @GetMapping('/{documentId}')
  Document getDocument(@PathVariable String documentId) {
    return documentsService.get(documentId)
  }

  @PostMapping
  Document postDocument(@RequestBody Document document) {
    return documentsService.create(document)
  }

  @PutMapping('/{documentId}')
  Document putDocument(@PathVariable String documentId, @RequestBody Document document) {
    document.id = documentId
    return documentsService.update(document)
  }

  @DeleteMapping('/{documentId}')
  void deleteDocument(@PathVariable String documentId) {
    documentsService.delete(documentId)
  }
}