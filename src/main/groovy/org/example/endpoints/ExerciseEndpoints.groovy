package org.example.endpoints

import io.javalin.Javalin
import org.example.repository.CategoryRepository
import org.example.repository.ExerciseRepository

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

    }
}
