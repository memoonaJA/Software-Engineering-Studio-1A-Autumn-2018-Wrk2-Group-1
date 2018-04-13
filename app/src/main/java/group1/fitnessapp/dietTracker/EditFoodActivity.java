package group1.fitnessapp.dietTracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import group1.fitnessapp.R;

public class EditFoodActivity extends AppCompatActivity {
    // GUI elements
    private TextView name = null;
    private TextView subText = null;
    private TextView calories = null;
    private EditText servings = null;
    private TextView servingSize = null;
    private ImageButton back = null;
    private ImageButton save = null;
    private ImageButton delete = null;

    // Food variables
    private Food originalFood = null;
    private Food editedFood = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        // Getting GUI elements
        name = findViewById(R.id.addFoodName);
        subText = findViewById(R.id.addFoodSubText);
        calories = findViewById(R.id.displayFoodCalories);
        servings = findViewById(R.id.editServings);
        servingSize = findViewById(R.id.addFoodServingsQuantity);
        originalFood = (Food) getIntent().getSerializableExtra("foodToEdit");
        editedFood = new Food(originalFood);
        back = findViewById(R.id.addBtnBack);
        save = findViewById(R.id.btnSave);
        delete = findViewById(R.id.btnDelete);

        // Listeners
        servings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                updateFields();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFood();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFood(originalFood);
            }
        });

        // Activity start functions
        populateFields();
    }

    private void saveFood() {
        Intent intent = new Intent();
        intent.putExtra("actionCode", 1);
        intent.putExtra("editSaveFoodOriginal", originalFood);
        intent.putExtra("editSaveFoodNew", editedFood);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void deleteFood(Food food) {
        Intent intent = new Intent();
        intent.putExtra("actionCode", 0);
        intent.putExtra("editDeleteFood", food);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void populateFields() {
        name.setText(originalFood.getName());
        subText.setText(originalFood.getSubText());
        calories.setText(String.format(Locale.getDefault(), "%d", (int) originalFood.getTotalCalories()));
        servings.setText(String.format(Locale.getDefault(), "%1$,.2f", originalFood.getServings()));
        String servingQuantity = originalFood.getServingQuantity() +" " +originalFood.getServingUnit();
        servingSize.setText(servingQuantity);
    }

    private void updateFields(){
        try{
            double newServings = Double.parseDouble(servings.getText().toString());
            editedFood = editedFood.editServings(newServings);
            calories.setText(String.format(Locale.getDefault(), "%d", (int) editedFood.getTotalCalories()));
        }catch (NumberFormatException e){
            calories.setText("0");
        }
    }
}
