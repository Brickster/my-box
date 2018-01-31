package com.target.mybox.repository

import com.target.mybox.domain.Folder
import org.springframework.data.mongodb.repository.MongoRepository

interface FoldersRepository extends MongoRepository<Folder, String> {

}
