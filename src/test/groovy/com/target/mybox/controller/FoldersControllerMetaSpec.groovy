package com.target.mybox.controller

import com.target.mybox.service.FoldersService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class FoldersControllerMetaSpec extends Specification {

  FoldersController foldersController = new FoldersController(
      foldersService: Mock(FoldersService),
      faulty: true
  )

  String folderId = 'f1'

  void 'deleteFolder'() {

    when:
    ResponseEntity<Void> responseEntity = foldersController.deleteFolder(folderId)

    then:
    1 * foldersController.foldersService.delete(folderId)
    0 * _

    responseEntity.statusCode == HttpStatus.GONE
    !responseEntity.body
  }
}
