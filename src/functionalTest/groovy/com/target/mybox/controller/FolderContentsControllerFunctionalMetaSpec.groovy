package com.target.mybox.controller

import com.target.mybox.FunctionalMetaSpec
import com.target.mybox.domain.Document
import com.target.mybox.domain.Folder
import com.target.mybox.domain.FolderContent
import com.target.mybox.exception.FolderAlreadyContainsDocumentException
import com.target.mybox.repository.DocumentsRepository
import com.target.mybox.repository.FolderContentsRepository
import com.target.mybox.repository.FoldersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class FolderContentsControllerFunctionalMetaSpec extends FunctionalMetaSpec {

  @Autowired
  FoldersRepository foldersRepository

  @Autowired
  DocumentsRepository documentsRepository

  @Autowired
  FolderContentsRepository folderContentsRepository

  void cleanup() {
    foldersRepository.deleteAll()
    documentsRepository.deleteAll()
    folderContentsRepository.deleteAll()
  }

  void 'putting a document in a folder puts the document in the folder'() {

    given:
    foldersRepository.save(new Folder(id: 'f1'))
    documentsRepository.save(new Document(id: 'd1'))

    when:
    ResponseEntity<List<Map<String, Object>>> response = post('/folders/f1/contents/d1')

    then:
    response.statusCode == HttpStatus.CREATED
    response.body.size() == 2
    response.body['document_id'] == 'd1'
    response.body['created'] =~ ISO_FORMAT
    folderContentsRepository.count() == 1

    when: 'putting again'
    response = post('/folders/f1/contents/d1')

    then: 'an error is returned and the document is still added'
    response.statusCode == HttpStatus.FORBIDDEN
    response.body.size() == 2
    response.body['error'] == FolderAlreadyContainsDocumentException.getSimpleName()
    response.body['message'] == 'Folder already contains document'
    folderContentsRepository.count() == 2
  }

  void 'removing a document from a folder removes the document from the folder'() {

    given:
    foldersRepository.save(new Folder(id: 'f1'))
    folderContentsRepository.save(new FolderContent(folderId: 'f1', documentId: 'd1'))

    when:
    ResponseEntity<Map<String, Object>> response = delete('/folders/f1/contents/d1')

    then:
    response.statusCode == HttpStatus.GONE
    !response.body
    folderContentsRepository.count() == 0
  }
}
