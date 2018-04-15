package group1.fitnessapp.excerciseTracker;

import java.util.ArrayList;

public class Workout {
    private String workoutName;
    private int sortOrder;
    private ArrayList<Object> excercises = new ArrayList<>();

    public Workout(String workoutName, int sortOrder){
        this.workoutName = workoutName;
        this.sortOrder = sortOrder;
    }
}
