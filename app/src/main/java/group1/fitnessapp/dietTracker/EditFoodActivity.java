package group1.fitnessapp.dietTracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;

import group1.fitnessapp.R;

public class EditFoodActivity extends AppCompatActivity {
    private Food originalFood = null;
    private Food returnFood = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        String name = getIntent().getStringExtra("selectedFoodName");
        String subText = getIntent().getStringExtra("selectedFoodSubText");
        double servings = getIntent().getDoubleExtra("selectedFoodServings", 1);
        double servingQuantity = getIntent().getDoubleExtra("selectedFoodServingQuantity", 0);
        String servingUnit = getIntent().getStringExtra("selectedFoodServingUnit");
        double calories = getIntent().getDoubleExtra("selectedFoodCalories", 999999);
        String JSON = getIntent().getStringExtra("selectedFoodJSON");
        try {
            originalFood = new Food(name, subText, servings, servingQuantity, servingUnit, calories, JSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(originalFood != null){
            populateFields();
        }

        //All the buttons and there functions
        ImageButton back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton save = findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFood();
            }
        });

        ImageButton delete = findViewById(R.id.btnDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFood();
            }
        });
    }

    private void saveFood() {
        Intent intent = new Intent();
        intent.putExtra("actionCode", 1);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void deleteFood() {
        Intent intent = new Intent();
        intent.putExtra("actionCode", 0);
        intent.putExtra("foodName", originalFood.getName());
        intent.putExtra("foodSubText", originalFood.getSubText());
        intent.putExtra("foodServingQuantity", originalFood.getServingQuantity());
        intent.putExtra("foodServingUnit", originalFood.getServingUnit());
        intent.putExtra("foodCalories", originalFood.getCalories());
        intent.putExtra("foodOriginalJSON", String.valueOf(originalFood.getOriginalJSON()));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void populateFields() {
        TextView name = findViewById(R.id.displayFoodName);
        TextView subText = findViewById(R.id.displayFoodSubText);
        name.setText(originalFood.getName());
        subText.setText(originalFood.getSubText());
    }
}
