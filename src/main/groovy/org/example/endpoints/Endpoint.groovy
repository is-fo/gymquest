package org.example.endpoints

import io.javalin.Javalin

class Endpoint {

    private Endpoint() {}

    static def init() {
        def app = Javalin.create().start(8080)

        UserEndpoints.endpoints(app)
        ExerciseEndpoints.endpoints(app)
    }
}
