package com.target.mybox.controller

import com.target.mybox.domain.Folder
import com.target.mybox.service.FoldersService
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
@RequestMapping('/folders')
class FoldersController {

  @Autowired
  FoldersService foldersService

  @GetMapping
  List<Folder> getFolders() {
    return foldersService.getAll()
  }

  @GetMapping('/{folderId}')
  Folder getFolder(@PathVariable String folderId) {
    return foldersService.get(folderId)
  }

  @PostMapping
  Folder postFolder(@RequestBody Folder folder) {
    return foldersService.create(folder)
  }

  @PutMapping('/{folderId}')
  Folder putFolder(@PathVariable String folderId, @RequestBody Folder folder) {
    folder.id = folderId
    return foldersService.update(folder)
  }

  @DeleteMapping('/{folderId')
  void deleteFolder(@PathVariable String folderId) {
    foldersService.delete(folderId)
  }
}
