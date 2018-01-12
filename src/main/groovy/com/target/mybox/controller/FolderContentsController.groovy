package com.target.mybox.controller

import com.target.mybox.domain.FolderContent
import com.target.mybox.exception.PageMustBePositiveException
import com.target.mybox.service.FolderContentsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping('/folders/{folderId}/contents')
class FolderContentsController {

  @Autowired
  FolderContentsService folderContentsService

  @GetMapping
  List<FolderContent> getFolderContents(
      @PathVariable String folderId,
      @PageableDefault(page = 0, size = 5) Pageable pageable
  ) {
    if (pageable.pageNumber < 0) {
      throw new PageMustBePositiveException()
    }
    return folderContentsService.getAllByFolder(folderId, pageable).content
  }

  @PostMapping('/{documentId}')
  FolderContent postFolderContent(@PathVariable String folderId, @PathVariable String documentId) {
    return folderContentsService.create(new FolderContent(folderId: folderId, documentId:  documentId))
  }

  @DeleteMapping('/{documentId}')
  @ResponseStatus(HttpStatus.GONE)
  void deleteFolderContent(@PathVariable String folderId, @PathVariable String documentId) {
    folderContentsService.delete(folderId, documentId)
  }
}
