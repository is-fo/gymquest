package org.example

import org.example.api.Endpoint
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
}