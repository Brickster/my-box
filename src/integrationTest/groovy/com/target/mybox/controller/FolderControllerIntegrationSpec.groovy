package com.target.mybox.controller

import com.target.mybox.IntegrationSpec
import com.target.mybox.domain.Folder
import com.target.mybox.repository.FoldersRepository
import org.springframework.beans.factory.annotation.Autowired

class FolderControllerIntegrationSpec extends IntegrationSpec {

  @Autowired
  FoldersController foldersController

  @Autowired
  FoldersRepository foldersRepository

  void cleanup() {
    foldersRepository.deleteAll()
  }

  void 'postFolder'() {

    given:
    Folder folder = new Folder(name: 'folder name')

    when:
    Folder createdFolder = foldersController.postFolder(folder)

    then:
    createdFolder.id
    createdFolder.name == folder.name
    createdFolder.created
    createdFolder.lastModified
  }
}
