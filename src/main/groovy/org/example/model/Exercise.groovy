package org.example.model

import org.bson.Document

class Exercise extends Entity {

    String category
    String name
    String description
    List<String> equipment

    Exercise(String category, String name, String description, List<String> equipment) {
        super(null)
        this.category = category
        this.name = name
        this.description = description
        this.equipment = equipment
    }

    @Override
    def createDocument() {
        return new Document("category", this.category)
                .append("name", this.name)
                .append("description", this.description)
                .append("equipment", this.equipment)
    }
}
