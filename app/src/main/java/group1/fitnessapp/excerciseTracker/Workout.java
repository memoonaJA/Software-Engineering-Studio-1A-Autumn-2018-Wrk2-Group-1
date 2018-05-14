package group1.fitnessapp.excerciseTracker;

import java.util.ArrayList;

public class Workout {
    private String workoutName;
    private Exercise exercise;
    private int repGoal;
    //private ArrayList<Object> excercises = new ArrayList<>(); This will be implemented in the future!

    public Workout(String workoutName, Exercise exercise, int repGoal){
        this.workoutName = workoutName;
        this.exercise = exercise;
        this.repGoal = repGoal;
    }

    public Exercise getExercise() {
        return this.exercise;
    }

    public int getRepGoal() {
        return this.repGoal;
    }

    public String getName() {
        return this.workoutName;
    }
}
