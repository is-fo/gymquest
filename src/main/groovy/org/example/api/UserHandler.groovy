package org.example.api

import org.bson.Document
import org.example.model.User
import org.example.repository.UserRepository
import org.example.util.TokenUtil
import org.mindrot.jbcrypt.BCrypt

class UserHandler {

    static final UserHandler instance = new UserHandler()

    def static final FIVE_MINUTES = 5 * 60 * 1000

    private UserHandler() {

    }

    static UserHandler getInstance() {
        return instance
    }

    static def registerUser(String username, String password) {
        def userRepository = UserRepository.getInstance()
        if (userRepository.findByUsername(username)) {
            return "Username already in use!"
        } else {
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
            def user = new User(username, new Date(), 0)
            def _id = userRepository.insert(user)
            if (_id == null) {
                return "ERROR INSERTING USER"
            }
            userRepository.updateRow(_id, "password", passwordHash)
            println "$user has been registred, id: $_id"
            return TokenUtil.generateToken(username)
        }
    }

    static def login(String username, String inputPassword) {
        def userRepository = UserRepository.getInstance()
        def userDoc = userRepository.findByUsername(username)
        if (!userDoc) {
            return "Fel användarnamn"
        }
        if (loginLocked(userDoc)) {
            println "Det gick inte att logga in, för många misslyckade försök."
            return "Låst i $FIVE_MINUTES millisekunder" as String
        }
        def id = userDoc.get("_id") as String
        String passwordHash = userDoc.get("password")
        if (BCrypt.checkpw(inputPassword, passwordHash)) {
            userRepository.updateRow(id, "loginAttempts", [])
            println "Lösenordet är korrekt!"
            return TokenUtil.generateToken(username)
        } else {
            println "Fel lösenord."
            loginFailed(id)
            return "Fel lösenord."
        }
    }

    static def lockUser(String id) {
        def now = new Date()
        UserRepository.getInstance().updateRow(id, "lockedUntil", new Date(now.time + FIVE_MINUTES))
    }

    static def loginLocked(Document userDoc) {
        def lockUntil = userDoc.get("lockedUntil") as Date

        return lockUntil != null && lockUntil.after(new Date())
    }

    static def loginFailed(String id) {
        def userRepository = UserRepository.getInstance()
        userRepository.appendToArray(id, "loginAttempts", new Date())
        def userDoc = userRepository.findById(id)
        def loginAttempts = userDoc.get("loginAttempts") as ArrayList

        if (loginAttempts.size() >= 3) {
            def thirdAttempt = loginAttempts[-3]
            def now = new Date()
            if (thirdAttempt > new Date(now.time - FIVE_MINUTES)) {
                println "Fel lösenord, användare låst"
                lockUser(id)
            } else {
                println "Fel lösenord, försök igen      (i loginFailed())"
            }
        }
    }
}
