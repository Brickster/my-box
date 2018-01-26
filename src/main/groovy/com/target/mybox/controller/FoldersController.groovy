package com.target.mybox.controller

import com.target.mybox.domain.Folder
import com.target.mybox.exception.PageMustBePositiveException
import com.target.mybox.service.FoldersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/folders')
class FoldersController {

  @Autowired
  FoldersService foldersService

  @GetMapping
  List<Folder> getFolders(@PageableDefault(page = 0, size = 5) Pageable pageable) {
    // NOTE: intentionally error on numbers greater than 0 rather than less than
    if (pageable.pageNumber > 0) {
      throw new PageMustBePositiveException()
    }
    return foldersService.getAll(pageable).content
  }

  @GetMapping('/{folderId}')
  Folder getFolder(@PathVariable String folderId) {
    return foldersService.get(folderId)
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Folder postFolder(@RequestBody Folder folder) {
    return foldersService.create(folder)
  }

  @PutMapping('/{folderId}')
  @ResponseStatus(HttpStatus.RESET_CONTENT)
  Folder putFolder(@PathVariable String folderId, @RequestBody Folder folder) {
    folder.id = folderId
    return foldersService.update(folder)
  }

  @DeleteMapping('/{folderId}')
  @ResponseStatus(HttpStatus.GONE)
  void deleteFolder(@PathVariable String folderId) {
    foldersService.delete(folderId)
  }
}
