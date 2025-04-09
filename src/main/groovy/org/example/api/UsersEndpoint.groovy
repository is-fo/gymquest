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

        app.get("/user/{username}") { ctx -> {
            def username = ctx.pathParam("username")
            def user = User.createUser(UserRepository.getInstance().findByUsername(username))

            if (user != null) {
                def jsonResponse = JsonOutput.toJson(user)
                ctx.contentType("application/json").result(jsonResponse)
            } else {
                ctx.status(404).result("User not found")
            }
        }}
    }
}
