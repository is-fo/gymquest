package org.example.service

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoException
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.BsonDocument
import org.bson.BsonInt64
import org.bson.Document
import org.bson.conversions.Bson
import org.example.model.User

class MongoService {

    static MongoDatabase database
    static MongoClient mongoClient

    def static connect() {
        def props = new Properties()
        def envFile = new File(".env")
        envFile.withInputStream { it -> props.load(it) }

        def uri = props.getProperty("CONNECTION_STRING")

        if (mongoClient == null) {
            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build()

            try {
                MongoClient mongoClient = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyConnectionString(new ConnectionString(uri))
                            .build())

                database = mongoClient.getDatabase("gymquest")
                println 'Connected to database: gymquest'

                Bson command = new BsonDocument("ping", new BsonInt64(1))
                database.runCommand(command)
                println 'Connection successful!'
            } catch (MongoException me) {
                System.err.println(me)
            }
        }
    }

    def static close() {
        mongoClient?.close()
    }

    def static insertUser(User user) {
        def collection = database.getCollection("users")

        def userDoc = user.createDocument()

        try {
            def result = collection.insertOne(userDoc)
            def _id = result.getInsertedId().asObjectId().getValue()
            println("Inserted an user with id: $_id")
            return _id
        } catch (Exception e) {
            println "Error inserting user ${e.message}"
        }
    }
}
