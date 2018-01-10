package com.target.mybox.service

import com.target.mybox.domain.Folder
import com.target.mybox.exception.FolderNotFoundException
import com.target.mybox.repository.FoldersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FoldersService {

  @Autowired
  FoldersRepository foldersRepository

  List<Folder> getAll() {
    return foldersRepository.findAll()
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
    if (!exists(folder.id)) {
      throw new FolderNotFoundException()
    }
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
