package group1.fitnessapp.dietTracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import group1.fitnessapp.R;

public class DietTrackerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // All GUI elements of the activity
    private ProgressBar remainingProgressBar = null;
    private ListView ls = null;

    // Food list items
    private FoodListAdapter adapt = null;
    private ArrayList<Food> foodArrayList = new ArrayList<>();
    private int goal = 0;

    // DB elements
    DietDBHandler db;

    // Utility
    private Date c = Calendar.getInstance().getTime();
    private SimpleDateFormat df = new SimpleDateFormat("dd MMM YYYY");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_tracker);

        // Finding GUI Elements
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        remainingProgressBar = findViewById(R.id.dietProgressBar);
        adapt =  new FoodListAdapter(this, foodArrayList);
        ls = (ListView) findViewById(R.id.ls_diet_food);
        ls.setAdapter(adapt);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FloatingActionButton addFood = (FloatingActionButton) findViewById(R.id.fab);

        //Listeners
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                launchEditFood((Food) adapterView.getItemAtPosition(i));
            }
        });
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddFood();
            }
        });

        // Preparing database
        db = new DietDBHandler(this);

        // Startup setup functions
        //generateTestData();
        getLog(df.format(c));
        getPreferences();
        updateCalories();
    }

    // Startup Functions
    private void getPreferences() {
        SharedPreferences preferences = getSharedPreferences("dietTracker", Context.MODE_PRIVATE);
        if (preferences.contains("userGoal")){
            goal = preferences.getInt("userGoal", 99999);
        }else{
            goal = preferences.getInt("defaultGoal", 99999);
        }
    }

    // TODO will have to be removed when db is functioning
    private void generateTestData() {
        foodArrayList.add(new Food("Peach Rings", "JuiceFuls",1, 50, "g", 238));
        foodArrayList.add(new Food("Almond Apple Cookie", "BreadSmith",2, 1.0, "cookie", 79));

    }

    private void getLog(String date){
        ArrayList<Food> toAdd = db.getLogDate(date);
        if (toAdd != null){
            foodArrayList.addAll(toAdd);
        }
    }

    // Common Functions
    private void updateCalories() {
        // Calorie goal
        TextView calorieGoal = findViewById(R.id.goalTxtView);
        calorieGoal.setText(String.format("%d", goal));

        // Calorie Used
        int used = 0;
        TextView caloriesUsed = findViewById(R.id.usedTxtView);
        for (int i = 0; i < adapt.getCount(); i++){
            used += adapt.getItem(i).getTotalCalories();
        }
        caloriesUsed.setText(String.format("%d", used));

        // Calories Remaining
        TextView caloriesRemaining = findViewById(R.id.remainingTxtView);
        caloriesRemaining.setText(String.format("%d", (goal - used)));

        //Update Progress Bar
        remainingProgressBar.setMax(goal);
        remainingProgressBar.setProgress(used, true);
    }

    // TODO update the db with new food added
    private void addFood(Food food) {
        foodArrayList.add(food);
        adapt.notifyDataSetChanged();
        updateCalories();
        db.foodLogAdd(df.format(c), food);
    }

    // TODO update db with new food removed
    private void removeFood (Food toDelete){
        Food foundDelete = null;
        for(Food f : foodArrayList){
            if (f.getName().equalsIgnoreCase(toDelete.getName())){
                if (f.getSubText().equalsIgnoreCase(toDelete.getSubText())){
                    if (f.getTotalCalories()== toDelete.getTotalCalories()){
                        foundDelete = f;
                        break;
                    }
                }
            }
        }
        if(!foodArrayList.remove(foundDelete)){
            System.out.println("Delete failed no match found");
        }
        adapt.notifyDataSetChanged();
        updateCalories();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    //Launch activity functions
    private void launchAddFood() {
        Intent intent = new Intent(this, DietAddFoodActivity.class);
        startActivityForResult(intent, 1);
    }

    private void launchEditFood(Food food) {
        Intent intent = new Intent(this, EditFoodActivity.class);
        intent.putExtra("foodToEdit", food);
        startActivityForResult(intent, 3);
    }

    // Logic for return data from activities
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Add food return logic
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                addFood((Food) data.getSerializableExtra("addFoodEdited"));
                showToast("Food saved");
            }
        }
        // Change settings return logic
        if(requestCode == 2) {
            if(resultCode == RESULT_OK){
                getPreferences();
                updateCalories();
                showToast("Settings saved");
            }
        }

        // Edit or remove food return logic
        if(requestCode == 3) {
            if(resultCode == RESULT_OK){
                switch (data.getIntExtra("actionCode", -1)){
                    case -1:
                        // Error wasn't able to get an action code
                        showToast("Error editing food");
                        break;
                    case 0:
                        // Delete the food item
                        removeFood((Food) data.getSerializableExtra("editDeleteFood"));
                        showToast("Food deleted");
                        break;
                    case 1:
                        //Edit the food item
                        removeFood((Food) data.getSerializableExtra("editSaveFoodOriginal"));
                        addFood((Food) data.getSerializableExtra("editSaveFoodNew"));
                        updateCalories();
                        showToast("Food saved");
                        break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Misc Menu functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.diet_tracker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, DietTrackerSettingsActivity.class);
            startActivityForResult(intent, 2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dietTracker) {
            // A new instance of this activity shouldn't be made again
        } else if (id == R.id.nav_excerciseTracker) {

        } else if (id == R.id.nav_stepTracker) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
