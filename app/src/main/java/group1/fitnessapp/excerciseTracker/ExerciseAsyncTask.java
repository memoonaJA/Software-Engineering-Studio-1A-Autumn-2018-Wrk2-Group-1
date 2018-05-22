package group1.fitnessapp.excerciseTracker;

import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ExerciseAsyncTask extends AsyncTask<String, Void, ArrayList<Exercise>> {

    @Override
    protected ArrayList<Exercise> doInBackground(String... search) {
        try {
            return new ExerciseAPI().search(search[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    private class ExerciseAPI {
        private ArrayList<Exercise> search(String query) throws IOException, JSONException {
            String app_key = "1cb14f4cacac1939ec56c6c2f1e35c17dfbd7157";
            URL url = new URL("https://wger.de/api/v2/exercise/search/?term=" + query + "&status=2&language=2");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("x-app-key",app_key);
            connection.setRequestProperty("accept", "application/json");
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            String text = readAll(rd);
            connection.getInputStream().close();
            JSONObject jsonObject = new JSONObject(text);
           // System.out.println(text);
            return asArrayList(jsonObject);
        }

        private JSONObject getDescription(String name) throws IOException, JSONException {
            URL url = new URL("https://wger.de/api/v2/exercise/?name=" + name + "&language=2&status=2");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("x-app-key",app_key);
            connection.setRequestProperty("accept", "application/json");
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            String text = readAll(rd);
            connection.getInputStream().close();
            JSONObject jsonObject = new JSONObject(text);
            return jsonObject;
        }

        private ArrayList<Exercise> asArrayList(JSONObject object) throws JSONException, IOException {
            ArrayList<Exercise> results = new ArrayList<>();
            JSONArray exercise = object.getJSONArray("suggestions");
            for(int i = 0; i < exercise.length(); i++) {
                JSONObject exerciseObject = exercise.getJSONObject(i);
                //System.out.println(exerciseObject.toString());
                JSONObject auxillaryObject = exerciseObject.getJSONObject("data");
                String name = auxillaryObject.getString("name");
                String category = auxillaryObject.getString("category");
                //String description = getDescription(name).getJSONArray("results").getJSONObject(0).getString("description").replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "");
                //String description = exerciseObject.getString("description");
                //results.add(new Exercise(name, description, category));
                //results.add(new Exercise(name));
                results.add(new Exercise(name, category));
            }
            return results;
        }

        private String readAll(Reader rd) throws IOException {
            StringBuilder builder = new StringBuilder();
            int i;
            while ((i = rd.read()) != -1) {
                builder.append((char) i);
            }
            return builder.toString();
        }
    }
}
