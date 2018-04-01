package group1.fitnessapp.dietTracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import group1.fitnessapp.R;

/**
 * Created by aaron on 1/04/2018.
 */

public class DietAddFood extends AppCompatActivity {
    private ArrayList<Food> results = new ArrayList<>();
    private DietTrackerActivity adapt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_add_food);
        SearchView searchView = findViewById(R.id.addFoodSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String search) {
                Thread thread = new Thread("Search") {
                    @Override
                    public void run() {
                        getResults(search);
                    }
                };
                thread.start();
                updateResults();
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String search) {
                return false;
            }
        });

//        adapt = new DietTrackerActivity(this, results);
//        final ListView ls = (ListView) findViewById(R.id.ls_results);
//        ls.setAdapter(adapt);
//        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Food selected = (Food) ls.getItemAtPosition(i);
//                setResult(selected);
//            }
//        });
    }

    private void setResult(Food food){
        Intent intent = new Intent();
        intent.putExtra("SELECTED_FOOD_NAME", food.getName());
        intent.putExtra("SELECTED_FOOD_SUB_TXT", food.getSubText());
        intent.putExtra("SELECTED_FOOD_CALORIES", food.getCalories());
        finish();
    }

    private void getResults(String query){
        try {
            results.addAll(new FoodAPI().search(query));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateResults() {
        System.out.println("Updating results");
        System.out.println(results);
        //adapt.notifyDataSetChanged();
    }
}