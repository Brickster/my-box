package com.target.mybox.controller

import com.target.mybox.domain.Folder
import com.target.mybox.service.FoldersService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Unroll

class FoldersControllerSpec extends Specification {

  FoldersController foldersController = new FoldersController(
      foldersService: Mock(FoldersService)
  )

  String folderId = 'f1'

  @Unroll
  void 'getFolders'() {

    given:
    List<Folder> expected = [new Folder()]
    Pageable pageable = new PageRequest(0, 10)

    when:
    List<Folder> actual = foldersController.getFolders(pageable)

    then:
    1 * foldersController.foldersService.getAll(pageable) >> new PageImpl<>(expected)
    0 * _

    actual == expected
  }

  void 'getFolder'() {

    given:
    Folder expected = new Folder()

    when:
    Folder actual = foldersController.getFolder(folderId)

    then:
    1 * foldersController.foldersService.get(folderId) >> expected
    0 * _

    actual.is(expected)
  }

  void 'postFolder'() {

    given:
    Folder folder = new Folder()
    Folder createdFolder = new Folder()

    when:
    Folder actual = foldersController.postFolder(folder)

    then:
    1 * foldersController.foldersService.create(folder) >> createdFolder
    0 * _

    actual.is(createdFolder)
  }

  void 'putFolder'() {

    given:
    Folder folder = new Folder()
    Folder savedFolder = new Folder()

    when:
    Folder actual = foldersController.putFolder(folderId, folder)

    then:
    1 * foldersController.foldersService.update({ it.id == folderId }) >> savedFolder
    0 * _

    actual.is(savedFolder)
  }

  void 'patch folder'() {

    given:
    Folder folder = new Folder()
    Folder savedFolder = new Folder()

    when:
    Folder actual = foldersController.patchFolder(folderId, folder)

    then:
    1 * foldersController.foldersService.update({ it.id == folderId }) >> savedFolder
    0 * _

    actual.is(savedFolder)
  }

  void 'deleteFolder'() {

    when:
    ResponseEntity<Void> responseEntity = foldersController.deleteFolder(folderId)

    then:
    1 * foldersController.foldersService.delete(folderId)
    0 * _

    responseEntity.statusCode == HttpStatus.NO_CONTENT
    !responseEntity.body
  }
}
