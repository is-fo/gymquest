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

        app.get("/exercises") {
            def limit = it.queryParam("limit")?.toInteger()
            def docs = ExerciseRepository.getInstance().getDocuments(limit)
            def list = []
            docs.forEach { list << it }

            it.json([exercises: list])
        }

        app.get("/categories") {
            def limit = it.queryParam("limit")?.toInteger()
            def docs = CategoryRepository.getInstance().getDocuments(limit) //om limit är null hanteras det i getDocuments()
            def list = []
            docs.forEach { list << it } //den här hanteringen kanske ska ligga i getDocuments() istället

            it.json([categories: list])
        }

    }
}
