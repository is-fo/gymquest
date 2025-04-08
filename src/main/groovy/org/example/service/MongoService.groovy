package org.example.service

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoException
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.bson.BsonDocument
import org.bson.BsonInt64
import org.bson.Document
import org.bson.conversions.Bson

class MongoService {
    static connect() {
        def props = new Properties()
        def envFile = new File(".env")
        envFile.withInputStream { it -> props.load(it) }

        def uri = props.getProperty("CONNECTION_STRING")

        ServerApi serverApi = ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build()

         try (MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .build()
        )) {
             MongoDatabase database = mongoClient.getDatabase("admin")
            try {
                Bson command = new BsonDocument("ping", new BsonInt64(1))
                Document commandResult = database.runCommand(command)
                println 'Success!'
            } catch (MongoException me) {
                System.err.println(me)
            }
         }
    }
}
