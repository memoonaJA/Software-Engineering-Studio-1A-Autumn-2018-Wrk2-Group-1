package group1.fitnessapp.dietTracker;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by aaron on 4/04/2018.
 */

public class FoodSearchASyncTask extends AsyncTask<String, Void, ArrayList<Food>> {
    @Override
    protected ArrayList<Food> doInBackground(String... search) {
        try {
            return new FoodAPI().search(search[0]);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class FoodAPI{
        private ArrayList<Food> search(String query) throws IOException, JSONException {
            String app_id = "6b98342e";
            String app_key = "a191dc56024fad657c97dba4f2d44d33";

            URL url = new URL("https://trackapi.nutritionix.com/v2/search/instant?query="+query);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("x-app-id",app_id);
            urlConnection.setRequestProperty("x-app-key",app_key);
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Charset.forName("UTF-8")));
            String jsonTxt = readAll(rd);
            urlConnection.getInputStream().close();
            JSONObject jsonObject = new JSONObject(jsonTxt);
            return asArrayList(jsonObject);
        }

        private ArrayList<Food> asArrayList(JSONObject jsonObject) throws JSONException {
            ArrayList<Food> results = new ArrayList<>();
            JSONArray branded = jsonObject.getJSONArray("branded");
            for (int i = 0; i  < branded.length(); i++){
                JSONObject foodJSON = branded.getJSONObject(i);
                String name = foodJSON.getString("food_name");
                String subTxt = foodJSON.getString("brand_name");
                double servingQuantity = foodJSON.getDouble("serving_qty");
                String servingUnit = foodJSON.getString("serving_unit");
                double calories = foodJSON.getDouble("nf_calories");
                results.add(new Food(name, subTxt, 1, servingQuantity, servingUnit, calories));
            }

            //JSONArray common = jsonObject.getJSONArray("common");
            //for (int i = 0; i  < common.length(); i++){
            //JSONObject food = common.getJSONObject(i);
            //String name = food.getString("food_name");
            //String subTxt = food.getString("serving_qty") + " " + food.getString("serving_unit");
            //int calories = food.getInt("nf_calories");
            //String subTxt = "testing";
            //int calories = 666;
            //results.add(new Food(name, subTxt, calories));
            //}
            return results;
        }

        private String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }
    }
}
