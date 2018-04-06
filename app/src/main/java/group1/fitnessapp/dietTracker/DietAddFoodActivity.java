package group1.fitnessapp.dietTracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import group1.fitnessapp.R;

public class DietAddFoodActivity extends AppCompatActivity {
    private ArrayList<Food> results = new ArrayList<>();
    private FoodListAdapter adapt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_add_food);

        final SearchView searchView = findViewById(R.id.addFoodSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String search) {
                AsyncTask<String, Void, ArrayList<Food>> asyncTask = new FoodSearchASyncTask().execute(search);
                try {
                    results.clear();
                    results.addAll(asyncTask.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                adapt.notifyDataSetChanged();
                return false;
            }
            @Override
            public boolean onQueryTextChange(final String search) {
                return false;
            }
        });

        adapt = new FoodListAdapter(this, results);
        final ListView ls = (ListView) findViewById(R.id.ls_results);
        ls.setAdapter(adapt);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Food selected = (Food) ls.getItemAtPosition(i);
                Intent intent = new Intent();
                intent.putExtra("foodName", selected.getName());
                intent.putExtra("foodSubText", selected.getSubText());
                //TODO ADDRESS THIS HARDCODED VALUE
                intent.putExtra("foodServings", 1);
                intent.putExtra("foodServingQuantity", selected.getServingQuantity());
                intent.putExtra("foodServingUnit", selected.getServingUnit());
                intent.putExtra("foodCalories", selected.getCalories());
                intent.putExtra("foodOriginalJSON", String.valueOf(selected.getOriginalJSON()));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
