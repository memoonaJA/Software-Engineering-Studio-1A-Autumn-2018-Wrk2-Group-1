package group1.fitnessapp.excerciseTracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import group1.fitnessapp.R;

public class AddSelectedExerciseActivity extends AppCompatActivity {

    private Exercise exercise;
    private TextView exerciseName, exerciseDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_selected_exercise);
        exerciseName = (TextView) findViewById(R.id.selectedName);
        exerciseDesc = (TextView) findViewById(R.id.selectedDesc);
        exercise = new Exercise((String) getIntent().getSerializableExtra("exName"), (String) getIntent().getSerializableExtra("exDesc"));
        //System.out.println(exercise.getName());
        exerciseName.setText(exercise.getName());
        exerciseDesc.setText(exercise.getDesc());
    }

    public void addExercise(View view) {

    }

    public void closeWindow(View view) {
        finish();
    }
}
