package org.example.repository

import org.bson.Document
import org.bson.types.ObjectId
import org.example.service.MongoService

class WorkoutsRepository extends Repository {
    static final instance = new WorkoutsRepository()

    private WorkoutsRepository() {
        this.collection = MongoService.getDatabase().getCollection("workouts")
    }

    static WorkoutsRepository getInstance() {
        return instance
    }

    def createWorkout(String userId) {
        return insertDoc([userId   : new ObjectId(userId),
                          timestamp: new Date(),
                          exercises: []] as Document)
    }

    def addExercise(String workoutId, String exerciseId) {
        return appendToArray(workoutId, "exercises",
                [exerciseId: new ObjectId(exerciseId),
                 sets      : []] as Document)
    }

    def logSet(String workoutId, String exerciseId, Integer reps, Integer weight) {
        if (!pushToNestedArray(workoutId, "exercises", "exerciseId", exerciseId, [reps: reps, weight: weight])) {
            println "Exercise not found, adding it..."
            addExercise(workoutId, exerciseId)
            pushToNestedArray(workoutId, "exercises", "exerciseId", exerciseId, [reps: reps, weight: weight])
        }
    }
}
