package com.target.mybox.controller

import com.target.mybox.domain.FolderContent
import com.target.mybox.exception.PageMustNotBeNegativeException
import com.target.mybox.exception.SizeMustBePositiveException
import com.target.mybox.service.FolderContentsService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import spock.lang.Specification
import spock.lang.Unroll

class FolderContentsControllerSpec extends Specification {

  FolderContentsController folderContentsController = new FolderContentsController(
      folderContentsService: Mock(FolderContentsService)
  )

  String folderId = 'f1'
  String documentId = 'd1'

  void 'getFolderContents'() {

    given:
    List<FolderContent> expected = [new FolderContent()]

    when:
    List<FolderContent> actual = folderContentsController.getFolderContents(folderId, 0, 5)

    then:
    1 * folderContentsController.folderContentsService.getAllByFolder(folderId, { Pageable pageable ->
      pageable.pageNumber == 0 && pageable.pageSize == 5
    }) >> new PageImpl<>(expected)
    0 * _

    actual == expected
  }

  void 'getFolderContents throws exception when page is negative using page=#page'() {

    when:
    folderContentsController.getFolderContents(folderId, page, 5)

    then:
    0 * _

    thrown(PageMustNotBeNegativeException)

    where:
    page | _
    -1   | _
    -2   | _
    -10  | _
  }

  @Unroll
  void 'getFolderContents throws exception when size is non-positive using size=#size'() {

    when:
    folderContentsController.getFolderContents(folderId, 0, size)

    then:
    0 * _

    thrown(SizeMustBePositiveException)

    where:
    size | _
    0    | _
    -1   | _
    -10  | _
  }

  void 'postFolderContent'() {

    given:
    FolderContent expected = new FolderContent()

    when:
    FolderContent actual = folderContentsController.postFolderContent(folderId, documentId)

    then:
    1 * folderContentsController.folderContentsService.create({
      it.folderId == folderId && it.documentId == documentId
    }) >> expected
    0 * _

    actual.is(expected)
  }

  void 'deleteFolderContent'() {

    when:
    folderContentsController.deleteFolderContent(folderId, documentId)

    then:
    1 * folderContentsController.folderContentsService.delete(folderId, documentId)
    0 * _
  }
}
