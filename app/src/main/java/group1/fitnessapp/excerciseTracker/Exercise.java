package group1.fitnessapp.excerciseTracker;

public class Exercise {
    private String name;
    private String description;

    public Exercise(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.description;
    }

}
