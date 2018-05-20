package group1.fitnessapp.excerciseTracker;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Exercise implements Parcelable {

    private int id;
    private String name;
    private String description;
    private String category;
    private ArrayList<Set> sets = new ArrayList<>();
    private int totalRepsDone = 0;
    private Context context;
    private ExerciseTrackerDatabaseHelper helper;

    public Exercise(Parcel parcel) {
        String data[] = new String[5];
        parcel.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.description = data[2];
        this.category = data[3];
        this.totalRepsDone = Integer.parseInt(data[4]);
    }

    public Exercise(String name) {
        this.name = name;
    }

    public Exercise(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Exercise(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public Exercise(int id, String name, String description, String category, int totalRepsDone) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.totalRepsDone = totalRepsDone;
    }

    public Exercise(String name, String description, String category, ArrayList<Set> sets) {
       this.name = name;
       this.description = description;
       this.sets = sets;
       this.category = category;
    }

    public void addReps(int reps) {
        totalRepsDone = totalRepsDone + reps;
        helper.updateTotalReps(this.id, this.totalRepsDone);
    }

    public void addSet(int repGoal) {
        sets.add(new Set(sets.size() + 1, repGoal, this));
    }

    public void addFullSet(Set set) {
        sets.add(set);
    }

    public void removeSet(Set set) {
        sets.remove(set);
        for(Set s: sets) {
            s.reposition();
        }
    }

    public int getId() { return this.id; }

    public int getTotalRepsDone() {
        Cursor cursor = helper.readExerciseById(this.id);
        cursor.moveToNext();
        this.totalRepsDone = cursor.getInt(4);
        return this.totalRepsDone;
    }

    public String getCategory() { return this.category; }

    public ArrayList<Set> getSets() { return this.sets; }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.description;
    }

    public void setContext(Context context) {
        this.context = context;
        this.helper = new ExerciseTrackerDatabaseHelper(this.context);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                this.id + "", this.name, this.description, this.category, this.totalRepsDone + ""
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }

    };
}
