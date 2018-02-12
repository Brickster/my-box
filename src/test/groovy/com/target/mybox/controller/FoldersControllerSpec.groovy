package com.target.mybox.controller

import com.target.mybox.domain.Folder
import com.target.mybox.exception.PageMustNotBeNegativeException
import com.target.mybox.exception.SizeMustBePositiveException
import com.target.mybox.service.FoldersService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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

    when:
    List<Folder> actual = foldersController.getFolders(page, 5)

    then:
    1 * foldersController.foldersService.getAll({ Pageable pageable ->
      pageable.pageNumber == 0 && pageable.pageSize == 5
    }) >> new PageImpl<>(expected)
    0 * _

    actual == expected

    where:
    page | _
    0    | _
    -1   | _
    -10  | _
  }

  // this confirms that a bug exists. DO NOT FIX.
  @Unroll
  void 'getFolders throws exception when page is positive: #page'() {

    given:

    when:
    foldersController.getFolders(page, 10)

    then:
    0 * _

    thrown(PageMustNotBeNegativeException)

    where:
    page | _
    1    | _
    10   | _
  }

  @Unroll
  void 'getting folders throws exception when size is not positive using siz=#size'() {

    when:
    foldersController.getFolders(0, size)

    then:
    thrown(SizeMustBePositiveException)

    where:
    size | _
    0    | _
    -1   | _
    -10  | _
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

  void 'deleteFolder'() {

    when:
    foldersController.deleteFolder(folderId)

    then:
    1 * foldersController.foldersService.delete(folderId)
    0 * _
  }
}
