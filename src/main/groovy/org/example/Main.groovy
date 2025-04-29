package org.example


import org.example.repository.ExerciseRepository
import org.example.repository.UserRepository
import org.example.repository.WorkoutsRepository
import org.example.service.MongoService

static void main(String[] args) {
    MongoService.connect()
    Runtime.getRuntime().addShutdownHook(new Thread() {
        void run() {
            MongoService.close()
        }
    })

    //kommentera ut om ni inte vill att endpointen körs (programmet stängs aldrig av när den körs)
    //nås på localhost:8080
//    Endpoint.init()

    def user = UserRepository.getInstance().findByUsername("glenn")._id as String
    def bench = ExerciseRepository.getInstance().findByName("Bench Press")._id as String
    def tricep = ExerciseRepository.getInstance().findByName("Overhead Tricep Extension")._id as String
//    UserRepository.getInstance().increaseExp(user, 15)
    def workout = WorkoutsRepository.getInstance().createWorkout(user) as String
    WorkoutsRepository.getInstance().logSet(workout, bench, 3, 100)
    WorkoutsRepository.getInstance().logSet(workout, bench, 2, 99)
    WorkoutsRepository.getInstance().logSet(workout, tricep, 10, 55)
    WorkoutsRepository.getInstance().logSet(workout, tricep, 12, 60)
    WorkoutsRepository.getInstance().logSet(workout, bench, 1, 77)

}