package group1.fitnessapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import group1.fitnessapp.bmiCalculator.BMICalculatorActivity;
import group1.fitnessapp.dietTracker.DietDBHandler;
import group1.fitnessapp.dietTracker.DietTrackerActivity;
import group1.fitnessapp.excerciseTracker.ExerciseTrackerActivity;
import group1.fitnessapp.dietTracker.Food;
import group1.fitnessapp.stepCounter.StepCounterActivity;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //GUI elements
    private TextView calorieGoal;
    private TextView caloriesUsed;
    private TextView caloriesRemaining;
    private ProgressBar remainingProgressBar;
    private CardView dietCardView;

    // Diet Tracker variables
    int goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        generatePreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Diet Tracker GUI elements
        calorieGoal = findViewById(R.id.goalTxtView);
        caloriesUsed = findViewById(R.id.usedTxtView);
        caloriesRemaining = findViewById(R.id.remainingTxtView);
        remainingProgressBar = findViewById(R.id.dietProgressBar);
        dietCardView = findViewById(R.id.dietCardView);
        dietCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDietTracker();
            }
        });
        getPreferences();
        updateDietTracker();
    }

    private void updateDietTracker() {
        calorieGoal.setText(String.format("%d", goal));
        int used = 0;
        Date currentDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd MMM YYYY");
        DietDBHandler db = new DietDBHandler(this);
        ArrayList<Food> log = db.getLogDate(df.format(currentDate));
        for (Food f : log){
            used += f.getCalories();
        }
        caloriesUsed.setText(String.format("%d", used));
        caloriesRemaining.setText(String.format("%d", (goal - used)));
        remainingProgressBar.setMax(goal);
        remainingProgressBar.setProgress(used, true);
    }

    private void getPreferences() {
        SharedPreferences preferences = getSharedPreferences("dietTracker", Context.MODE_PRIVATE);
        if (preferences.contains("userGoal")){
            goal = preferences.getInt("userGoal", 99999);
        }else{
            goal = preferences.getInt("defaultGoal", 99999);
        }
    }

    private void generatePreferences() {
        System.out.println("generatePreferences");
        SharedPreferences sharedPreferences =  getSharedPreferences("dietTracker", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("defaultGoal", 1500);
        editor.apply();
        System.out.println("done");
    }

    @Override
    public void onResume(){
        super.onResume();
        updateDietTracker();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Launch activities via cards
    private void startDietTracker() {
        startActivity(new Intent(this, DietTrackerActivity.class));
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
