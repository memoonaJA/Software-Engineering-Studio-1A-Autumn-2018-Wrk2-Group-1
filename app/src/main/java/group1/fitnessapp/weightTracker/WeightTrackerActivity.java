package group1.fitnessapp.weightTracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import group1.fitnessapp.R;

public class WeightTrackerActivity extends AppCompatActivity {
    //GUI Elements
    private ListView listView;
    private WeightListAdapter weightListAdapter;

    //DB variables
    private WeightDBHandler dbHandler;

    //Variables
    private ArrayList<Weight> log;

    //Utility
    private Date currentDate = Calendar.getInstance().getTime();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("dd MMM YYYY");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.addWeight);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddWeight();
            }
        });

        // GUI Elements
        listView = findViewById(R.id.weight_log);
        weightListAdapter =  new WeightListAdapter(this, log);
        listView.setAdapter(weightListAdapter);

        // DB
        dbHandler = new WeightDBHandler(this);

        // Setup
        getLog();
        updateGraph();
    }

    private void getLog() {
        generateTestData();
    }

    private void generateTestData() {
        log = new ArrayList<>();
        log.add(new Weight(currentDate, 110, "Kg"));
        weightListAdapter.notifyDataSetChanged();
    }

    private void updateGraph() {

    }

    private void launchAddWeight() {

    }

}
