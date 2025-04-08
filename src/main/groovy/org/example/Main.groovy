package org.example

import org.example.model.User
import org.example.service.MongoService

static void main(String[] args) {
    println "Hello world!"
    println("Hello Groovy")

    def user = new User("Ryan Gosling",
            "hej123",
            new Date(),
    76)
    MongoService.connect()
    MongoService.insertUser(user)
    MongoService.close()
}