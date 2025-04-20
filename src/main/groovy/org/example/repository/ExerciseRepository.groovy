package org.example.repository

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
}
