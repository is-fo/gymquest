package org.example.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.bson.Document
import org.bson.types.ObjectId
import org.example.model.Entity

class Repository<T extends Entity> {

    protected MongoCollection<Document> collection

    Repository(MongoDatabase database, String collectionName) {
        this.collection = database.getCollection(collectionName)
    }

    def insert(T entity) {
        try {
            def result = collection.insertOne(entity.createDocument() as Document)
            def _id = result.getInsertedId().asObjectId().getValue().toString()
            entity.set_id(_id)
            return _id
        } catch (Exception e) {
            println "Error insert entity: ${e.message}"
        }
    }

    def findById(String id) {
        try {
            def filter = Filters.eq("_id", new ObjectId(id))
            def result = collection.find(filter).first()
            return result
        } catch (Exception e) {
            println "Error finding entity by id: ${e.message}"
        }
    }
}
