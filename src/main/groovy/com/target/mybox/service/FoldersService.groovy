package com.target.mybox.service

import com.target.mybox.domain.Folder
import com.target.mybox.exception.FolderNotFoundException
import com.target.mybox.repository.FoldersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

//@CompileStatic
@Service
class FoldersService {

  @Autowired
  FoldersRepository foldersRepository

  Page<Folder> getAll(Pageable pageable) {
    return foldersRepository.findAll(pageable)
  }

  Folder get(String folderId) {
    Folder folder = foldersRepository.findOne(folderId)
    if (!folder) {
      throw new FolderNotFoundException()
    }
    return folder
  }

  Folder create(Folder folder) {
    return foldersRepository.save(folder)
  }

  Folder update(Folder folder) {
    Folder existingFolder = foldersRepository.findOne(folder.id)
    if (!existingFolder) {
      throw new FolderNotFoundException()
    }
    folder.created = existingFolder.created
    return foldersRepository.save(folder)
  }

  void delete(String folderId) {
    if (foldersRepository.exists(folderId)) {
      foldersRepository.delete(folderId)
    }
  }

  boolean exists(String folderId) {
    return foldersRepository.exists(folderId)
  }
}
