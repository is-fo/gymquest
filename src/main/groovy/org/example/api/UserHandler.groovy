package org.example.api

import org.example.model.User
import org.example.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt

class UserHandler {

    UserRepository userRepository

    UserHandler() {
        this.userRepository = UserRepository.getInstance()
    }

    def registerUser(String username, String password){
        if (userRepository.findByUsername(username)){
            throw new RuntimeException("Username already in use!")
        } else {
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
            def user = new User(username,passwordHash,new Date(), 0)
            def _id = userRepository.insert(user)
            println "$user has been registred, id: $_id"
        }
    }

    def login(String username, String inputPassword){
        def userDoc = userRepository.findByUsername(username)
        if (userDoc != null){
            String passwordHash = userDoc.get("password")
            if (BCrypt.checkpw(inputPassword, passwordHash)) {
                println "Lösenordet är korrekt!"
            } else {
                println "Fel lösenord."
            }
        } else {
            println "Fel användarnamn."
        }
    }
}
