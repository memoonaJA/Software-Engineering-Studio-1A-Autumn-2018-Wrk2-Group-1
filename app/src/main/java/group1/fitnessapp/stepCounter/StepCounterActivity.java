package group1.fitnessapp.stepCounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;

import group1.fitnessapp.R;
import group1.fitnessapp.bmiCalculator.BMICalculatorActivity;
import group1.fitnessapp.dietTracker.DietTrackerActivity;
import group1.fitnessapp.excerciseTracker.ExerciseTrackerActivity;
import group1.fitnessapp.weightTracker.WeightTrackerActivity;

public class StepCounterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    //Other
    SensorManager sensorManager;
    boolean running = false;
    float stepCountForReset = 0;
    float stepCountSinceLastReset;
    float stepCountSinceOnline;

    //Elements
    private TextView tvStepCount;
    private TextView stepsLeft;
    private CircularProgressBar stepProgress;
    private ProgressBar dailyProgress;
    private TextView etStepCount;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize components

        etStepCount = findViewById(R.id.etBaro);
        btnReset = findViewById(R.id.btnReset);
        tvStepCount = findViewById(R.id.txtSteps);
        dailyProgress = findViewById(R.id.progressBar8);
        stepProgress = findViewById(R.id.stepsProgress);
        stepsLeft = findViewById(R.id.txtStepsLeft);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

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
        stepCountSinceLastReset = 0;
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
        getMenuInflater().inflate(R.menu.step_counter, menu);
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
        //startActivity(new Intent(this, StepCounterActivity.class));
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
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;

        String[] sensorResults = registerSensors(SENSORS_IN_USE.Step_Counter);

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
            stepCountSinceOnline = sensorEvent.values[0];
            stepCountSinceLastReset = sensorEvent.values[0] - stepCountForReset;
            String value = String.valueOf(stepCountSinceLastReset);
            tvStepCount.setText(value.substring(0, value.length() - 2));
            String left = Float.toString((1200 - stepCountSinceLastReset));
            stepsLeft.setText(left.substring(0, left.length() - 2) + " STEPS LEFT");
            stepProgress.setProgressWithAnimation(Math.round((float)stepCountSinceLastReset/1200 * 100));
            dailyProgress.setProgress(Math.round(stepCountSinceLastReset/1200 * 100), true);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public enum SENSORS_IN_USE{
        Step_Counter(Sensor.TYPE_STEP_COUNTER);

        private final int value;

        SENSORS_IN_USE(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
    }
}
