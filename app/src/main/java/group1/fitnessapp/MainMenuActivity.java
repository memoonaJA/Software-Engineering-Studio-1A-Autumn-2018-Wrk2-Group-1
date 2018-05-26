package group1.fitnessapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import group1.fitnessapp.dietTracker.DietDBHandler;
import group1.fitnessapp.dietTracker.DietTrackerActivity;
import group1.fitnessapp.dietTracker.Food;
import group1.fitnessapp.stepCounter.StepCounterActivity;
import group1.fitnessapp.weightTracker.Weight;
import group1.fitnessapp.weightTracker.WeightDBHandler;
import group1.fitnessapp.weightTracker.WeightTrackerActivity;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //GUI elements
    private TextView calorieGoal;
    private TextView caloriesUsed;
    private TextView caloriesRemaining;
    private ProgressBar remainingProgressBar;
    private CardView dietCardView;
    private GraphView graph;

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
        graph = findViewById(R.id.summaryGraph);
        getPreferences();
        updateDietTracker();
        new weightLog(this);
    }

    // Launch activities
    private void startDietTracker() {
        Intent intent = new Intent(this, DietTrackerActivity.class);
        startActivity(intent);
    }

    private void startWeightTracker() {
        Intent intent = new Intent(this, WeightTrackerActivity.class);
        startActivity(intent);
    }

    private class weightLog{
        private ArrayList<Weight> log;
        private Context context;

        weightLog(Context context){
            WeightDBHandler db = new WeightDBHandler(context);
            log = new ArrayList<>(db.getLastFifty());
            this.context = context;
            updateGraph();
        }

        private void updateGraph() {
            ArrayList<Weight> l = new ArrayList<>(log);
            l.sort(new Comparator<Weight>() {
                @Override
                public int compare(Weight o1, Weight o2) {
                    return compare(o1.getLogDate(), o2.getLogDate());
                }
                private int compare(long a, long b){
                    return Long.compare(a, b);
                }
            });

            graph.removeAllSeries();
            DataPoint[] dataPoints = new DataPoint[l.size()];
            for (int i = 0; i < l.size(); i++){
                Weight current = l.get(i);
                dataPoints[i] = new DataPoint(new Date(current.getLogDate()), current.getWeight());
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
            graph.addSeries(series);

            // Setting axis min and max values
            // Finding the values
            double minWeight = 999999;
            double maxWeight = 0;
            long minDate = Long.MAX_VALUE;
            long maxDate = 0;
            for (Weight w : l){
                if(w.getWeight() < minWeight) minWeight = w.getWeight();
                if(w.getWeight() > maxWeight) maxWeight = w.getWeight();
                if(w.getLogDate() < minDate) minDate = w.getLogDate();
                if(w.getLogDate() > maxDate) maxDate= w.getLogDate();
            }

            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));

            // y
            graph.getViewport().setMinY(minWeight);
            graph.getViewport().setMaxY(maxWeight);
            graph.getViewport().setYAxisBoundsManual(true);

            // X
            graph.getViewport().setMinX(minDate);
            graph.getViewport().setMaxX(maxDate);
            graph.getViewport().setXAxisBoundsManual(true);

            graph.getGridLabelRenderer().setHumanRounding(false, true);

        }
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
        db.close();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dietTracker) {
            startDietTracker();
        } else if (id == R.id.nav_weightTracker) {
            startWeightTracker();
        } else if (id == R.id.nav_excerciseTracker) {

        } else if (id == R.id.nav_stepTracker) {
            Intent intent = new Intent(this, StepCounterActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
