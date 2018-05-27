package group1.fitnessapp.dietTracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import group1.fitnessapp.R;

public class DietAddFoodActivity extends AppCompatActivity {
    // GUI elements
    private ArrayList<Food> results = new ArrayList<>();
    private FoodListAdapter adapt = null;
    private SearchView searchView = null;
    private ListView ls = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_add_food);

        // Getting GUI elements
        searchView = findViewById(R.id.addFoodSearchView);
        adapt = new FoodListAdapter(this, results);
        ls = (ListView) findViewById(R.id.ls_results);
        ls.setAdapter(adapt);


        // Listeners
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String search) {
                AsyncTask<String, Void, ArrayList<Food>> asyncTask = new FoodSearchASyncTask().execute(search);
                try {
                    searchView.clearFocus();
                    results.clear();
                    results.addAll(asyncTask.get());
                } catch (InterruptedException | ExecutionException | NullPointerException e) {
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

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                launchAddFoodEdit((Food) ls.getItemAtPosition(i));
            }
        });
    }

    private void launchAddFoodEdit(Food food) {
        Intent intent = new Intent(this, AddFoodEditActivity.class);
        intent.putExtra("addFoodToEdit", food);
        startActivityForResult(intent, 1);
    }

    // Logic for return data from activities
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //Return logic from the addFoodEdit activity
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                switch (data.getIntExtra("returnCode", -1)) {
                    case -1:
                        //Error unexpected return code or no return code
                        break;
                    case 0:
                        //No logic need, continue searching for a food
                        break;
                    case 1:
                        // Add the food, pass straight back to parent activity
                        setResult(RESULT_OK, data);
                        finish();
                        break;
                }
            }
        }
    }
}
