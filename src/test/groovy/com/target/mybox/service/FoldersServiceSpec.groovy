package com.target.mybox.service

import com.target.mybox.domain.Folder
import com.target.mybox.exception.FolderNotFoundException
import com.target.mybox.repository.FoldersRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant

class FoldersServiceSpec extends Specification {

  FoldersService foldersService = new FoldersService(
      foldersRepository: Mock(FoldersRepository)
  )

  String folderId = 'f1'

  void 'getAll'() {

    given:
    Pageable pageable = new PageRequest(0, 10)
    List<Folder> folders = [new Folder()]

    when:
    Page<Folder> actual = foldersService.getAll(pageable)

    then:
    1 * foldersService.foldersRepository.findAll(pageable) >> new PageImpl<Folder>(folders)
    0 * _

    actual.content == folders
  }

  void 'get'() {

    given:
    Folder found = new Folder()

    when:
    Folder folder = foldersService.get(folderId)

    then:
    1 * foldersService.foldersRepository.findOne(folderId) >> found
    0 * _

    folder.is(found)
  }

  void 'get when folder is not found'() {

    when:
    foldersService.get(folderId)

    then:
    1 * foldersService.foldersRepository.findOne(folderId) >> null
    0 * _

    thrown(FolderNotFoundException)
  }

  void 'create'() {

    given:
    Folder folder = new Folder()
    Folder saved = new Folder()

    when:
    Folder actual = foldersService.create(folder)

    then:
    1 * foldersService.foldersRepository.save(folder) >> saved
    0 * _

    actual.is(saved)
  }

  void 'update'() {

    given:
    Folder folder = new Folder(id: folderId)
    Folder existing = new Folder(created: Instant.now())
    Folder saved = new Folder()

    when:
    Folder actual = foldersService.update(folder)

    then:
    1 * foldersService.foldersRepository.findOne(folderId) >> existing
    1 * foldersService.foldersRepository.save({ it.is(folder) && it.created == existing.created }) >> saved
    0 * _

    actual.is(saved)
  }

  void 'update when folder does not exist'() {

    given:
    Folder folder = new Folder(id: folderId)

    when:
    foldersService.update(folder)

    then:
    1 * foldersService.foldersRepository.findOne(folderId) >> null
    0 * _

    thrown(FolderNotFoundException)
  }

  @Unroll
  void 'delete'() {

    when:
    foldersService.delete(folderId)

    then:
    1 * foldersService.foldersRepository.exists(folderId) >> exists
    0 * _

    interaction {
      if (exists) {
        1 * foldersService.foldersRepository.delete(folderId)
      } else {
        0 * foldersService.foldersRepository.delete(_)
      }
    }

    where:
    exists | _
    true   | _
    false  | _
  }

  void 'exists'() {

    when:
    boolean exists = foldersService.exists(folderId)

    then:
    1 * foldersService.foldersRepository.exists(folderId) >> true
    0 * _

    exists
  }
}
