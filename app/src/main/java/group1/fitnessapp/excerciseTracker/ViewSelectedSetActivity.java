package group1.fitnessapp.excerciseTracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import group1.fitnessapp.R;

public class ViewSelectedSetActivity extends AppCompatActivity {

    private Set set;
    private Exercise exercise;
    private TextView repGoalSetTxt, repRemainingSetTxt, setProgressTxt;
    private String repGoal, repsRemaining, progress;
    private EditText addRepsEt, changeRepGoalEt;
    private Button commitRepsBtn;

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
        commitRepsBtn = (Button) findViewById(R.id.commitRepsBtn);
        setData();
        set.setExercise(exercise);
        set.setContext(this);
        exercise.setContext(this);
        setTitle("Set " + set.getSetNumber());
    }

    public void setData() {
        repGoal = "Current Rep Goal: " + set.getRepGoal();
        repsRemaining = "Reps Remaining: " + set.getRepsRemaining();
        progress = "Progress: " + (set.getRepGoal() - set.getRepsRemaining()) + " / " + set.getRepGoal();
        repGoalSetTxt.setText(repGoal);
        repRemainingSetTxt.setText(repsRemaining);
        setProgressTxt.setText(progress);
        if(set.getRepsRemaining() == 0) {
            showDialogBox("This set has already been completed! If you wish to commit reps in this set please change the Rep Goal or" +
                    " go back and press the repeat button to refresh the set. Otherwise this set will automatically be reset the next day.", "Notice");
            commitRepsBtn.setClickable(false);
            commitRepsBtn.setText("Set Completed!");
        } else {
            commitRepsBtn.setClickable(true);
            commitRepsBtn.setText("Commit Reps");
        }
    }

    public void commitReps(View view) {
        try {
            String getText = addRepsEt.getText().toString();
            int num = Integer.parseInt(getText);
            if(num <= 0) {
                showDialogBox("Please enter a number greater than 0!");
            } else {
                set.commitReps(Integer.parseInt(getText));
                setData();
            }
        } catch(NumberFormatException e) {
            showDialogBox("Please enter a number greater than 0!");
        }
        //set.commitReps(Integer.parseInt(addRepsEt.getText().toString()));
        //setData();
    }

    public void showDialogBox(String message) {
        CustomDialogBoxActivity customDialog = new CustomDialogBoxActivity();
        customDialog.setDialogText(message);
        customDialog.setCustomTitle("Error");
        customDialog.show(getSupportFragmentManager(), "Error");
    }

    public void showDialogBox(String message, String title) {
        CustomDialogBoxActivity customDialog = new CustomDialogBoxActivity();
        customDialog.setDialogText(message);
        customDialog.setCustomTitle(title);
        customDialog.show(getSupportFragmentManager(), title);
    }

    public void changeReps(View view) {
        try {
            String getText = changeRepGoalEt.getText().toString();
            int num = Integer.parseInt(getText);
            if(num <= 0) {
                showDialogBox("Please enter a number greater than 0!");
            } else {
                set.changeReps(Integer.parseInt(changeRepGoalEt.getText().toString()));
                setData();
            }
        } catch(NumberFormatException e) {
            showDialogBox("Please enter a number greater than 0!");
        }
        //set.changeReps(Integer.parseInt(changeRepGoalEt.getText().toString()));
        //setData();
    }

    public void closeWindow(View view) {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}
