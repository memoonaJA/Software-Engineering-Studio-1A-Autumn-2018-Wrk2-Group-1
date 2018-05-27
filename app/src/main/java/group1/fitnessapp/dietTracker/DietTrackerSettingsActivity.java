package group1.fitnessapp.dietTracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import group1.fitnessapp.R;

public class DietTrackerSettingsActivity extends AppCompatActivity {
    // GUI elements
    private TextView currentGoal = null;
    private Button settingsSave = null;

    //Preferences
    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_tracker_settings);

        // Getting GUI elements
        currentGoal = findViewById(R.id.currentGoalText);
        settingsSave = findViewById(R.id.saveSettingsBtn);

        //Getting and setting current currentGoal
        preferences = getSharedPreferences("dietTracker", Context.MODE_PRIVATE);
        int currentGoal1;
        if (preferences.contains("userGoal")){
            currentGoal1 = preferences.getInt("userGoal", 99999);
        }else{
            currentGoal1 = preferences.getInt("defaultGoal", 99999);
        }
        currentGoal.setText(String.format(Locale.getDefault(),"%d", currentGoal1));

        //Listeners
        settingsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.editGoalEditText);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("userGoal", Integer.parseInt(editText.getText().toString()));
                editor.apply();
                close();
            }
        });
    }

    private void close() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
