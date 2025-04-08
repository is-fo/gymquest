package org.example.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document
import org.bson.types.ObjectId
import org.example.model.Entity

abstract class Repository<T extends Entity> {

    abstract MongoCollection<Document> collection

    def insert(T entity) {
        try {
            def result = collection.insertOne(entity.createDocument() as Document)
            def _id = result.getInsertedId().asObjectId().getValue().toString()
            entity.set_id(_id) //ska entiteten returneras istället?
            return _id
        } catch (Exception e) {
            println "Error insert entity: ${e.message}"
            e.printStackTrace()
        }
    }

    def findById(String id) {
        try {
            def filter = Filters.eq("_id", new ObjectId(id))
            def result = collection.find(filter).first()
            if (result == null) {
                println "No entity found with id: $id"
                return null
            }
            return result
        } catch (Exception e) {
            println "Error finding entity by id: ${e.message}"
        }
    }

}
