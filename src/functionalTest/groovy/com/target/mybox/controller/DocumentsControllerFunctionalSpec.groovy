package com.target.mybox.controller

import com.target.mybox.FunctionalSpec
import com.target.mybox.domain.Document
import com.target.mybox.domain.FolderContent
import com.target.mybox.exception.PageMustNotBeNegativeException
import com.target.mybox.exception.SizeMustBePositiveException
import com.target.mybox.repository.DocumentsRepository
import com.target.mybox.repository.FolderContentsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Unroll

import javax.validation.ConstraintViolationException

class DocumentsControllerFunctionalSpec extends FunctionalSpec {

  @Autowired
  DocumentsRepository documentsRepository

  @Autowired
  FolderContentsRepository folderContentsRepository

  void cleanup() {
    documentsRepository.deleteAll()
    folderContentsRepository.deleteAll()
  }

  void 'getting documents returns no documents'() {

    when:
    ResponseEntity<List<Map<String, Object>>> response = getList('/documents')

    then:
    response.statusCode == HttpStatus.OK
    response.body != null
    response.body.size() == 0
  }

  void 'getting documents returns documents'() {

    given:
    Document document = new Document(id: 'd1', name: 'document.txt', text: 'the text', created: date1)
    documentsRepository.save([document])

    when:
    ResponseEntity<List<Map<String, Object>>> response = getList('/documents')

    then:
    response.statusCode == HttpStatus.OK
    response.body.size() == 1
    response.body.first().size() == 5
    response.body.first()['id'] == document.id
    response.body.first()['name'] == document.name
    response.body.first()['text'] == document.text
    response.body.first()['created'] == date1Iso
    response.body.first()['last_modified'] =~ ISO_FORMAT
  }

  @Unroll
  void 'getting documents returns documents with page=#page and size=#size'() {

    given:
    documentsRepository.save([
        new Document(id: 'd1', name: 'document1.txt', text: '1', created: date1),
        new Document(id: 'd2', name: 'document2.txt', text: '2', created: date1),
        new Document(id: 'd3', name: 'document3.txt', text: '3', created: date1),
        new Document(id: 'd4', name: 'document4.txt', text: '4', created: date1),
        new Document(id: 'd5', name: 'document5.txt', text: '5', created: date1),
        new Document(id: 'd6', name: 'document6.txt', text: '6', created: date1)
    ])

    when:
    ResponseEntity<List<Map<String, Object>>> response = getList('/documents', ['page': page, 'size': size])

    then:
    response.statusCode == HttpStatus.OK
    response.body.collect { it['id'] } == ids

    where:
    page | size | ids
    null | null | ['d1', 'd2', 'd3', 'd4', 'd5']
    0    | null | ['d1', 'd2', 'd3', 'd4', 'd5']
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
  void 'getting documents with a negative page returns an error using page=#page'() {

    when:
    ResponseEntity<Map<String, Object>> response = getError('/documents', ['page': page])

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
  void 'getting documents with a non-positive size returns an error using size=#size'() {

    when:
    ResponseEntity<Map<String, Object>> response = getError('/documents', ['size': size])

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

  void 'getting a document returns a document'() {

    given:
    Document document = new Document(id: 'd1', name: 'document.txt', text: 'the text', created: date1)
    documentsRepository.save([document])

    when:
    ResponseEntity<List<Map<String, Object>>> response = get("/documents/${document.id}")

    then:
    response.statusCode == HttpStatus.OK
    response.body.size() == 5
    response.body['id'] == document.id
    response.body['name'] == document.name
    response.body['text'] == document.text
    response.body['created'] == date1Iso
    response.body['last_modified'] =~ ISO_FORMAT
  }

  void 'creating a document creates and returns a document'() {

    given:
    Document document = new Document(name: 'document.txt', text: 'the text')

    when:
    ResponseEntity<Map<String, Object>> response = post('/documents', document)

    then:
    response.statusCode == HttpStatus.CREATED
    response.body.size() == 5
    response.body['id']
    response.body['name'] == document.name
    response.body['text'] == document.text
    response.body['created'] =~ ISO_FORMAT
    response.body['last_modified'] =~ ISO_FORMAT
  }

  void 'updating a document updates and returns a document'() {

    given:
    Document document = new Document(id: 'd1', name: 'document.txt', text: 'the text', created: date1)
    documentsRepository.save([document])
    Document updateDocument = new Document(name: 'document-new.txt', text: 'the new text')

    when:
    ResponseEntity<List<Map<String, Object>>> response = put("/documents/${document.id}", updateDocument)

    then:
    response.statusCode == HttpStatus.OK
    response.body.size() == 5
    response.body['id'] == document.id
    response.body['name'] == updateDocument.name
    response.body['text'] == updateDocument.text
    response.body['created'] == date1Iso
    response.body['last_modified'] =~ ISO_FORMAT

    Document repoDocument = documentsRepository.findOne(document.id)
    repoDocument.name == updateDocument.name
    repoDocument.text == updateDocument.text
  }

  void 'patching a document updates and returns a document'() {

    given:
    Document document = new Document(id: 'd1', name: 'document.txt', text: 'the text', created: date1)
    documentsRepository.save([document])
    Map<String, String> documentUpdate = [text: 'the new text']

    when:
    ResponseEntity<List<Map<String, Object>>> response = patch("/documents/${document.id}", documentUpdate)

    then:
    response.statusCode == HttpStatus.OK
    response.body.size() == 5
    response.body['id'] == document.id
    response.body['name'] == document.name
    response.body['text'] == documentUpdate['text']
    response.body['created'] == date1Iso
    response.body['last_modified'] =~ ISO_FORMAT

    documentsRepository.findOne(document.id).text == documentUpdate['text']
  }

  void 'patching a document returns error when removing required field'() {

    given:
    Document document = new Document(id: 'd1', name: 'document.txt', text: 'the text', created: date1)
    documentsRepository.save([document])
    Map<String, String> documentUpdate = [text: null]

    when:
    ResponseEntity<List<Map<String, Object>>> response = patch("/documents/${document.id}", documentUpdate)

    then:
    response.statusCode == HttpStatus.BAD_REQUEST
    response.body.size() == 2
    response.body['error'] == ConstraintViolationException.getSimpleName()
    response.body['message'] == 'text must not be blank'
  }

  void 'deleting a document deletes a document'() {

    given:
    Document document = new Document(id: 'd1', name: 'document.txt', text: 'the text', created: date1)
    documentsRepository.save([document])

    when:
    ResponseEntity<Map<String, Object>> response = delete("/documents/${document.id}")

    then: 'the document is deleted'
    response.statusCode == HttpStatus.GONE
    !response.body

    when: 'the document is retrieved'
    response = get("/documents/${document.id}")

    // this is an intentional bug
    then: 'it is found'
    response.statusCode == HttpStatus.OK
    response.body

    when: 'deleting again'
    response = delete("/documents/${document.id}")

    then: 'the document is still gone'
    response.statusCode == HttpStatus.GONE
    !response.body
  }

  void 'deleting a document removes if from all folders'() {

    given:
    Document document = new Document(id: 'd1', name: 'document.txt', text: 'the text', created: date1)
    documentsRepository.save([document])
    folderContentsRepository.save(new FolderContent(folderId: 'f1', documentId: document.id))
    folderContentsRepository.save(new FolderContent(folderId: 'f2', documentId: document.id))
    folderContentsRepository.save(new FolderContent(folderId: 'f1', documentId: 'd2'))

    when:
    delete("/documents/${document.id}")

    then:
    folderContentsRepository.count() == 1
  }
}
