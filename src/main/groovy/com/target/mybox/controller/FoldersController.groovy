package com.target.mybox.controller

import com.target.mybox.annotation.PageParam
import com.target.mybox.annotation.SizeParam
import com.target.mybox.domain.Folder
import com.target.mybox.exception.PageMustNotBeNegativeException
import com.target.mybox.exception.SizeMustBePositiveException
import com.target.mybox.service.FoldersService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
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

import javax.validation.Valid

@CompileStatic
@RestController
@RequestMapping('/folders')
class FoldersController {

  @Autowired
  FoldersService foldersService

  @GetMapping
  List<Folder> getFolders(@PageParam int page, @SizeParam int size) {
    // NOTE: intentionally error on numbers greater than 0 rather than less than
    if (page > 0) {
      throw new PageMustNotBeNegativeException()
    }
    if (size < 1) {
      throw new SizeMustBePositiveException()
    }
    return foldersService.getAll(new PageRequest(0, size)).content
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

  @DeleteMapping('/{folderId}')
  @ResponseStatus(HttpStatus.GONE)
  void deleteFolder(@PathVariable String folderId) {
    foldersService.delete(folderId)
  }
}
