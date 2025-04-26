package org.example.endpoints

import com.mongodb.client.model.Sorts
import groovy.json.JsonOutput
import io.javalin.Javalin
import org.bson.Document
import org.example.util.TokenUtil
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

            //kanske lägga in innan här o kolla om användaren är låst, och returna en specialare så att man vet det
            def success = UserHandler.getInstance().login(username as String, password as String)
            def result = success ? TokenUtil.generateToken(username as String) : "Login failed"

            it.json([status: "ok", token: result ])
        }

        app.post("/register") {
            def body = it.bodyAsClass(Map)
            def username = body.username
            def password = body.password

            def success = UserHandler.getInstance().registerUser(username as String, password as String)
            def result = success ? TokenUtil.generateToken(username as String) : "Register failed."

            it.json([status: "ok", token: result])
        }

        /**
         * https://jwt.io/introduction
         */

        app.get("/test") {
            def token = TokenUtil.extractBearerToken(it)

            if (!TokenUtil.validateToken(token)) {
                it.status(401).result("Invalid or expired token")
                return
            }

            def username = TokenUtil.getUsername(token)
            it.result("Hello ${username}! You have a valid token!")
        }

        app.get("/top/{limit}") {
            def limitStr = it.pathParam("limit")
            int limit
            try {
                limit = Integer.parseInt(limitStr)
                if (limit <= 0) throw new NumberFormatException()
            } catch (NumberFormatException e) {
                it.status(400).result("Path param: \"limit\" must be a positive integer")
                return
            }

            def projection = new Document("username", 1)
                    .append("exp", 1)
                    .append("_id", 0)

            def result = UserRepository.getInstance()
                    .collection
                    .find()
                    .sort(Sorts.descending("exp"))
                    .limit(limit)
                    .projection(projection)
                    .into([])

            it.json(result)
        }
    }
}
