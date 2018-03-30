package com.target.mybox.controller

import com.target.mybox.domain.FolderContent
import com.target.mybox.service.FolderContentsService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping('/folders/{folderId}/contents')
class FolderContentsController {

  @Value('${faulty}')
  boolean faulty

  @Autowired
  FolderContentsService folderContentsService

  @GetMapping
  List<FolderContent> getFolderContents(
      @PathVariable String folderId,
      @PageableDefault(size = 5, sort = 'created', direction = Sort.Direction.ASC) Pageable pageable
  ) {
    return folderContentsService.getAllByFolder(folderId, pageable).content
  }

  @PostMapping('/{documentId}')
  @ResponseStatus(HttpStatus.CREATED)
  FolderContent postFolderContent(@PathVariable String folderId, @PathVariable String documentId) {
    return folderContentsService.create(new FolderContent(folderId: folderId, documentId: documentId))
  }

  @DeleteMapping('/{documentId}')
  @ResponseStatus(HttpStatus.GONE)
  ResponseEntity<Void> deleteFolderContent(@PathVariable String folderId, @PathVariable String documentId) {
    folderContentsService.delete(folderId, documentId)
    new ResponseEntity<Void>(faulty ? HttpStatus.GONE : HttpStatus.NO_CONTENT)
  }
}
