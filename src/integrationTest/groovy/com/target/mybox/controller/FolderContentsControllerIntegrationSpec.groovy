package com.target.mybox.controller

import com.target.mybox.IntegrationSpec
import com.target.mybox.domain.Document
import com.target.mybox.domain.Folder
import com.target.mybox.domain.FolderContent
import com.target.mybox.repository.DocumentsRepository
import com.target.mybox.repository.FolderContentsRepository
import com.target.mybox.repository.FoldersRepository
import org.springframework.beans.factory.annotation.Autowired

class FolderContentsControllerIntegrationSpec extends IntegrationSpec {

  @Autowired
  FolderContentsController folderContentsController

  @Autowired
  FoldersRepository foldersRepository

  @Autowired
  DocumentsRepository documentsRepository

  @Autowired
  FolderContentsRepository folderContentsRepository

  Folder folder
  Document document

  void setup() {
    folder = foldersRepository.save(new Folder(name: 'test folder'))
    document = documentsRepository.save(new Document(name: 'test_doc.txt', text: 'text contents'))
  }

  void cleanup() {
    folderContentsRepository.deleteAll()
    documentsRepository.deleteAll()
    foldersRepository.deleteAll()
  }

  void 'postFolderContent'() {

    when:
    FolderContent folderContent = folderContentsController.postFolderContent(folder.id, document.id)

    then:
    folderContent.id
    folderContent.folderId == folder.id
    folderContent.documentId == document.id
    folderContent.created
  }
}
