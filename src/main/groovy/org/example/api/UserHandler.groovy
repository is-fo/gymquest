package org.example.api

import org.example.model.User
import org.example.repository.UserRepository
import org.example.util.TokenUtil
import org.mindrot.jbcrypt.BCrypt

class UserHandler {

    static final UserHandler instance = new UserHandler()

    UserRepository userRepository
    def static final FIVEMINUTES = 5 * 60 * 1000

    private UserHandler() {
        this.userRepository = UserRepository.getInstance()
    }

    static UserHandler getInstance() {
        return instance
    }

    def registerUser(String username, String password) {
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

    def login(String username, String inputPassword) {
        if (loginLocked(username)) {
            println "Det gick inte att logga in, för många misslyckade försök."
            return "Låst i $FIVEMINUTES millisekunder" as String
        }
        def userDoc = userRepository.findByUsername(username)
        def id = userDoc.get("_id") as String
        if (userDoc != null) {
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
        } else {
            println "Fel användarnamn"
            return "Fel användarnamn"
        }
    }

    def lockUser(String id) {
        def now = new Date()
        userRepository.updateRow(id, "lockedUntil", new Date(now.time + FIVEMINUTES))
    }

    def loginLocked(String username) {
        def userDoc = userRepository.findByUsername(username)
        def lockUntil = userDoc.get("lockedUntil") as Date

        return lockUntil != null && lockUntil.after(new Date())
    }

    def loginFailed(String id) {
        userRepository.appendToArray(id, "loginAttempts", new Date())
        def userDoc = userRepository.findById(id)
        def loginAttempts = userDoc.get("loginAttempts") as ArrayList

        if (loginAttempts.size() >= 3) {
            def thirdAttempt = loginAttempts[-3]
            def now = new Date()
            if (thirdAttempt > new Date(now.time - FIVEMINUTES)) {
                println "Fel lösenord, användare låst"
                lockUser(id)
            } else {
                println "Fel lösenord, försök igen      (i loginFailed())"
            }
        }
    }
}
