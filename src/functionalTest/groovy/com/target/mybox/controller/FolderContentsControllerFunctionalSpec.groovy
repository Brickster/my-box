package com.target.mybox.controller

import com.target.mybox.FunctionalSpec
import com.target.mybox.domain.Document
import com.target.mybox.domain.Folder
import com.target.mybox.domain.FolderContent
import com.target.mybox.exception.FolderAlreadyContainsDocumentException
import com.target.mybox.exception.PageMustNotBeNegativeException
import com.target.mybox.exception.SizeMustBePositiveException
import com.target.mybox.repository.DocumentsRepository
import com.target.mybox.repository.FolderContentsRepository
import com.target.mybox.repository.FoldersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Unroll

import java.time.Instant

class FolderContentsControllerFunctionalSpec extends FunctionalSpec {

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

  void 'getting folder contents returns the folder contents'() {

    given:
    foldersRepository.save(new Folder(id: 'f1'))
    folderContentsRepository.save([
        new FolderContent(folderId: 'f1', documentId: 'd1'),
        new FolderContent(folderId: 'f1', documentId: 'd2'),
        new FolderContent(folderId: 'f1', documentId: 'd3'),
        new FolderContent(folderId: 'f2', documentId: 'd1'),
        new FolderContent(folderId: 'f2', documentId: 'd2')
    ])

    when:
    ResponseEntity<List<Map<String, Object>>> response = getList('/folders/f1/contents')

    then:
    response.statusCode == HttpStatus.OK
    response.body.size() == 3
    response.body.collect { it['document_id'] }.sort() == ['d1', 'd2', 'd3']
    response.body.every { it['created'] =~ ISO_FORMAT }
  }

  @Unroll
  void 'getting documents returns documents with page=#page and size=#size'() {

    given:
    foldersRepository.save(new Folder(id: 'f1'))
    folderContentsRepository.save([
        new FolderContent(folderId: 'f1', documentId: 'd1'),
        new FolderContent(folderId: 'f1', documentId: 'd2'),
        new FolderContent(folderId: 'f1', documentId: 'd3'),
        new FolderContent(folderId: 'f1', documentId: 'd4'),
        new FolderContent(folderId: 'f1', documentId: 'd5'),
        new FolderContent(folderId: 'f1', documentId: 'd6')
    ])

    when:
    ResponseEntity<List<Map<String, Object>>> response = getList('/folders/f1/contents', ['page': page, 'size': size])

    then:
    response.statusCode == HttpStatus.OK
    response.body.collect { it['document_id'] } == ids

    where:
    page | size | ids
    null | null | ['d1', 'd2', 'd3', 'd4', 'd5']
    0    | null | ['d1', 'd2', 'd3', 'd4', 'd5']
    null | 5    | ['d1', 'd2', 'd3', 'd4', 'd5']
    null | 6    | ['d1', 'd2', 'd3', 'd4', 'd5', 'd6']
    null | 7    | ['d1', 'd2', 'd3', 'd4', 'd5', 'd6']
    null | 100  | ['d1', 'd2', 'd3', 'd4', 'd5', 'd6']
    1    | null | ['d6']
    null | 4    | ['d1', 'd2', 'd3', 'd4']
    0    | 4    | ['d1', 'd2', 'd3', 'd4']
    1    | 4    | ['d5', 'd6']
    2    | 4    | []
    100  | 4    | []
  }

  @Unroll
  void 'get folder contents sorted #description'() {

    given:
    Folder folder = foldersRepository.save(new Folder(name: 'docs'))
    FolderContent folderContent = new FolderContent(id: 'fc1', folderId: folder.id, documentId: 'd1', created: date1)
    FolderContent folderContent2 = new FolderContent(id: 'fc2', folderId: folder.id, documentId: 'd2', created: Instant.now())
    FolderContent folderContent3 = new FolderContent(id: 'fc3', folderId: folder.id, documentId: 'd3', created: date2)
    FolderContent folderContent4 = new FolderContent(id: 'fc4', folderId: 'other folder', documentId: 'd4', created: date1)
    folderContentsRepository.save([folderContent, folderContent2, folderContent3, folderContent4])

    when:
    ResponseEntity<List<Map<String, Object>>> response = getList("/folders/${folder.id}/contents", queryParameters)

    then:
    (response.body as List<Map<String, Object>>)*.get('document_id') == expertedOrder

    where:
    field     | direction            | expertedOrder      | description
    'created' | Direction.ASCENDING  | ['d1', 'd3', 'd2'] | 'ascending by created'
    'created' | Direction.DESCENDING | ['d2', 'd3', 'd1'] | 'descending by created'
    'blarg'   | Direction.ASCENDING  | ['d1', 'd2', 'd3'] | 'by insertion order'
    null      | null                 | ['d1', 'd3', 'd2'] | 'using default'

    queryParameters = field ? ['sort': direction.toQueryParameter(field)] : null
  }

  @Unroll
  void 'getting folder contents with a negative page returns an error using page=#page'() {

    when:
    ResponseEntity<Map<String, Object>> response = getError('/folders/f1/contents', ['page': page])

    then:
    response.statusCode == HttpStatus.BAD_REQUEST
    response.body.size() == 2
    response.body['error'] == PageMustNotBeNegativeException.getSimpleName()
    response.body['message'] == 'Page must not be negative'

    where:
    page | _
    -1   | _
    -2   | _
    -10  | _
  }

  @Unroll
  void 'getting folder contents with a non-positive size returns an error using size=#size'() {

    when:
    ResponseEntity<Map<String, Object>> response = getError('/folders/f1/contents', ['size': size])

    then:
    response.statusCode == HttpStatus.BAD_REQUEST
    response.body.size() == 2
    response.body['error'] == SizeMustBePositiveException.getSimpleName()
    response.body['message'] == 'Size must be positive'

    where:
    size | _
    0    | _
    -1   | _
    -2   | _
    -10  | _
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

    then: 'an error is returned and the document is not added'
    response.statusCode == HttpStatus.FORBIDDEN
    response.body.size() == 2
    response.body['error'] == FolderAlreadyContainsDocumentException.getSimpleName()
    response.body['message'] == 'Folder already contains document'
    folderContentsRepository.count() == 1
  }

  void 'removing a document from a folder removes the document from the folder'() {

    given:
    foldersRepository.save(new Folder(id: 'f1'))
    folderContentsRepository.save(new FolderContent(folderId: 'f1', documentId: 'd1'))

    when:
    ResponseEntity<Map<String, Object>> response = delete('/folders/f1/contents/d1')

    then:
    response.statusCode == HttpStatus.NO_CONTENT
    !response.body
    folderContentsRepository.count() == 0
  }
}
