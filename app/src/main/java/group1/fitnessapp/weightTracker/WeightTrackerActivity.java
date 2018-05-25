package group1.fitnessapp.weightTracker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.nio.file.WatchEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import group1.fitnessapp.R;

public class WeightTrackerActivity extends AppCompatActivity {
    //GUI Elements
    private ListView listView;
    private WeightListAdapter weightListAdapter;
    private Dialog addWeightDialog;
    private TextView dateTV;
    private EditText weightEdit;
    private Button addWeightBtn;
    private GraphView graph;

    //DB variables
    private WeightDBHandler dbHandler;

    //Variables
    private ArrayList<Weight> log;

    //Utility
    private Date currentDate = Calendar.getInstance().getTime();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("dd MM YYYY");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        log = new ArrayList<>();

        // GUI Elements
        listView = findViewById(R.id.weight_log);
        weightListAdapter =  new WeightListAdapter(this, log);
        listView.setAdapter(weightListAdapter);
        addWeightDialog = new Dialog(this);
        graph = findViewById(R.id.logGraph);
        FloatingActionButton fab = findViewById(R.id.addWeight);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });

        // Setup
        getLog();
        updateGraph();
    }

    private void showPopup() {
        addWeightDialog.setContentView(R.layout.add_weight_popup);

        // Finding GUI elements
        dateTV = addWeightDialog.findViewById(R.id.dateTV);
        weightEdit = addWeightDialog.findViewById(R.id.weightEdit);
        addWeightBtn = addWeightDialog.findViewById(R.id.addWeightBtn);

        // Setting Elements
        dateTV.setText(String.format("Date: %s", df.format(currentDate)));
        WeightDBHandler db = new WeightDBHandler(this);
        weightEdit.setText(Double.toString(db.getLastWeight()));
        weightEdit.setSelection(weightEdit.getText().length());
        addWeightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeight(new Weight(df.format(currentDate), Double.parseDouble(weightEdit.getText().toString()), "kg"));
                addWeightDialog.dismiss();
            }
        });

        addWeightDialog.show();
    }

    private void getLog() {
        WeightDBHandler dbHandler = new WeightDBHandler(this);
        log.clear();
        log.addAll(dbHandler.getLastFifty());
        weightListAdapter.notifyDataSetChanged();
        updateGraph();

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
        graph.removeAllSeries();
        DataPoint[] dataPoints = new DataPoint[log.size()];
        for (int i = 0; i < log.size(); i++){
           Weight current = log.get(i);
           Date date = null;
           try {
               System.out.println("-----------------------------------------");
               System.out.println("Date before: " +current.getLogDate());
               date = df.parse(current.getLogDate());
               System.out.println(df.format(date));
           } catch (ParseException e) {
               e.printStackTrace();
           }
           if (date != null){
            dataPoints[i] = new DataPoint(i, current.getWeight());
           }
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graph.addSeries(series);

    }

}
