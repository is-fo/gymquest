package org.example

import org.example.service.MongoService

static void main(String[] args) {
    println "Hello world!"
    println("Hello Groovy")

    def mongo = MongoService.connect()
}