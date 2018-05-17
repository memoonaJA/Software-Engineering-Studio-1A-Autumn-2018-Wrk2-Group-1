package group1.fitnessapp.dietTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DietDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "DietFeature.db";
    // Table name
    public static final String TABLE_NAME = "DietLog";

    // Table columns names
    public static final String FOOD_KEY_ID = "key_id";
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
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + FOOD_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FOOD_LOG_DATE + " TEXT, "
                + FOOD_NAME + " TEXT, "
                + FOOD_SUBTEXT + " TEXT, "
                + FOOD_SERVINGS + " DOUBLE, "
                + FOOD_SERVINGS_QTY + " DOUBLE, "
                + FOOD_SERVINGS_UNIT + " TEXT, "
                + FOOD_CALORIES + " DOUBLE " + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // CRUD METHODS HERE
    // CREATE --------------------------------------------------------------------------------------
    // Add a food to the db as new foods are added and return the foods primary key in the db
    public void foodLogAdd(String date, Food food){
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

    // READ ----------------------------------------------------------------------------------------
    // Return an array list of all foods added
    public ArrayList<Food> getLogAll(){
        ArrayList<Food> log = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                Food food = new Food(
                        cursor.getInt(0),                           // Key ID
                        cursor.getString(2),                        // Name
                        cursor.getString(3),                        // Subtext
                        Double.parseDouble(cursor.getString(4)),    // Servings
                        Double.parseDouble(cursor.getString(5)),    // Servings qty
                        cursor.getString(6),                        // Servings unit
                        Double.parseDouble(cursor.getString(7))     // Calories
                );
                log.add(food);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return log;
    }

    // Return an array list of foods in the log by the passed in date
    public ArrayList<Food> getLogDate (String date){
        ArrayList<Food> log = new ArrayList<>();

        // TODO ADDRESS THIS BAD LOGIC WITH A BETTER SELECT STATEMENT
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                if(cursor.getString(1).equalsIgnoreCase(date)){
                    Food food = new Food(
                            cursor.getInt(0),                           // Key ID
                            cursor.getString(2),                        // Name
                            cursor.getString(3),                        // Subtext
                            Double.parseDouble(cursor.getString(4)),    // Servings
                            Double.parseDouble(cursor.getString(5)),    // Servings qty
                            cursor.getString(6),                        // Servings unit
                            Double.parseDouble(cursor.getString(7))     // Calories
                    );
                    log.add(food);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return log;
    }

    // UPDATE --------------------------------------------------------------------------------------
    // Currently unused because of how the activity handles food updates with a destroy and add

    // DELETE --------------------------------------------------------------------------------------
    // Remove food from log
    public boolean deleteFood(Food toDelete){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, FOOD_KEY_ID + " = " + toDelete.getKeyId(), null) > 0;
    }
}
