package group1.fitnessapp.dietTracker;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import group1.fitnessapp.R;
import group1.fitnessapp.bmiCalculator.BMICalculatorActivity;
import group1.fitnessapp.excerciseTracker.ExerciseTrackerActivity;
import group1.fitnessapp.stepCounter.StepCounterActivity;

public class DietTrackerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // All GUI elements of the activity
    private TextView calorieGoal;
    private TextView caloriesRemaining;
    private TextView caloriesUsed;
    private ProgressBar remainingProgressBar;
    private TextView selectedDate;

    // Food list items
    private FoodListAdapter adapt = null;
    private ArrayList<Food> foodArrayList = new ArrayList<>();
    private int goal = 0;

    // DB elements
    DietDBHandler db;

    // Utility
    private Date currentDate = Calendar.getInstance().getTime();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("dd MMM YYYY");

    //Variables
    private Date currentViewingDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_tracker);

        // Setting up activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FloatingActionButton addFood = (FloatingActionButton) findViewById(R.id.fab);
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddFood();
            }
        });

        // Goal Tracking
        calorieGoal = findViewById(R.id.goalTxtView);
        caloriesUsed = findViewById(R.id.usedTxtView);
        caloriesRemaining = findViewById(R.id.remainingTxtView);
        remainingProgressBar = findViewById(R.id.dietProgressBar);

        // Date Selection
        selectedDate = findViewById(R.id.viewingDate);
        Button incrementDate = findViewById(R.id.incrementDate);
        incrementDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(incrementDate(1));
            }
        });
        Button deIncrementDate = findViewById(R.id.deIncrementDate);
        deIncrementDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(incrementDate(-1));
            }
        });

        // Log list
        adapt =  new FoodListAdapter(this, foodArrayList);
        ListView ls = findViewById(R.id.ls_diet_food);
        ls.setAdapter(adapt);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                launchEditFood((Food) adapterView.getItemAtPosition(i));
            }
        });


        // Preparing database
        db = new DietDBHandler(this);

        // Startup setup functions
        currentViewingDate = currentDate;
        setDate(currentViewingDate);
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

    private void getLog(String date){
        foodArrayList.clear();
        ArrayList<Food> toAdd = db.getLogDate(date);
        if (toAdd != null){
            foodArrayList.addAll(toAdd);
        }
        adapt.notifyDataSetChanged();
        updateCalories();
    }

    private Date incrementDate(int n){
        if( n == 1){
            currentViewingDate = new Date(currentViewingDate.getTime() + (long) (24 * 3600 * 1000));
        }else if( n == -1){
            currentViewingDate = new Date(currentViewingDate.getTime() - (long) (24 * 3600 * 1000));
        }
        return  currentViewingDate;
    }

    private void setDate(Date date){
        Date tomorrow = new Date(currentDate.getTime() + (long) (24 * 3600 * 1000));
        Date yesterday = new Date(currentDate.getTime() - (long) (24 * 3600 * 1000));

        if (date.equals(currentDate)){
            selectedDate.setText(String.format("Today %s", df.format(date)));
        }else if(date.equals(tomorrow)){
            selectedDate.setText(String.format("Tomorrow %s", df.format(date)));
        }else if(date.equals(yesterday)){
            selectedDate.setText(String.format("Yesterday %s", df.format(date)));
        }else{
            selectedDate.setText(df.format(date));
        }
        getLog(df.format(currentViewingDate));
    }

    // Common Functions
    @SuppressLint("DefaultLocale")
    private void updateCalories() {
        getPreferences();
        calorieGoal.setText(String.format("%d", goal));

        int used = 0;
        for (int i = 0; i < adapt.getCount(); i++){
            used += Objects.requireNonNull(adapt.getItem(i)).getTotalCalories();
        }
        caloriesUsed.setText(String.format("%d", used));
        caloriesRemaining.setText(String.format("%d", (goal - used)));
        remainingProgressBar.setMax(goal);
        remainingProgressBar.setProgress(used, true);
    }

    private void addFood(Food food) {
        db.foodLogAdd(df.format(currentViewingDate), food);
        getLog(df.format(currentViewingDate));
    }

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
        db.deleteFood(foundDelete);
        getLog(df.format(currentViewingDate));
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

    // Launch activities
    private void startDietTracker() {
        // Should not start another version of itself
        //startActivity(new Intent(this, DietTrackerActivity.class));
    }

    private void startBMITracker() {
        startActivity(new Intent(this, BMICalculatorActivity.class));
    }

    private  void startExerciseTracker(){
        startActivity(new Intent(this, ExerciseTrackerActivity.class));
    }

    private void startStepTracker(){
        startActivity(new Intent(this, StepCounterActivity.class));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_dietTracker) {
            startDietTracker();
        } else if (id == R.id.nav_bmiCalculator) {
            startBMITracker();
        } else if (id == R.id.nav_excerciseTracker) {
            startExerciseTracker();
        } else if (id == R.id.nav_stepTracker) {
            startStepTracker();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
