package org.example.model

import org.bson.Document

class User extends Entity {
    String username
    String password
    Date registerDate
    Integer exp

    User(String username, String password, Date registerDate, Integer exp) {
        super(null)
        this.username = username
        this.password = password
        this.registerDate = registerDate
        this.exp = exp
    }

    def createDocument() {
        return new Document("username", this.username)
        .append("password", this.password)
        .append("registerDate", this.registerDate)
        .append("exp", this.exp)

    }

    static def createUser(Document document) {
        def user = new User(
                document.get('username') as String,
                document.get('password') as String,
                document.get('registerDate') as Date,
                document.get('exp') as Integer
        )
        user._id = document.get('_id') as String
        return user
    }

    @Override
    String toString() {
        return "User{" +
                "_id='" + (_id != null ? _id : "0") + '\'' +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registerDate=" + registerDate +
                ", exp=" + exp +
                '}'
    }
}
