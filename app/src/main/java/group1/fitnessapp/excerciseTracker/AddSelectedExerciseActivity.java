package group1.fitnessapp.excerciseTracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import group1.fitnessapp.R;

public class AddSelectedExerciseActivity extends AppCompatActivity {

    private Exercise exercise;
    private TextView exerciseName, exerciseDesc;
    private ExerciseTrackerDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_selected_exercise);
        exerciseName = (TextView) findViewById(R.id.selectedName);
        exerciseDesc = (TextView) findViewById(R.id.selectedDesc);
        exercise = new Exercise((String) getIntent().getSerializableExtra("exName"), (String) getIntent().getSerializableExtra("exDesc")
        , (String) getIntent().getSerializableExtra("category"));
        //System.out.println(exercise.getName());
        exerciseName.setText(exercise.getName());
        exerciseDesc.setText(exercise.getDesc());
        helper = new ExerciseTrackerDatabaseHelper(this);
        setTitle(exercise.getName());
    }

    public void addExercise(View view) {
        if(!exerciseExists()) {
            String category = exercise.getCategory();
            helper.insertNewExercise(exercise.getName(), exercise.getDesc(), category);
            Intent intent = new Intent(AddSelectedExerciseActivity.this, ViewExercisesActivity.class);
            switch(category) {
                case "Chest": intent.putExtra("reqCode", 0); break;
                case "Abs": intent.putExtra("reqCode", 0); break;
                case "Arms": intent.putExtra("reqCode", 1); break;
                case "Legs": intent.putExtra("reqCode", 2); break;
                case "Calves": intent.putExtra("reqCode", 2); break;
                case "Back": intent.putExtra("reqCode", 3); break;
                case "Shoulders": intent.putExtra("reqCode", 4); break;
                default: System.out.println("Unable to ascertain category!"); System.out.println(category);
            }
            helper.close();
            startActivity(intent);
        } else {
            CustomDialogBoxActivity dialog = new CustomDialogBoxActivity();
            dialog.setCustomTitle("Notice");
            dialog.setDialogText("This Exercise Already Exists!");
            dialog.show(getSupportFragmentManager(), "Notice");
        }
    }

    public boolean exerciseExists() {
        Cursor cursor = helper.readExerciseByName(exercise.getName());
        return cursor.getCount() > 0;
    }

    public boolean hasValidText(String repText) {
        try {
            Integer.parseInt(repText);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void closeWindow(View view) {
        finish();
    }
}
