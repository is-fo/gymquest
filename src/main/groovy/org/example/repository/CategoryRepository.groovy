package org.example.repository


import org.bson.Document
import org.example.service.MongoService

class CategoryRepository extends Repository {

    static final instance = new CategoryRepository()

    private CategoryRepository() {
        this.collection = MongoService.getDatabase().getCollection("categories")
    }

    static def getInstance() {
        return instance
    }

    def populateCategories() {
        def categories = ExerciseRepository.getInstance().collection.distinct("category", String).into([])

        categories.each {
            if (findByField("name", it)) {
                collection.insertOne(new Document("name", it))
                println "Added $it to categories-collection"
            }
        }
    }
}
