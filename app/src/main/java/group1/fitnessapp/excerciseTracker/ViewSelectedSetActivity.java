package group1.fitnessapp.excerciseTracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import group1.fitnessapp.R;

public class ViewSelectedSetActivity extends AppCompatActivity {

    private Set set;
    private Exercise exercise;
    private TextView repGoalSetTxt, repRemainingSetTxt, setProgressTxt;
    private String repGoal, repsRemaining, progress;
    private EditText addRepsEt, changeRepGoalEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_selected_set);
        Bundle bundle = getIntent().getExtras();
        set = (Set) bundle.getParcelable("set");
        exercise = (Exercise) bundle.getParcelable("exercise");
        repGoalSetTxt = (TextView) findViewById(R.id.repGoalSetTxt);
        repRemainingSetTxt = (TextView) findViewById(R.id.repRemainingSetTxt);
        setProgressTxt = (TextView) findViewById(R.id.setProgressTxt);
        addRepsEt = (EditText) findViewById(R.id.addRepsEt);
        changeRepGoalEt = (EditText) findViewById(R.id.changeRepGoalEt);
        setData();
        set.setExercise(exercise);
    }

    public void setData() {
        repGoal = "Current Rep Goal: " + set.getRepGoal();
        repsRemaining = "Reps Remaining: " + set.getRepsRemaining();
        progress = "Progress: " + (set.getRepGoal() - set.getRepsRemaining()) + " / " + set.getRepGoal();
        repGoalSetTxt.setText(repGoal);
        repRemainingSetTxt.setText(repsRemaining);
        setProgressTxt.setText(progress);
    }

    public void commitReps(View view) {

    }

    public void changeReps(View view) {

    }

    public void closeWindow(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}
