package org.example.api


import org.example.model.User
import org.example.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt

class UserHandler {

    UserRepository userRepository

    UserHandler() {
        this.userRepository = UserRepository.getInstance()
    }

    def registerUser(String username, String password) {
        if (userRepository.findByUsername(username)) {
            throw new RuntimeException("Username already in use!")
        } else {
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
            def user = new User(username, passwordHash, new Date(), 0)
            def _id = userRepository.insert(user)
            println "$user has been registred, id: $_id"
        }
    }

    def login(String username, String inputPassword) {
        if (loginLocked(username)) {
            println "Det gick inte att logga in, för många misslyckade försök."
            return
        }
        def userDoc = userRepository.findByUsername(username)
        if (userDoc != null) {
            String passwordHash = userDoc.get("password")
            if (BCrypt.checkpw(inputPassword, passwordHash)) {
                userRepository.updateRow(userDoc.get("_id") as String, "loginAttempts", [])
                println "Lösenordet är korrekt!"
            } else {
                println "Fel lösenord."
                loginFailed(username)
            }
        } else {
            println "Fel användarnamn."
        }

    }

    def lockUser(String id) {
        def now = new Date()
        userRepository.updateRow(id, "lockedUntil", new Date(now.time + (5 * 60 * 1000)))
    }

    def loginLocked(String username) {
        def userDoc = userRepository.findByUsername(username)
        def lockUntil = userDoc.get("lockedUntil") as Date

        if (lockUntil != null && lockUntil.after(new Date())) {
            return true
        } else {
            return false
        }
    }

    def loginFailed(String username) {
        def userDoc = userRepository.findByUsername(username)
        String id = userDoc.get("_id")
        userRepository.appendToArray(id, "loginAttempts", new Date())

        def loginAttempts = userDoc.get("loginAttempts") as ArrayList

        if (loginAttempts.size() >= 3) {
            //def thirdAttempt = loginAttempts[-3]
            def thirdAttempt = loginAttempts.get(loginAttempts.size() - 3)
            println thirdAttempt

            def now = new Date()

            if (thirdAttempt > new Date(now.time - (5 * 60 * 1000))) {
                println "Fel lösenord, användare låst"
                lockUser(id)

            } else {
                println "Fel lösenord, försök igen      (i loginFailed())"
            }
        }
    }
}
