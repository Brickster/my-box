package com.target.mybox

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import com.target.mybox.domain.Document
import com.target.mybox.domain.Folder
import com.target.mybox.domain.FolderContent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate

import java.time.Instant

class IssueReproductionsFunctionalSpec extends FunctionalSpec {

  @Autowired
  MongoTemplate mongoTemplate

  void cleanup() {
    ['folders', 'documents', 'folder_contents'].each { mongoTemplate.dropCollection(it) }
  }

  void 'issue 16: Dates lack millisecond precision when zero'() {

    given:
    String dateString = '2016-01-02T03:04:05.000Z'
    Instant date = Instant.ofEpochMilli(1451703845000)
    mongoTemplate.getCollection('folders').save(folderDbObject(new Folder(id: 'f1', name: 'folder', created: date, lastModified: date)))
    mongoTemplate.getCollection('documents').save(documentDbObject(new Document(id: 'd1', name: 'd1.txt', text: 'd1', created: date, lastModified: date)))
    mongoTemplate.getCollection('folder_contents').save(folderContentDbObject(new FolderContent(id: 'fc1', folderId: 'f1', documentId: 'd1', created: date)))

    when:
    Map<String, Object> folderJson = get('/folders/f1').body
    Map<String, Object> documentJson = get('/documents/d1').body
    List<Map<String, Object>> folderContentsJson = getList('/folders/f1/contents').body

    then:
    folderJson['created'] == dateString
    folderJson['last_modified'] == dateString
    documentJson['created'] == dateString
    documentJson['last_modified'] == dateString
    folderContentsJson.first()['created'] == dateString
  }

  private DBObject folderDbObject(Folder folder) {
    BasicDBObject dbObject = new BasicDBObject()
    dbObject.put('_id', folder.id)
    dbObject.put('name', folder.name)
    dbObject.put('created', Date.from(folder.created))
    dbObject.put('last_modified', Date.from(folder.lastModified))
    return dbObject
  }

  private DBObject documentDbObject(Document document) {
    BasicDBObject dbObject = new BasicDBObject()
    dbObject.put('_id', document.id)
    dbObject.put('name', document.name)
    dbObject.put('text', document.text)
    dbObject.put('created', Date.from(document.created))
    dbObject.put('last_modified', Date.from(document.lastModified))
    return dbObject
  }

  private DBObject folderContentDbObject(FolderContent folderContent) {
    BasicDBObject dbObject = new BasicDBObject()
    dbObject.put('_id', folderContent.id)
    dbObject.put('folder_id', folderContent.folderId)
    dbObject.put('document_id', folderContent.documentId)
    dbObject.put('created', Date.from(folderContent.created))
    return dbObject
  }
}
