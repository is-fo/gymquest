package org.example

import org.example.api.UserHandler
import org.example.api.UsersEndpoint
import org.example.model.User
import org.example.repository.UserRepository
import org.example.service.MongoService

static void main(String[] args) {
    println "Hello world!"
    println("Hello Groovy")

    MongoService.connect()
    Runtime.getRuntime().addShutdownHook(new Thread() {
        void run() {
            MongoService.close()
        }
    })
    //kommentera ut om ni inte vill att endpointen körs (programmet stängs aldrig av när den körs)
    //nås på localhost:8080/users/användarnamn t.ex Kalle
    UsersEndpoint.init()

//    def userId = User.createUser(UserRepository.getInstance().findByUsername("Kalle"))._id
//    println userId

    def userHandler = new UserHandler()
    //userHandler.registerUser("Kalle", "mypassword")
//    userHandler.login("Kalle", "mypassword")
//    UserRepository.getInstance().updateRow(userId, 'exp', 5)



    userHandler.login("Kalle","mypassword")



}