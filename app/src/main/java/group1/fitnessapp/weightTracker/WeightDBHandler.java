package group1.fitnessapp.weightTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

public class WeightDBHandler extends SQLiteOpenHelper{
    //DB Features
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WeightFeature.db";
    private static final String TABLE_NAME = "WeightLog";

    //Column Names
    private static final String WEIGHT_KEY_ID = "key_id";
    private static final String WEIGHT_LOG_DATE = "log_date";
    private static final String WEIGHT_WEIGHT = "weight";
    private static final String WEIGHT_UNITS = "units";

    public WeightDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + WEIGHT_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WEIGHT_LOG_DATE + " TEXT, "
                + WEIGHT_WEIGHT + " DOUBLE, "
                + WEIGHT_UNITS + " TEXT " + ")";
        db.execSQL(CREATE_TABLE);;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // CRUD METHODS HERE
    // CREATE --------------------------------------------------------------------------------------
    public void addWeight(Weight weight){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WEIGHT_LOG_DATE, weight.getLogDate());
        values.put(WEIGHT_WEIGHT, weight.getWeight());
        values.put(WEIGHT_UNITS, weight.getUnits());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // READ ----------------------------------------------------------------------------------------
    public ArrayList<Weight> getLastFifty(){
        ArrayList<Weight> log = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        int count = 0;
        if(cursor.moveToLast()){
            do {
                count++;
                Weight weight = new Weight(
                        cursor.getInt(0),       // Key ID
                        cursor.getString(1),    // LogDate
                        cursor.getDouble(2),    // Weight
                        cursor.getString(3)     // Units
                );
                log.add(weight);
            } while (cursor.moveToPrevious() && count != 51);
        }
        db.close();
        cursor.close();
        return log;
    }

    // UPDATE --------------------------------------------------------------------------------------

    // DESTROY -------------------------------------------------------------------------------------
    public boolean deleteWeight(Weight toDelete){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = db.delete(TABLE_NAME, WEIGHT_KEY_ID + " = " + toDelete.getKey_id(), null) > 0;
        db.close();
        return result;
    }
}
