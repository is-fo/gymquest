package org.example.repository

import com.mongodb.client.model.Filters
import org.example.service.MongoService

class UserRepository extends Repository {

    static final UserRepository instance = new UserRepository()

    private UserRepository() {
        this.collection = MongoService.getDatabase().getCollection("users")
    }

    static UserRepository getInstance() {
        return instance
    }

    def findByUsername(String username) {
        try {
            def filter = Filters.eq("username", username)
            def result = collection.find(filter).first()
            if (result == null) {
                println "No entity found with id: $username"
                return null
            }
            return result
        } catch (Exception e) {
            println "Error finding entity by username: ${e.message}"
            e.printStackTrace()
        }
    }

}