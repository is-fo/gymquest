package org.example

import org.example.api.UserHandler
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

//    def userId = User.createUser(UserRepository.getInstance().findByUsername("Kalle"))._id
//    println userId

    def userHandler = new UserHandler()
    userHandler.registerUser("Kalle", "mypassword")
//    userHandler.login("Kalle", "mypassword")
//    UserRepository.getInstance().updateRow(userId, 'exp', 5)



}