package group1.fitnessapp.excerciseTracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import group1.fitnessapp.R;

public class ViewExercisesActivity extends AppCompatActivity {

    private int reqCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercises);
        reqCode = (int) getIntent().getSerializableExtra("reqCode");
        System.out.println(reqCode);
        adjudicateTitle(reqCode);
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
