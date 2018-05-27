package group1.fitnessapp.weightTracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import group1.fitnessapp.R;

public class WeightTrackerActivity extends AppCompatActivity {
    private WeightListAdapter weightListAdapter;
    private Dialog addWeightDialog;
    private Dialog editWeightDialog;
    private EditText weightEdit;
    private GraphView graph;

    //Variables
    private ArrayList<Weight> log;

    //Utility
    private Date currentDate;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("dd MMM YYYY");

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentDate = new Date();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        log = new ArrayList<>();

        // GUI Elements
        ListView listView = findViewById(R.id.weight_log);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchEditWeight((Weight) parent.getItemAtPosition(position));
            }
        });
        weightListAdapter =  new WeightListAdapter(this, log);
        listView.setAdapter(weightListAdapter);
        addWeightDialog = new Dialog(this);
        editWeightDialog = new Dialog(this);
        graph = findViewById(R.id.summaryGraph);
        FloatingActionButton fab = findViewById(R.id.addWeight);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddWeight();
            }
        });

        // Setup
        getLog();
    }

    private class addWeight{
        private Date currentWorkingDate;
        private Context context;
        private TextView dateTV;

        addWeight(Context context){
            currentWorkingDate = currentDate;
            this.context = context;

            addWeightDialog.setContentView(R.layout.add_weight_popup);
            // Finding GUI elements
            dateTV = addWeightDialog.findViewById(R.id.dateTV);
            weightEdit = addWeightDialog.findViewById(R.id.weightEdit);
            ImageButton addWeightBtn = addWeightDialog.findViewById(R.id.addWeightBtn);
            ImageButton addWeightCalBtn = addWeightDialog.findViewById(R.id.addWeightCalBtn);

            // Setting Elements
            dateTV.setText(String.format("Date: %s", df.format(currentDate)));
            WeightDBHandler db = new WeightDBHandler(context);
            weightEdit.setText(Double.toString(db.getLastWeight()));
            weightEdit.setSelection(weightEdit.getText().length());

            // Listeners
            addWeightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        if (Double.parseDouble(weightEdit.getText().toString()) > 0){
                            addWeight(new Weight(currentWorkingDate.getTime(), Double.parseDouble(weightEdit.getText().toString()), "kg"));
                            addWeightDialog.dismiss();
                        }
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                }
            });
            addWeightCalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCalendar();
                }
            });

            addWeightDialog.show();
        }

        private void showCalendar() {
            DatePickerDialog calendarDialog = new DatePickerDialog(context);
            calendarDialog.show();
            calendarDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                    try {
                        currentWorkingDate = format.parse("" + dayOfMonth + " " + (month+1) + " " + year);
                        updateDateText();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void updateDateText() {
            dateTV.setText("Date: " + df.format(currentWorkingDate));
        }

    }

    private class editWeight{
        private Date currentWorkingDate;
        private Context context;
        private TextView weightDateTV;


        editWeight(Context context, final Weight weight){
            currentWorkingDate = new Date(weight.getLogDate());
            this.context = context;

            editWeightDialog.setContentView(R.layout.edit_weight_popup);

            // Finding the GUI elements
            weightDateTV = editWeightDialog.findViewById(R.id.weightDateTV);
            final EditText weightEditText = editWeightDialog.findViewById(R.id.weightEditText);
            ImageButton save = editWeightDialog.findViewById(R.id.editWeightSaveBtn);
            ImageButton delete = editWeightDialog.findViewById(R.id.editWeightDeleteBtn);
            ImageButton calender = editWeightDialog.findViewById(R.id.editWeightDateBtn);

            // Setting elements
            weightDateTV.setText(String.format("Date: %s", df.format(currentWorkingDate)));
            weightEditText.setText(Double.toString(weight.getWeight()));
            weightEditText.setSelection(weightEditText.getText().length());

            // Listeners
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (Double.parseDouble(weightEditText.getText().toString()) > 0){
                            Weight newWeight =  new Weight(
                                    currentWorkingDate.getTime(),
                                    Double.parseDouble(weightEditText.getText().toString()),
                                    "kg"
                            );
                            editWeight(weight, newWeight);
                            editWeightDialog.dismiss();
                        }
                    } catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeWeight(weight);
                    editWeightDialog.dismiss();
                }
            });
            calender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCalendar();
                }
            });

            editWeightDialog.show();
        }

        private void showCalendar() {
            DatePickerDialog calendarDialog = new DatePickerDialog(context);
            SimpleDateFormat day = new SimpleDateFormat("dd");
            SimpleDateFormat month = new SimpleDateFormat("MM");
            SimpleDateFormat year = new SimpleDateFormat("yyyy");
            int d = Integer.parseInt(day.format(currentWorkingDate));
            int m = Integer.parseInt(month.format(currentWorkingDate)) -1;
            int y = Integer.parseInt(year.format(currentWorkingDate));
            calendarDialog.updateDate(y, m, d);
            calendarDialog.show();
            calendarDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                    try {
                        currentWorkingDate = format.parse("" + dayOfMonth + " " + (month+1) + " " + year);
                        updateDateText();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void updateDateText() {
            weightDateTV.setText("Date: " + df.format(currentWorkingDate));
        }
    }

    private void launchAddWeight() {
        new addWeight(this);
    }

    private void launchEditWeight(final Weight weight) {
        new editWeight(this, weight);
    }

    private void getLog() {
        WeightDBHandler dbHandler = new WeightDBHandler(this);
        log.clear();
        log.addAll(dbHandler.getLastFifty());
        log.sort(new Comparator<Weight>() {
            @Override
            public int compare(Weight o1, Weight o2) {
                return compare(o1.getLogDate(), o2.getLogDate());
            }

            private int compare(long a, long b){
                return Long.compare(b, a);
            }
        });
        weightListAdapter.notifyDataSetChanged();
        updateGraph();
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

    private void editWeight(Weight originalWeight, Weight newWeight){
        WeightDBHandler dbHandler = new WeightDBHandler(this);
        dbHandler.editWeight(originalWeight, newWeight);
        dbHandler.close();
        getLog();
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

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));

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
