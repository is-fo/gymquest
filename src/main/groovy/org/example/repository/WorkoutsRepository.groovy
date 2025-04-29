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
        def doc = [
                userId: new ObjectId(userId),
                timestamp: new Date(),
                exercises: []
        ] as Document
        insertDoc(doc)
        return doc._id
    }

    def addExercise(String workoutId, String exerciseId) {
        def exercise = [
                exerciseId: new ObjectId(exerciseId),
                sets: []
        ] as Document
        appendToArray(workoutId, "exercises", exercise)
    }

    def logSet(String workoutId, String exerciseId, Integer reps, Integer weight) {
        def success = pushToNestedArray(
                workoutId,
                "exercises",
                "exerciseId",
                exerciseId,
                [reps: reps, weight: weight]
        )
        if (!success) {
            println "Exercise not found, adding it..."
            addExercise(workoutId, exerciseId)
            pushToNestedArray(
                    workoutId,
                    "exercises",
                    "exerciseId",
                    exerciseId,
                    [reps: reps, weight: weight]
            )
        }
    }
}
