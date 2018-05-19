package group1.fitnessapp.excerciseTracker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import group1.fitnessapp.R;

public class ViewSetsActivity extends AppCompatActivity implements AddSetDialogBoxActivity.AddSetDialogListener {

    private Exercise exercise;
    private ExerciseTrackerDatabaseHelper helper;
    private RecyclerView recyclerView;
    private ViewSetsAdapter adapter;
    private TextView totalRepsTxt;
    private String reps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sets);
        Bundle bundle = getIntent().getExtras();
        exercise = (Exercise) bundle.getParcelable("exercise");
        setTitle(exercise.getName());
        helper = new ExerciseTrackerDatabaseHelper(this);
        setData();
        adapter = new ViewSetsAdapter(this, exercise.getSets());
        recyclerView = (RecyclerView) findViewById(R.id.setsView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        reps = exercise.getTotalRepsDone() + "";
        totalRepsTxt = findViewById(R.id.totalRepsTxt);
        totalRepsTxt.setText(reps);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
        adapter.refresh(exercise.getSets());
        recyclerView.setAdapter(adapter);
        reps = exercise.getTotalRepsDone() + "";
        totalRepsTxt.setText(reps);
    }

    public void setData() {
        Cursor cursor = helper.readSetsFromExercise(exercise.getId());
        ArrayList<Set> list = exercise.getSets();
        if(cursor.getCount() > 0) {
            list.clear();
            while(cursor.moveToNext()) {
                Set set = new Set(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), this.exercise, new Date(cursor.getLong(4)));
                exercise.addFullSet(set);
            }
        }
    }

    public void openAddSetDialog(View view) {
        AddSetDialogBoxActivity dialog = new AddSetDialogBoxActivity();
        dialog.show(getSupportFragmentManager(), "Add Set");
    }

    @Override
    public void sendText(String text) {
        int setNo = exercise.getSets().size() + 1;
        int repGoal = Integer.parseInt(text);
        Date date = new Date();
        helper.insertNewSet(setNo, repGoal, repGoal, exercise.getId(), date.getTime());
        onResume();
    }
}
