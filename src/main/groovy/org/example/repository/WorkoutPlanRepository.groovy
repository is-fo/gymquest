package org.example.repository

import org.example.service.MongoService

class WorkoutPlanRepository extends Repository {
    static final WorkoutPlanRepository instance = new WorkoutPlanRepository()

    private WorkoutPlanRepository() {
        this.collection = MongoService.getDatabase().getCollection("workout_plan")
    }

    static WorkoutPlanRepository getInstance() {
        return instance
    }
}
