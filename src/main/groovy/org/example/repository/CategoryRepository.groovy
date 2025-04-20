package org.example.repository

import com.mongodb.client.model.Filters
import org.bson.Document
import org.example.service.MongoService

class CategoryRepository extends Repository{

    static final instance = new CategoryRepository()

    private CategoryRepository() {
        this.collection = MongoService.getDatabase().getCollection("categories")
    }

    static def getInstance() {
        return instance
    }

    def populateCategories() {
        println("Populating category-collection")
        def categories = ExerciseRepository.getInstance().collection.distinct("category", String).into([])

        categories.each {
            if (!collection.find(Filters.eq("name", it)).first()) {
                collection.insertOne(new Document("name", it))
            }
        }
    }
}
