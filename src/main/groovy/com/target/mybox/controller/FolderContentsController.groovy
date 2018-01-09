package com.target.mybox.controller

import com.target.mybox.domain.FolderContent
import com.target.mybox.service.FolderContentsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping('/folders/{folderId}/contents')
class FolderContentsController {

  @Autowired
  FolderContentsService folderContentsService

  @GetMapping
  List<FolderContent> getFolderContents(@PathVariable String folderId) {
    return folderContentsService.getAllByFolder(folderId)
  }

  @PostMapping('/{documentId}')
  FolderContent postFolderContent(@PathVariable String folderId, @PathVariable String documentId) {
    return folderContentsService.create(new FolderContent(folderId: folderId, documentId:  documentId))
  }

  @DeleteMapping('/{documentId}')
  void deleteFolderContent(@PathVariable String folderId, @PathVariable String documentId) {
    folderContentsService.delete(folderId, documentId)
  }
}
