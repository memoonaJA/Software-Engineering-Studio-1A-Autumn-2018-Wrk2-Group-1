package group1.fitnessapp.weightTracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    }

    // READ ----------------------------------------------------------------------------------------
    public void getLastFifty(Date StartDate){

    }

    // UPDATE --------------------------------------------------------------------------------------

    // DESTROY -------------------------------------------------------------------------------------
}
