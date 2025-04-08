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

    println User.createUser(UserRepository.getInstance().findByUsername("Kalle"))

    def userHandler = new UserHandler()
    // skapa användare -> userHandler.registerUser("Kalle", "mypassword")
    userHandler.login("Kalle", "mypassword")


}