package org.example.model

import org.bson.Document

class User {
    String _id
    String username
    String password
    Date registerDate
    Integer exp

    User(String username, String password, Date registerDate, Integer exp) {
        this.username = username
        this.password = password
        this.registerDate = registerDate
        this.exp = exp
    }

    void set_id(String _id) {
        this._id = _id
    }

    def createDocument() {
        return new Document("user", this.username)
        .append("password", this.password)
        .append("registerDate", this.registerDate)
        .append("exp", this.exp)
    }
}
