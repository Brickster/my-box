package com.target.mybox.controller

import com.target.mybox.annotation.PageParam
import com.target.mybox.annotation.SizeParam
import com.target.mybox.domain.Document
import com.target.mybox.exception.PageMustNotBeNegativeException
import com.target.mybox.exception.SizeMustBePositiveException
import com.target.mybox.service.DocumentsService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@CompileStatic
@RestController
@RequestMapping('/documents')
class DocumentsController {

  @Autowired
  DocumentsService documentsService

  @GetMapping
  List<Document> getDocuments(@PageParam int page, @SizeParam int size) {
    if (page < 0) {
      throw new PageMustNotBeNegativeException()
    }
    if (size < 1) {
      throw new SizeMustBePositiveException()
    }
    return documentsService.getAll(new PageRequest(page, size)).content
  }

  @GetMapping('/{documentId}')
  Document getDocument(@PathVariable String documentId) {
    return documentsService.get(documentId)
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Document postDocument(@RequestBody @Valid Document document) {
    return documentsService.create(document)
  }

  @PutMapping('/{documentId}')
  @ResponseStatus(HttpStatus.OK)
  Document putDocument(@PathVariable String documentId, @RequestBody @Valid Document document) {
    document.id = documentId
    return documentsService.update(document)
  }

  @PatchMapping('/{documentId}')
  @ResponseStatus(HttpStatus.OK)
  Document patchDocument(@PathVariable String documentId, @RequestBody Map<String, String> document) {
    return documentsService.update(documentId, document)
  }

  @DeleteMapping('/{documentId}')
  @ResponseStatus(HttpStatus.GONE)
  void deleteDocument(@PathVariable String documentId) {
    documentsService.delete(documentId)
  }
}
