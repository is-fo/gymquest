package org.example.model

abstract sealed class Entity permits User, Exercise {
    String _id

    Entity(String _id) {
        this._id = _id
    }

    abstract createDocument()

    void set_id(String _id) {
        this._id = _id
    }
}
