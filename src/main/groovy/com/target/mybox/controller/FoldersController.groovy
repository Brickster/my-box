package com.target.mybox.controller

import com.target.mybox.domain.Folder
import com.target.mybox.service.FoldersService
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
@RequestMapping('/folders')
class FoldersController {

  @Value('${faulty}')
  boolean faulty

  @Autowired
  FoldersService foldersService

  @GetMapping
  List<Folder> getFolders(
      @PageableDefault(size = 5, sort = 'name', direction = Sort.Direction.ASC) Pageable pageable
  ) {
    return foldersService.getAll(pageable).content
  }

  @GetMapping('/{folderId}')
  Folder getFolder(@PathVariable String folderId) {
    return foldersService.get(folderId)
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Folder postFolder(@RequestBody @Valid Folder folder) {
    return foldersService.create(folder)
  }

  @PutMapping('/{folderId}')
  @ResponseStatus(HttpStatus.OK)
  Folder putFolder(@PathVariable String folderId, @RequestBody @Valid Folder folder) {
    folder.id = folderId
    return foldersService.update(folder)
  }

  @PatchMapping('/{folderId}')
  @ResponseStatus(HttpStatus.OK)
  Folder patchFolder(@PathVariable String folderId, @RequestBody @Valid Folder folder) {
    return putFolder(folderId, folder)
  }

  @DeleteMapping('/{folderId}')
  @ResponseStatus(HttpStatus.GONE)
  ResponseEntity<Void> deleteFolder(@PathVariable String folderId) {
    foldersService.delete(folderId)
    new ResponseEntity<Void>(faulty ? HttpStatus.GONE : HttpStatus.NO_CONTENT)
  }
}
