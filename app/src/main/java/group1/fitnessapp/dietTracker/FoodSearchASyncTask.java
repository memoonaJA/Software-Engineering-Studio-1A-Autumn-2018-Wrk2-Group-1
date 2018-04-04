package group1.fitnessapp.dietTracker;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by aaron on 4/04/2018.
 */

public class FoodSearchASyncTask extends AsyncTask<String, Void, ArrayList<Food>> {
    private Exception exception;
    private ArrayList<Food> results = null;

    @Override
    protected ArrayList<Food> doInBackground(String... search) {
        try {
            results = new FoodAPI().search(search[0]);
            return results;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Food> getResults(){
        return results;
    }
}
