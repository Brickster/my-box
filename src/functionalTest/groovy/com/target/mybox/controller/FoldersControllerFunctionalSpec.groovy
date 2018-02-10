package com.target.mybox.controller

import com.target.mybox.FunctionalSpec
import com.target.mybox.domain.Folder
import com.target.mybox.exception.FolderNotFoundException
import com.target.mybox.exception.PageMustNotBeNegativeException
import com.target.mybox.exception.SizeMustBePositiveException
import com.target.mybox.repository.FoldersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Unroll

class FoldersControllerFunctionalSpec extends FunctionalSpec {

  @Autowired
  FoldersRepository foldersRepository

  void cleanup() {
    foldersRepository.deleteAll()
  }

  void 'getting folders returns no folders'() {

    when:
    ResponseEntity<List<Map<String, Object>>> response = getList('/folders')

    then:
    response.statusCode == HttpStatus.OK
    response.body != null
    response.body.size() == 0
  }

  void 'getting folders returns folders'() {

    given:
    Folder folder = new Folder(id: 'f1', name: 'the folder', created: date1)
    foldersRepository.save([folder])

    when:
    ResponseEntity<List<Map<String, Object>>> response = getList('/folders')

    then:
    response.statusCode == HttpStatus.OK
    response.body.size() == 1
    response.body.first().size() == 4
    response.body.first()['id'] == folder.id
    response.body.first()['name'] == folder.name
    response.body.first()['created'] == date1Iso
    response.body.first()['last_modified'] =~ ISO_FORMAT
  }

  @Unroll
  void 'getting folders returns folders with page=#page and size=#size'() {

    given:
    foldersRepository.save([
        new Folder(id: 'f1', name: 'folder1', created: date1),
        new Folder(id: 'f2', name: 'folder2', created: date1),
        new Folder(id: 'f3', name: 'folder3', created: date1),
        new Folder(id: 'f4', name: 'folder4', created: date1),
        new Folder(id: 'f5', name: 'folder5', created: date1),
        new Folder(id: 'f6', name: 'folder6', created: date1)
    ])

    when:
    ResponseEntity<List<Map<String, Object>>> response = getList('/folders', ['page': page, 'size': size])

    then:
    response.statusCode == HttpStatus.OK
    response.body.collect { it['id'] } == ids

    where:
    page | size | ids
    null | null | ['f1', 'f2', 'f3', 'f4', 'f5']
    0    | null | ['f1', 'f2', 'f3', 'f4', 'f5']
    null | 5    | ['f1', 'f2', 'f3', 'f4', 'f5']
    null | 6    | ['f1', 'f2', 'f3', 'f4', 'f5', 'f6']
    null | 7    | ['f1', 'f2', 'f3', 'f4', 'f5', 'f6']
    null | 100  | ['f1', 'f2', 'f3', 'f4', 'f5', 'f6']
    null | 4    | ['f1', 'f2', 'f3', 'f4']
    0    | 4    | ['f1', 'f2', 'f3', 'f4']

    // negative cases that confirm a bug
    -1   | 5    | ['f1', 'f2', 'f3', 'f4', 'f5']
    -2   | 5    | ['f1', 'f2', 'f3', 'f4', 'f5']
    -10  | 5    | ['f1', 'f2', 'f3', 'f4', 'f5']
  }

  // this confirms that a bug exists. DO NOT FIX.
  @Unroll
  void 'getting folders with positive page returns error with page=#page'() {

    when:
    ResponseEntity<Map<String, Object>> response = getError('/folders', ['page': page])

    then:
    response.statusCode == HttpStatus.BAD_REQUEST
    response.body.size() == 2
    response.body['error'] == PageMustNotBeNegativeException.getSimpleName()
    response.body['message'] == 'Page must not be negative'

    where:
    page | _
    1    | _
    2    | _
    100  | _
  }

  @Unroll
  void 'getting folders with non-positive size returns error with size=#size'() {

    when:
    ResponseEntity<Map<String, Object>> response = getError('/folders', ['size': size])

    then:
    response.statusCode == HttpStatus.BAD_REQUEST
    response.body.size() == 2
    response.body['error'] == SizeMustBePositiveException.getSimpleName()
    response.body['message'] == 'Size must be positive'

    where:
    size | _
    -1   | _
    -2   | _
    -10  | _
  }

  void 'getting a folder returns a folder'() {

    given:
    Folder folder = new Folder(id: 'f1', name: 'the folder', created: date1)
    foldersRepository.save([folder])

    when:
    ResponseEntity<List<Map<String, Object>>> response = get("/folders/${folder.id}")

    then:
    response.statusCode == HttpStatus.OK
    response.body.size() == 4
    response.body['id'] == folder.id
    response.body['name'] == folder.name
    response.body['created'] == date1Iso
    response.body['last_modified'] =~ ISO_FORMAT
  }

  void 'creating a folder creates and returns a folder'() {

    given:
    Folder folder = new Folder(name: 'the folder')

    when:
    ResponseEntity<Map<String, Object>> response = post('/folders', folder)

    then:
    response.statusCode == HttpStatus.CREATED
    response.body.size() == 4
    response.body['id']
    response.body['name'] == folder.name
    response.body['created'] =~ ISO_FORMAT
    response.body['last_modified'] =~ ISO_FORMAT
  }

  void 'updating a folder updates and return a folder'() {

    given:
    Folder folder = new Folder(id: 'f1', name: 'the folder', created: date1)
    foldersRepository.save([folder])
    Folder updatedFolder = new Folder(name: 'new folder name')

    when:
    ResponseEntity<List<Map<String, Object>>> response = put("/folders/${folder.id}", updatedFolder)

    then:
    response.statusCode == HttpStatus.RESET_CONTENT
    response.body.size() == 4
    response.body['id'] == folder.id
    response.body['name'] == updatedFolder.name
    response.body['created'] == date1Iso
    response.body['last_modified'] =~ ISO_FORMAT
  }

  void 'deleting a folder deletes a folder'() {

    given:
    Folder folder = new Folder(id: 'f1', name: 'the folder', created: date1)
    foldersRepository.save([folder])

    when:
    ResponseEntity<Map<String, Object>> response = delete("/folders/${folder.id}")

    then: 'the folder is deleted'
    response.statusCode == HttpStatus.GONE
    !response.body

    when: 'the folder is retrieved'
    response = get("/folders/${folder.id}")

    then: 'it is not found'
    response.statusCode == HttpStatus.NOT_FOUND
    response.body['error'] == FolderNotFoundException.getSimpleName()
    response.body['message'] == 'Folder not found'

    when: 'deleting again'
    response = delete("/folders/${folder.id}")

    then: 'the document is still gone'
    response.statusCode == HttpStatus.GONE
    !response.body
  }
}
