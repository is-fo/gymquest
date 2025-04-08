package org.example

import org.example.api.UserHandler
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

    println UserRepository.getInstance().findByUsername("Kalle")

    def userHandler = new UserHandler()
    // skapa användare -> userHandler.registerUser("Kalle", "mypassword")

}