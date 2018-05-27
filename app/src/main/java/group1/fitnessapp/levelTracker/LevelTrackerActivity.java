package group1.fitnessapp.levelTracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import group1.fitnessapp.R;
import group1.fitnessapp.bmiCalculator.BMICalculatorActivity;
import group1.fitnessapp.dietTracker.DietTrackerActivity;
import group1.fitnessapp.excerciseTracker.ExerciseTrackerActivity;
import group1.fitnessapp.stepCounter.StepCounterActivity;
import group1.fitnessapp.weightTracker.WeightTrackerActivity;

public class LevelTrackerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {
        
    //Other
    SensorManager sensorManager;
    boolean running = false;
    float flightsClimbed = 0;
    int samples = 0;
    int valIndex = 0;
    ArrayList<Float> recordedVals = new ArrayList<>();

    //Elements
    private EditText etBaro;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_tracker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize components
        etBaro = findViewById(R.id.etBaro);
        tvTitle = findViewById(R.id.tvTitle);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // TODO delete
        etBaro.setText("0");
        etBaro.setEnabled(false);
        etBaro.setClickable(false);
        tvTitle.setText("Level Tracker???");

        // TODO this needs to be changed to the add food fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.level_tracker, menu);
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
        startActivity(new Intent(this, ExerciseTrackerActivity.class));
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

    @Override
    protected void onResume() {
        super.onResume();
        running = true;

        String[] sensorResults = registerSensors(SENSORS_IN_USE.Pressure_Sensor);

        if (sensorResults.length > 0)
        {
            String failedSensors = String.join(", ", sensorResults);

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Phone Incompatability");
            alertDialog.setMessage("This phone does not contain the necessary sensors needed: \n" + failedSensors);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

    }

    private String[] registerSensors(SENSORS_IN_USE... sensors)
    {
        ArrayList<String> failedSensorNames = new ArrayList<>();

        for (SENSORS_IN_USE sensorToAdd : sensors)
        {
            Sensor sensor = sensorManager.getDefaultSensor(sensorToAdd.getValue());

            if (sensor != null)
            {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
            }
            else
            {
                failedSensorNames.add(sensorToAdd.name().replaceAll("_", " "));
            }
        }

        return failedSensorNames.toArray(new String[0]);
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (running)
        {
            samples++;
            etBaro.setText(String.valueOf(samples));

            if (samples >= 20)
            {
                samples = 0;
                recordedVals.add(valIndex, sensorEvent.values[0]);

                if (valIndex == 50)
                {
                    valIndex = 0;
                    analyzeData();
                }
                else
                {
                    valIndex++;
                }

            }
           // etBaro.setText(String.valueOf(flightsClimbed));
        }
    }

    private void analyzeData()
    {
        if (highest() - lowest() >= 0.1)
        {
            flightsClimbed++;
            Toast.makeText(this, String.valueOf(samples), Toast.LENGTH_SHORT).show();
        }

        recordedVals.clear();
    }

    private float lowest()
    {
        float min = recordedVals.get(0);
        for (float val: recordedVals) {
            min = val <= min ? val : min;
        }

        return min;
    }

    private float highest()
    {
        float max = recordedVals.get(0);
        for (float val: recordedVals) {
            max = val >= max ? val : max;
        }

        return max;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public enum SENSORS_IN_USE{
        Pressure_Sensor(Sensor.TYPE_PRESSURE);

        private final int value;
        SENSORS_IN_USE(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
    }
}
