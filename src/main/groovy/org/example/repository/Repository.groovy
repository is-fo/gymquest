package org.example.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.types.ObjectId
import org.example.model.Entity

import java.nio.ByteBuffer

abstract class Repository<T extends Entity> {

    abstract MongoCollection<Document> collection

    def insert(T entity) {
        try {
            def result = collection.insertOne(entity.createDocument() as Document)
            def _id = result.getInsertedId().asObjectId().getValue().toString()
            entity.set_id(_id)
            return _id
        } catch (Exception e) {
            println "Error insert entity: ${e.message}"
            e.printStackTrace()
            return null
        }
    }

    def insertDoc(Document document) {
        try {
            def result = collection.insertOne(document)
            def insertedId = result.getInsertedId()?.asObjectId()?.getValue()?.toString()
            println "Document inserted with _id: $insertedId"
            return insertedId
        } catch (Exception e) {
            println "Error inserting document into ${collection.getNamespace().collectionName}: ${e.message}"
            e.printStackTrace()
            return null
        }
    }

    def findById(String id) {
        try {
            def filter = Filters.eq("_id", new ObjectId(id))
            def result = collection.find(filter).first()
            if (!result) {
                println "No entity found with id: $id"
                return null
            }
            return result
        } catch (Exception e) {
            println "Error finding entity by id: ${e.message}"
        }
    }

    def findByField(String field, Object value) {
        try {
            def filter = Filters.eq(field, value)
            return collection.find(filter).first()
        } catch (Exception e ) {
            println "Error finding by $field = $value: ${e.message}"
            e.printStackTrace()
            return null
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
        }
        catch (Exception e) {
            println "Error appending to field: $fieldName on id $id: ${e.message}"
            e.printStackTrace()
            return false
        }
    }

    def pushToNestedArray(String id, String arrayField, String matchField, Object matchValue, Map pushData) {
        try {
            def matchValueObjId = matchValue instanceof ObjectId ? matchValue : new ObjectId(matchValue.toString())
            def filter = Filters.and(
            Filters.eq("_id", new ObjectId(id)),
            Filters.elemMatch(arrayField, Filters.eq(matchField, matchValueObjId))
            )
            def update = Updates.push("${arrayField}.\$.sets", new Document(pushData))
            def result = collection.updateOne(filter, update)
            return result.modifiedCount > 0
        } catch (Exception e) {
            println "Error pushing to nested array: ${e.message}"
            e.printStackTrace()
            return false
        }
    }

    static def createDocument(Map fields) {
        return new Document(fields)
    }

    def getDocuments(Integer limit = 0) {
        try {
            def results =
                    limit > 0 ? collection.find().limit(limit) : collection.find()
            return results.collect()
        } catch (Exception e) {
            println "Error getting documents from ${collection.getNamespace().collectionName}: ${e.getMessage()}"
            e.printStackTrace()
        }
    }
}
