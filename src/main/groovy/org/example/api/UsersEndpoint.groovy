package org.example.api

import groovy.json.JsonOutput
import io.javalin.Javalin
import org.example.model.User
import org.example.repository.UserRepository

class UsersEndpoint {
    static final instance = new UsersEndpoint()

    private UsersEndpoint() {}

    static UsersEndpoint getInstance() {
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
    }
}
