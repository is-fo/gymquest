package org.example.api

import groovy.json.JsonOutput
import io.javalin.Javalin
import org.example.model.User
import org.example.repository.ExerciseRepository
import org.example.repository.UserRepository

class Endpoint {
    static final instance = new Endpoint()

    private Endpoint() {}

    static Endpoint getInstance() {
        return instance
    }

    static def init() {
        def app = Javalin.create().start(8080)

        app.get("/user/{username}") {
            def username = it.pathParam("username")
            def user = User.createUser(UserRepository.getInstance().findByUsername(username))

            if (user != null) {
                def jsonResponse = JsonOutput.toJson(user)
                it.contentType("application/json").result(jsonResponse)
            } else {
                it.status(404).result("User not found")
            }
        }

        app.post("/login") {
            def body = it.bodyAsClass(Map)
            println body.toString()
            def username = body.username
            def password = body.password

            def result = UserHandler.getInstance().login(username as String, password as String)

            it.json([status: "ok", token: result ])
        }

        app.post("/register") {
            def body = it.bodyAsClass(Map)

            def username = body.username
            def password = body.password

            def result = UserHandler.getInstance().registerUser(username as String, password as String)

            it.json([status: "ok", token: result])
        }

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
