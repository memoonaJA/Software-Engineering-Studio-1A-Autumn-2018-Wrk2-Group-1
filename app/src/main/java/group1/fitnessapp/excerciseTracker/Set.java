package group1.fitnessapp.excerciseTracker;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Set implements Parcelable {

    private int id;
    private int setNo;
    private int repGoal;
    private int repsRemaining;
    private Exercise exercise;
    private Date lastModified;
    private Context context;
    private ExerciseTrackerDatabaseHelper helper;

    public Set(Parcel parcel) {
        String data[] = new String[5];
        parcel.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.setNo = Integer.parseInt(data[1]);
        this.repGoal = Integer.parseInt(data[2]);
        this.repsRemaining = Integer.parseInt(data[3]);
        this.lastModified = new Date(Long.parseLong(data[4]));
    }

    public Set(int id, int setNo, int repGoal, int repsRemaining, Exercise exercise, Date lastModified) {
        this.id = id;
        this.setNo = setNo;
        this.repGoal = repGoal;
        this.repsRemaining = repsRemaining;
        this.exercise = exercise;
        this.lastModified = lastModified;
    }

    public Set(int setNo, int repGoal, int repsRemaining, Exercise exercise, Date lastModified) {
        this.setNo = setNo;
        this.repGoal = repGoal;
        this.repsRemaining = repsRemaining;
        this.exercise = exercise;
        this.lastModified = lastModified;
    }

    public Set(int repGoal, int repsRemaining) {
        this.repGoal = repGoal;
        this.repsRemaining = repsRemaining;
    }

    public Set(int setNo, int repGoal, Exercise exercise) {
        this.repGoal = repGoal;
        this.repsRemaining = repGoal;
        this.exercise = exercise;
        this.setNo = setNo;
    }

    public void removeSet() {
        this.exercise.getSets().remove(this);
    }

    public void reposition() {
        this.setNo = this.exercise.getSets().indexOf(this);
    }

    public void commitReps(int reps) {
        if(reps > repsRemaining) {
            int difference = reps - repsRemaining;
            this.exercise.addReps(repsRemaining);
            this.repsRemaining = 0;
            //this.exercise.addReps(reps);
            helper.updateSetRepsRemaining(this.id, repsRemaining);
            if(getNextSet() != null) {
                this.getNextSet().commitReps(difference);
            }
        } else {
            this.repsRemaining = this.repsRemaining - reps;
            this.exercise.addReps(reps);
            //this.exercise.addReps(reps);
            helper.updateSetRepsRemaining(this.id, repsRemaining);
        }
        this.lastModified = getCurrentDate();
        helper.updateLastDate(this.id, this.lastModified);
    }

    public Date getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return date;
    }

    public void changeReps(int repGoal) {
        this.repGoal = repGoal;
        this.repsRemaining = repGoal;
        helper.updateSetRepGoal(this.id, this.repGoal);
        helper.updateSetRepsRemaining(this.id, this.repsRemaining);
    }

    public void refreshSet() {
        this.repsRemaining = repGoal;
    }

    public int getThisIndex(Set set) {
        int i = 0;
        for(Set s: exercise.getSets()) {
            if(s.getSetNumber() == this.setNo) {
                return i;
            } else {
                i = i + 1;
            }
        }
        return 0;
    }

    public void setContext(Context context) {
        this.context = context;
        helper = new ExerciseTrackerDatabaseHelper(this.context);
    }

    public void setExercise(Exercise exercise) { this.exercise = exercise; }

    public Exercise getExercise() { return this.exercise; }

    public int getId() { return this.id; }

    public int getRepsRemaining() { return this.repsRemaining; }

    public int getRepGoal() { return this.repGoal; }

    public int getSetNumber() {
        return this.setNo;
    }

    public Set getNextSet() {
        Cursor cursor = helper.readSetBySetNo(this.setNo + 1, this.exercise.getId());
        cursor.moveToNext();
        Set set = new Set(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), this.exercise,
                new Date(cursor.getLong(4)));
        set.setContext(this.context);
        return set;
    }

    public Date getLastModified() { return this.lastModified; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                this.id + "", this.setNo + "", this.repGoal + "", this.repsRemaining + "", this.lastModified.getTime() + ""
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Set createFromParcel(Parcel in) {
            return new Set(in);
        }

        public Set[] newArray(int size) { return new Set[size]; }

    };

}
