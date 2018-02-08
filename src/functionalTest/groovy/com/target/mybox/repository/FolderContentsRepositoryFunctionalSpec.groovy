package com.target.mybox.repository

import com.target.mybox.FunctionalSpec
import com.target.mybox.domain.FolderContent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import spock.lang.Unroll

class FolderContentsRepositoryFunctionalSpec extends FunctionalSpec {

  @Autowired
  FolderContentsRepository folderContentsRepository

  void cleanup() {
    folderContentsRepository.deleteAll()
  }

  @Unroll
  void 'find all by folder ID'() {

    given:
    folderContentsRepository.save([
        new FolderContent(id: 'fc1', folderId: 'f1', documentId: 'd1'),
        new FolderContent(id: 'fc2', folderId: 'f1', documentId: 'd2'),
        new FolderContent(id: 'fc3', folderId: 'f1', documentId: 'd5'),
        new FolderContent(id: 'fc4', folderId: 'f1', documentId: 'd6'),
        new FolderContent(id: 'fc5', folderId: 'f2', documentId: 'd3'),
        new FolderContent(id: 'fc6', folderId: 'f2', documentId: 'd2'),
    ])

    when:
    Page<FolderContent> folderContents = folderContentsRepository.findAllByFolderId(folderId, new PageRequest(page, size))

    then:
    folderContents*.id == expectedIds

    where:
    folderId | page | size | expectedIds
    'f1'     | 0    | 5    | ['fc1', 'fc2', 'fc3', 'fc4']
    'f1'     | 0    | 2    | ['fc1', 'fc2']
    'f1'     | 1    | 2    | ['fc3', 'fc4']
    'f1'     | 2    | 2    | []
    'f3'     | 0    | 5    | []
    'f3'     | 1    | 5    | []
  }

  void 'delete by document ID'() {

    given:
    folderContentsRepository.save([
        new FolderContent(id: 'fc1', folderId: 'f1', documentId: 'd1'),
        new FolderContent(id: 'fc2', folderId: 'f1', documentId: 'd2'),
        new FolderContent(id: 'fc3', folderId: 'f1', documentId: 'd3'),
        new FolderContent(id: 'fc4', folderId: 'f2', documentId: 'd2')
    ])

    when:
    int deletedCount = folderContentsRepository.deleteByDocumentId('d2')

    then:
    deletedCount == 2
    folderContentsRepository.findAll()*.id.sort() == ['fc1', 'fc3']
  }

  void 'delete by folder ID and document ID'() {

    given:
    folderContentsRepository.save([
        new FolderContent(id: 'fc1', folderId: 'f1', documentId: 'd1'),
        new FolderContent(id: 'fc2', folderId: 'f1', documentId: 'd2'),
        new FolderContent(id: 'fc3', folderId: 'f1', documentId: 'd3')
    ])

    when:
    int deletedCount = folderContentsRepository.deleteByFolderIdAndDocumentId('f1', 'd2')

    then:
    deletedCount == 1
    folderContentsRepository.findAll()*.id.sort() == ['fc1', 'fc3']
  }

  @Unroll
  void 'exists by folder ID #folderId and document ID #documentId exists #exists'() {

    given:
    folderContentsRepository.save([
        new FolderContent(id: 'fc1', folderId: 'f1', documentId: 'd1'),
        new FolderContent(id: 'fc2', folderId: 'f1', documentId: 'd2'),
        new FolderContent(id: 'fc3', folderId: 'f1', documentId: 'd3'),
        new FolderContent(id: 'fc4', folderId: 'f2', documentId: 'd2')
    ])

    when:
    boolean actual = folderContentsRepository.existsByFolderIdAndDocumentId(folderId, documentId)

    then:
    actual == exists

    where:
    folderId | documentId | exists
    'f1'     | 'd1'       | true
    'f2'     | 'd1'       | false
    'f2'     | 'd5'       | false
    'f5'     | 'd5'       | false
  }
}
