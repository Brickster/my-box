package com.target.mybox.controller

import com.target.mybox.annotation.PageParam
import com.target.mybox.annotation.SizeParam
import com.target.mybox.domain.FolderContent
import com.target.mybox.exception.PageMustNotBeNegativeException
import com.target.mybox.exception.SizeMustBePositiveException
import com.target.mybox.service.FolderContentsService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
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

  @Autowired
  FolderContentsService folderContentsService

  @GetMapping
  List<FolderContent> getFolderContents(@PathVariable String folderId, @PageParam int page, @SizeParam int size) {
    if (page < 0) {
      throw new PageMustNotBeNegativeException()
    }
    if (size < 1) {
      throw new SizeMustBePositiveException()
    }
    return folderContentsService.getAllByFolder(folderId, new PageRequest(page, size)).content
  }

  @PostMapping('/{documentId}')
  @ResponseStatus(HttpStatus.CREATED)
  FolderContent postFolderContent(@PathVariable String folderId, @PathVariable String documentId) {
    return folderContentsService.create(new FolderContent(folderId: folderId, documentId: documentId))
  }

  @DeleteMapping('/{documentId}')
  @ResponseStatus(HttpStatus.GONE)
  void deleteFolderContent(@PathVariable String folderId, @PathVariable String documentId) {
    folderContentsService.delete(folderId, documentId)
  }
}
