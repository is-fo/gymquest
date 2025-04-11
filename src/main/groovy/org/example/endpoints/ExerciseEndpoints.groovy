package org.example.endpoints

import io.javalin.Javalin
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
        }
    }
}
