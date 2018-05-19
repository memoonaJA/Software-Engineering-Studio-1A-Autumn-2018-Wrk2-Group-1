package group1.fitnessapp.excerciseTracker;

import java.util.ArrayList;

public class Workout {

    private String name;
    private ArrayList<Exercise> exercises = new ArrayList<>();

    public Workout(String name) {
        this.name = name;
    }

    public Workout(String name, ArrayList<Exercise> list) {
        this.name = name;
        this.exercises = list;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Exercise> getExercises() {
        return this.exercises;
    }

    public boolean hasExercise(String name) {
        for(Exercise e: exercises) {
            if(e.getName() == name) {
              return true;
            }
        }
        return false;
    }

    public void removeExercise(Exercise exercise) {
        for(Exercise e: exercises) {
            if(e.getName() == exercise.getName()) {
              exercises.remove(e);
            }
        }
    }
}
