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

import group1.fitnessapp.R;

public class EditFoodActivity extends AppCompatActivity {
    private Food originalFood = null;
    private Food editedFood = null;

    private TextView name = null;
    private TextView subText = null;
    private TextView calories = null;
    private EditText servings = null;
    private TextView servingSize = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        name = findViewById(R.id.addFoodName);
        subText = findViewById(R.id.addFoodSubText);
        calories = findViewById(R.id.displayFoodCalories);
        servings = findViewById(R.id.editServings);
        servingSize = findViewById(R.id.addFoodServingsQuantity);
        originalFood = (Food) getIntent().getSerializableExtra("foodToEdit");
        editedFood = new Food(originalFood);
        populateFields();

        // Edit text fields update logic
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


        // All the buttons and there functions
        ImageButton back = findViewById(R.id.addBtnBack);
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
                deleteFood(originalFood);
            }
        });
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
        calories.setText(Integer.toString((int) originalFood.getTotalCalories()));
        servings.setText(Double.toString(originalFood.getServings()));
        String servingQuantity = originalFood.getServingQuantity() +" " +originalFood.getServingUnit();
        servingSize.setText(servingQuantity);
    }

    private void updateFields(){
        try{
            double newServings = Double.parseDouble(servings.getText().toString());
            editedFood = editedFood.editServings(newServings);
            calories.setText(Integer.toString((int)editedFood.getTotalCalories()));
        }catch (NumberFormatException e){
            System.out.println(e);
            calories.setText("0");
        }
    }
}
