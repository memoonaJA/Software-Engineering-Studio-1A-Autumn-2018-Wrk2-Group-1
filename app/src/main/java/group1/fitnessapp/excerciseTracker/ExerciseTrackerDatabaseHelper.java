package group1.fitnessapp.excerciseTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExerciseTrackerDatabaseHelper extends SQLiteOpenHelper {

    //Stores Database name
    public static final String DATABASE_NAME = "exercise_tracker.db";

    // Code to generate Exercise Table
    public static final String TABLE_1 = "Exercise";
    public static final String COL_1 = "exID"; //PRIMARY KEY INTEGER
    public static final String COL_2 = "Name"; //TEXT
    public static final String COL_3 = "Description"; //TEXT
    public static final String COL_4 = "Category"; //TEXT
    public static final String COL_5 = "Total_Reps"; //INTEGER

    //Code to generate Set Table
    public static final String TABLE_2 = "Exercise_Set";
    public static final String COL_6 = "setID"; //PRIMARY KEY INTEGER
    public static final String COL_7 = "setNo"; //INTEGER
    public static final String COL_8 = "repGoal"; //INTEGER
    public static final String COL_9 = "reps_Remaining"; //INTEGER
    public static final String COL_10 = "last_Date"; //INTEGER
    public static final String COL_11 = "exID"; //FOREIGN KEY INTEGER

    //Create table Exercise
    public static final String CREATE_TABLE_EXERCISE = "create table if not exists " + TABLE_1 + " (exID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT," +
            " Description TEXT, Category TEXT, Total_Reps INTEGER); ";

    //Create table Set
    public static final String CREATE_TABLE_SET = "create table if not exists " + TABLE_2 + " (setID INTEGER PRIMARY KEY AUTOINCREMENT, setNo INTEGER," +
            " repGoal INTEGER, reps_Remaining INTEGER, last_Date INTEGER, exID INTEGER, FOREIGN KEY (exID) REFERENCES Exercise(exID) on delete cascade); ";

    //Constructor for DatabaseHelper
    public ExerciseTrackerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL(CREATE_TABLE_EXERCISE);
        db.execSQL(CREATE_TABLE_SET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_2);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    //Insert Statements

    public boolean insertNewExercise(String name, String description, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues exercise_values = new ContentValues();
        exercise_values.put(COL_2, name);
        exercise_values.put(COL_3, description);
        exercise_values.put(COL_4, category);
        exercise_values.put(COL_5, 0);
        long result = db.insert(TABLE_1, null, exercise_values);
        return result == -1;
    }

    public boolean insertNewSet(int setNo, int repGoal, int repsRemaining, int exID, long date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues set_values = new ContentValues();
        set_values.put(COL_7, setNo);
        set_values.put(COL_8, repGoal);
        set_values.put(COL_9, repsRemaining);
        set_values.put(COL_10, date);
        set_values.put(COL_11, exID);
        long result = db.insert(TABLE_2, null, set_values);
        return result == -1;
    }

    //Read Statements

    public Cursor readExercises() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_1, null);
        return cursor;
    }

    public Cursor readExercisesByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        String categorySql = "select * from " + TABLE_1 + " where category = ";
        Cursor cursor;
        if(category.equals("Chest")) {
            cursor = db.rawQuery(categorySql + "'Chest' or category = 'Abs'" + ";", null);
        } else if(category.equals("Legs")) {
            cursor = db.rawQuery(categorySql + "'Legs' or category = 'Calves'" + ";",null);
        } else {
            cursor = db.rawQuery(categorySql + "'" + category + "'" + ";", null);
        }
        return cursor;
    }

    public Cursor readSetsFromExercise(int exID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_2 + " where exID = " + exID + ";", null);
        return cursor;
    }

    //Update Statements

    public void updateSetRepsRemaining(int id, int repsRemaining) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update " + TABLE_2 + " set reps_Remaining = " + repsRemaining + " where setID = " + id + ";");
    }

    public void updateSetNumber(int id, int setNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update " + TABLE_2 + " set setNo = " + setNo + " where setID = " + id + ";");
    }

    public void updateSetRepGoal(int id, int repGoal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update " + TABLE_2 + " set repGoal = " + repGoal + " where setID = " + repGoal + ";");
    }

    public void updateTotalReps(int id, int totalReps) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update " + TABLE_1 + " set Total_Reps = " + totalReps + " where exID = " + id + ";");
    }

    //Delete Statements

    public void deleteExercise(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_1 + " where exID = " + id + ";");
    }

    public void deleteSet(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_2 + " where setID = " + id + ";");
    }

}
