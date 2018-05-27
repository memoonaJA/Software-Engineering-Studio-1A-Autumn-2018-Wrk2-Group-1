package group1.fitnessapp.excerciseTracker;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

public class AuxillaryAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... search) {
        try {
            return new ExerciseAPI().search(search[0]);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class ExerciseAPI {

        private String search(String query) throws IOException, JSONException {
            URL url = new URL("https://wger.de/api/v2/exercise/?name=" + query + "&language=2&status=2");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("x-app-key",app_key);
            connection.setRequestProperty("accept", "application/json");
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            String text = readAll(rd);
            connection.getInputStream().close();
            JSONObject jsonObject = new JSONObject(text);
            String description  = jsonObject.getJSONArray("results").getJSONObject(0).getString("description").replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "");
            return description;
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
