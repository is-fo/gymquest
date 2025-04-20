package org.example.repository

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import org.example.model.Exercise
import org.example.service.MongoService

class ExerciseRepository extends Repository {

    static final instance = new ExerciseRepository()

    private ExerciseRepository() {
        this.collection = MongoService.getDatabase().getCollection("exercises")
    }

    static ExerciseRepository getInstance() {
        return instance
    }

    def insertExercise(String category, String name, String description, List<String> equipment) {
        def result = insert(new Exercise(category, name, description, equipment))
        return result != null ? "Successfully inserted $category, $name" as String : "Failed inserting $category, $name" as String
    }

    def findByCategory(String category, Integer limit) {
        try {
            def filter = Filters.eq("category", category)
            def results =
                    (limit && limit > 0) ? collection.find(filter).limit(limit) : collection.find(filter)
            def list = []
            results.forEach {list << it}
            return list
        } catch (MongoException e) {
            println "MongoDB Error finding entity by category ${category}: ${e.message}"
            e.printStackTrace()
        } catch (Exception e) {
            println "Error finding entity by category ${category}: ${e.message}"
            e.printStackTrace()
        }
    }
}
