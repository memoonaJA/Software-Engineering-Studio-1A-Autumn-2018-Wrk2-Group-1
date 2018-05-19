package group1.fitnessapp.excerciseTracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import group1.fitnessapp.R;

public class ViewExercisesActivity extends AppCompatActivity {

    private int reqCode;
    private ExerciseTrackerDatabaseHelper helper;
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ViewExercisesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercises);
        reqCode = (int) getIntent().getSerializableExtra("reqCode");
        //System.out.println(reqCode);
        adjudicateTitle(reqCode);
        helper = new ExerciseTrackerDatabaseHelper(this);
        setData(reqCode);
        adapter = new ViewExercisesAdapter(this, exerciseList);
        recyclerView = (RecyclerView) findViewById(R.id.exerciseViewRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData(reqCode);
        adapter.refresh(exerciseList);
        recyclerView.setAdapter(adapter);
    }

    public void setData(int reqCode) {
        Cursor cursor;
        switch(reqCode) {
            case 0: cursor = helper.readExercisesByCategory("Chest"); break;
            case 1: cursor = helper.readExercisesByCategory("Arms"); break;
            case 2: cursor = helper.readExercisesByCategory("Legs"); break;
            case 3: cursor = helper.readExercisesByCategory("Back"); break;
            case 4: cursor = helper.readExercisesByCategory("Shoulders"); break;
            default: System.out.println("Failed to attain reqCode"); cursor = null;
        }
        if(cursor.getCount() > 0) {
            exerciseList.clear();
            while(cursor.moveToNext()) {
                exerciseList.add(new Exercise(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getInt(4)) );
            }
        }
    }

    public void adjudicateTitle(int reqCode) {
        switch(reqCode) {
            case 0: setTitle("Chest Exercises"); break;
            case 1: setTitle("Arm Exercises"); break;
            case 2: setTitle("Leg Exercises"); break;
            case 3: setTitle("Back Exercises"); break;
            case 4: setTitle("Shoulder Exercises"); break;
            default: setTitle("");
        }
    }

    public void openAddExercisesActivity(View view) {
        Intent intent = new Intent(ViewExercisesActivity.this, AddExerciseActivity.class);
        intent.putExtra("reqCode", reqCode);
        startActivity(intent);
    }
}
