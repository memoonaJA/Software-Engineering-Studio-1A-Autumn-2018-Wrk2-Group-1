package group1.fitnessapp.dietTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.Key;
import java.util.ArrayList;

public class DietDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DietFeature.db";
    // Table name
    public static final String TABLE_NAME = "DietLog";

    // Table columns names
    public static final String FOOD_LOG_DATE = "log_date";
    public static final String FOOD_NAME = "name";
    public static final String FOOD_SUBTEXT = "subtext";
    public static final String FOOD_SERVINGS = "servings";
    public static final String FOOD_SERVINGS_QTY = "servings_qty";
    public static final String FOOD_SERVINGS_UNIT = "servings_unit";
    public static final String FOOD_CALORIES = "calories";



    public DietDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + FOOD_LOG_DATE + "TEXT,"
                + FOOD_NAME + "TEXT,"
                + FOOD_SUBTEXT + "TEXT,"
                + FOOD_SERVINGS + "DOUBLE,"
                + FOOD_SERVINGS_QTY + "DOUBLE,"
                + FOOD_SERVINGS_UNIT + "TEXT,"
                + FOOD_CALORIES + "DOUBLE " + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Add a food to the db as new foods are added
    void foodLogAdd(String date, Food food){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FOOD_LOG_DATE, date);
        values.put(FOOD_NAME, food.getName());
        values.put(FOOD_SUBTEXT, food.getSubText());
        values.put(FOOD_SERVINGS, food.getServings());
        values.put(FOOD_SERVINGS_QTY, food.getServingQuantity());
        values.put(FOOD_SERVINGS_UNIT, food.getServingUnit());
        values.put(FOOD_CALORIES, food.getCalories());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // TODO Update an added food in the db
    void foodLogUpdate(String date, Food original, Food food){
        // Check for data redundancy first with inDB() or with primary key system
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FOOD_LOG_DATE, date);
        values.put(FOOD_NAME, food.getName());
        values.put(FOOD_SUBTEXT, food.getSubText());
        values.put(FOOD_SERVINGS, food.getServings());
        values.put(FOOD_SERVINGS_QTY, food.getServingQuantity());
        values.put(FOOD_SERVINGS_UNIT, food.getServingUnit());
        values.put(FOOD_CALORIES, food.getCalories());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Return an array list of all foods added
    public ArrayList<Food> getLogAll(){
        ArrayList<Food> log = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                Food food = new Food(
                        cursor.getString(1),                        // Name
                        cursor.getString(2),                        // Subtext
                        Double.parseDouble(cursor.getString(3)),    // Servings
                        Double.parseDouble(cursor.getString(4)),    // Servings qty
                        cursor.getString(5),                        // Servings unit
                        Double.parseDouble(cursor.getString(6))     // Calories
                );
                log.add(food);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return log;
    }

    // Return an array list of foods in the log by the passed in date
    public ArrayList<Food> getLogDate (String date){
        return null;
    }

    // Remove food from log
    public boolean deleteFood(int delKeyID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, FOOD_NAME + "=" + delKeyID, null) > 0;
    }
}
