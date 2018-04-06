package group1.fitnessapp.dietTracker;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;

import group1.fitnessapp.R;

public class EditFoodActivity extends AppCompatActivity {
    private Food originalFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        String name = getIntent().getStringExtra("selectedFoodName");
        String subText = getIntent().getStringExtra("selectedFoodSubText");
        int calories = getIntent().getIntExtra("selectedFoodCalories", 999999);
        String JSON = getIntent().getStringExtra("selectedFoodJSON");

        try {
            originalFood = new Food(name, subText, calories, JSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(originalFood.toString());
    }

    private void close(Food selected){
        Intent intent = new Intent();
        intent.putExtra("foodName", selected.getName());
        intent.putExtra("foodSubText", selected.getSubText());
        intent.putExtra("foodCalories", selected.getCalories());
        intent.putExtra("foodOriginalJSON", String.valueOf(selected.getOriginalJSON()));
        setResult(RESULT_OK, intent);
        finish();
    }
}
