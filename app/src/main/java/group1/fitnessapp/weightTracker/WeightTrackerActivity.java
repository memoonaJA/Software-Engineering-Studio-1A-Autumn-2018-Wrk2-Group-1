package group1.fitnessapp.weightTracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

        log = new ArrayList<>();

        // GUI Elements
        listView = findViewById(R.id.weight_log);
        weightListAdapter =  new WeightListAdapter(this, log);
        listView.setAdapter(weightListAdapter);

        // Setup
        getLog();
        updateGraph();
    }

    private void getLog() {
        generateTestData();
//        WeightDBHandler dbHandler = new WeightDBHandler(this);
//        log.clear();
//        log = dbHandler.getLastFifty();
//        weightListAdapter.notifyDataSetChanged();
//        updateGraph();

    }

    private void generateTestData() {
        log.add(new Weight(df.format(currentDate), 110, "Kg"));
        weightListAdapter.notifyDataSetChanged();
    }

    private void addWeight(Weight weight){
        WeightDBHandler dbHandler = new WeightDBHandler(this);
        dbHandler.addWeight(weight);
        dbHandler.close();
        getLog();
    }

    private void removeWeight(Weight weight){
        WeightDBHandler dbHandler = new WeightDBHandler(this);
        dbHandler.deleteWeight(weight);
        dbHandler.close();
        getLog();
    }

    private void updateGraph() {

    }

    private void launchAddWeight() {

    }

}
