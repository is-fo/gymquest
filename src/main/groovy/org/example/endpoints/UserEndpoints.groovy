package org.example.endpoints

import groovy.json.JsonOutput
import io.javalin.Javalin
import org.example.api.TokenUtil
import org.example.api.UserHandler
import org.example.model.User
import org.example.repository.UserRepository

class UserEndpoints {
    static void endpoints(Javalin app) {
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

        /**
         * https://jwt.io/introduction
         */

        app.get("/test") {
            def token = extractBearerToken(it)

            if (!TokenUtil.validateToken(token)) {
                it.status(401).result("Invalid or expired token")
                return
            }

            def username = TokenUtil.getUsername(token)
            it.result("Hello ${username}! You have a valid token!")
        }
    }

    //util metod, kanske flytta
    static String extractBearerToken(it) {
        def authHeader = it.header("Authorization")

        // authHeader kan vara null (jag har testat) och då får man 401 tillbaka i Postman
        if (!authHeader?.startsWith("Bearer ")) {
            it.status(401).result("Missing or malformed Auth header")
            return null
        }

        return authHeader.replace("Bearer ", "")
    }
}
