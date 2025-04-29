package org.example.repository

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
        findByField("username", username)
    }

    def increaseExp(String _id, Integer exp) {
        updateRow(_id, "exp", this.findById(_id).get("exp") as Integer + exp)
    }

}