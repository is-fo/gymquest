package org.example

import org.example.model.User
import org.example.repository.Repository
import org.example.service.MongoService

static void main(String[] args) {
    println "Hello world!"
    println("Hello Groovy")

    Runtime.getRuntime().addShutdownHook(new Thread() {
        void run() {
            MongoService.close()
        }
    })
    def user = new User("Sir Printsalot",
            "hej1235!!!as",
            new Date(),
    736)
    MongoService.connect()

    def userRepository = new Repository<User>(MongoService.database, "users")
    def userId = userRepository.insert(user)
    user.set_id(userId)
    println userRepository.findById(userId)
    println user.toString()
}