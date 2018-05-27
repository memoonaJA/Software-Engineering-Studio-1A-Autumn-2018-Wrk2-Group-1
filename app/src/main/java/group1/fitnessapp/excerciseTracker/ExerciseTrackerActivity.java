package group1.fitnessapp.excerciseTracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import group1.fitnessapp.R;
import group1.fitnessapp.bmiCalculator.BMICalculatorActivity;
import group1.fitnessapp.dietTracker.DietTrackerActivity;
import group1.fitnessapp.stepCounter.StepCounterActivity;
import group1.fitnessapp.weightTracker.WeightTrackerActivity;

public class ExerciseTrackerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // GUI elements
    private Toolbar toolbar = null;
    private DrawerLayout drawer = null;
    private NavigationView navigationView = null;
    private RecyclerView recyclerView;
    private MenuListAdapter adapter;
    private ArrayList<ExerciseMenuItem> menuList = new ArrayList<ExerciseMenuItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise_tracker);

        // GUI Elements
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.exerciseMenuView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        menuList.add(new ExerciseMenuItem(R.drawable.chest, "Chest", 0));
        menuList.add(new ExerciseMenuItem(R.drawable.arm, "Arm", 1));
        menuList.add(new ExerciseMenuItem(R.drawable.leg, "Leg", 2));
        menuList.add(new ExerciseMenuItem(R.drawable.back, "Back", 3));
        menuList.add(new ExerciseMenuItem(R.drawable.shoulder, "Shoulder", 4));
        adapter = new MenuListAdapter(this, menuList);
        recyclerView.setAdapter(adapter);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
        getMenuInflater().inflate(R.menu.excercise_tracker, menu);
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

    private void startWeightTracker() {
        Intent intent = new Intent(this, WeightTrackerActivity.class);
        startActivity(intent);
    }

    private void startBMITracker() {
        startActivity(new Intent(this, BMICalculatorActivity.class));
    }

    private  void startExerciseTracker(){
        //startActivity(new Intent(this, ExerciseTrackerActivity.class));
    }

    private void startStepTracker(){
        startActivity(new Intent(this, StepCounterActivity.class));
    }

    private void startLevelTracker(){
        startActivity(new Intent(this, StepCounterActivity.class));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_dietTracker) {
            startDietTracker();
        } else if (id == R.id.nav_weightTracker) {
            startWeightTracker();
        } else if (id == R.id.nav_bmiCalculator) {
            startBMITracker();
        } else if (id == R.id.nav_excerciseTracker) {
            startExerciseTracker();
        } else if (id == R.id.nav_stepTracker) {
            startStepTracker();
        } else if (id == R.id.nav_levelTracker) {
            startLevelTracker();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
