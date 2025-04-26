package org.example.endpoints

import io.javalin.Javalin
import org.bson.Document
import org.example.repository.CategoryRepository
import org.example.repository.ExerciseRepository
import org.example.repository.WorkoutPlanRepository

class ExerciseEndpoints {
    static endpoints(Javalin app) {
        app.post("/exercises") {
            def body = it.bodyAsClass(Map)
            def category = body.category
            def name = body.name
            def description = body.description
            def equipment = body.equipment

            def result = ExerciseRepository.getInstance()
                    .insertExercise(category as String,
                            name as String,
                            description as String,
                            equipment as ArrayList<String>)

            it.json([result: result])

            CategoryRepository.getInstance().populateCategories()
        }

        app.get("/exercises/{category}") {
            def category = it.pathParam("category")
            def limit = it.queryParam("limit")?.toInteger()
            def docs = ExerciseRepository.getInstance().groupByCategory(category, limit)

            it.json([exercises: docs])
        }

        app.get("/categories") {
            def limit = it.queryParam("limit")?.toInteger()
            def docs = CategoryRepository.getInstance().getDocuments(limit) //om limit är null hanteras det i getDocuments()

            it.json([categories: docs])
        }

        app.post("/workout") {
            def body = it.bodyAsClass(Map)

            def name = body.name
            def exerciseNames = body.exercises

            if (!(name instanceof String) || name.trim().isEmpty()) {
                it.status(400).result("Invalid or missing 'name'")
                return
            }

            if (!(exerciseNames instanceof List) || exerciseNames.isEmpty() || !exerciseNames.every { it instanceof String && !it.trim().isEmpty() }) {
                it.status(400).result("Invalid or missing 'exercises'")
            }

            exerciseNames = exerciseNames as List<String>

            def foundExercises = ExerciseRepository.getInstance().findByNames(exerciseNames)
            if (foundExercises.size() < exerciseNames.size()) {
                it.status(400).result("One or more exercises in input not in database")
                return
            }
            Document workoutDoc = [
                    name: name,
                    exercises: foundExercises.withIndex().collect(
                            (exercise, idx) ->
                        [
                                exerciseId: exercise._id,
                                order: idx + 1,
                        ]
                    )
            ]
            WorkoutPlanRepository.getInstance().insertDoc(workoutDoc)
            it.status(201).result("Workout plan created")
        }

    }
}
