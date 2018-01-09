package com.target.mybox.service

import com.target.mybox.domain.Folder
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
    return foldersRepository.findOne(folderId)
  }

  Folder create(Folder folder) {
    return foldersRepository.save(folder)
  }

  Folder update(Folder folder) {
    return foldersRepository.save(folder)
  }

  void delete(String folderId) {
    if (foldersRepository.exists(folderId)) {
      foldersRepository.delete(folderId)
    }
  }
}
