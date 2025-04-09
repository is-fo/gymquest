package org.example.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
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

    def updateRow(String id, String fieldName, Object newValue) {
        try {
            def filter = Filters.eq("_id", new ObjectId(id))
            def update = Updates.set(fieldName, newValue)
            def result = collection.updateOne(filter, update)

            println "Matched: ${result.matchedCount}, Modified: ${result.modifiedCount}"
            return result.modifiedCount > 0
        } catch (Exception e) {
            println "Error updating field: $fieldName on id $id: ${e.message}"
            e.printStackTrace()
            return false
        }
    }

    //otestad
    def appendToArray(String id, String fieldName, Object value) {
        try {
            def filter = Filters.eq("_id", new ObjectId(id))
            def update
            if (value instanceof List) {
                update = Updates.pushEach(fieldName, value)
            } else {
                update = Updates.push(fieldName, value)
            }
            def result = collection.updateOne(filter, update)
            println "Matched: ${result.matchedCount}, Modified: ${result.modifiedCount}"
            return result.modifiedCount > 0
        } catch (Exception e) {
            println "Error appending to field: $fieldName on id $id: ${e.message}"
            e.printStackTrace()
            return false
        }
    }
}
