package org.example

import org.example.endpoints.Endpoint
import org.example.repository.CategoryRepository
import org.example.repository.ExerciseRepository
import org.example.repository.UserRepository
import org.example.service.MongoService

static void main(String[] args) {
    MongoService.connect()
    Runtime.getRuntime().addShutdownHook(new Thread() {
        void run() {
            MongoService.close()
        }
    })

    //kommentera ut om ni inte vill att endpointen körs (programmet stängs aldrig av när den körs)
    //nås på localhost:8080
    Endpoint.init()

//    def user = UserRepository.getInstance().findByUsername("glenn").get("_id") as String
//    UserRepository.getInstance().increaseExp(user, 15)
}