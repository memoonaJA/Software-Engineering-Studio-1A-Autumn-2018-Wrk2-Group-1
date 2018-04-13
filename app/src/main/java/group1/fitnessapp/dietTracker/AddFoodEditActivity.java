package group1.fitnessapp.dietTracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import group1.fitnessapp.R;

public class AddFoodEditActivity extends AppCompatActivity {
    // Food variables
    private Food originalFood = null;
    private Food editedFood = null;

    // GUI elements
    private TextView name = null;
    private TextView subtext = null;
    private TextView calories = null;
    private EditText servings = null;
    private TextView servingSize = null;
    private ImageButton back = null;
    private ImageButton add = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_edit);

        // Getting data from intent
        originalFood = (Food) getIntent().getSerializableExtra("addFoodToEdit");
        editedFood = new Food(originalFood);

        // Getting all GUI elements
        name = findViewById(R.id.addFoodName);
        subtext = findViewById(R.id.addFoodSubText);
        calories = findViewById(R.id.addFoodCalories);
        servings = findViewById(R.id.addEditFoodServings);
        servingSize = findViewById(R.id.addFoodServingsQuantity);
        add = findViewById(R.id.addBtnAdd);
        back = findViewById(R.id.addBtnBack);

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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("returnCode", 0);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // Startup functions
        populateFields();
    }

    private void populateFields() {
        name.setText(originalFood.getName());
        subtext.setText(originalFood.getSubText());
        calories.setText(Integer.toString((int) originalFood.getTotalCalories()));
        servings.setText(Double.toString(originalFood.getServings()));
        String servingQuantity = originalFood.getServingQuantity() +" " +originalFood.getServingUnit();
        servingSize.setText(servingQuantity);
    }

    private void updateFields() {
        try{
            double newServings = Double.parseDouble(servings.getText().toString());
            editedFood = editedFood.editServings(newServings);
            calories.setText(Integer.toString((int)editedFood.getTotalCalories()));
        }catch (NumberFormatException e){
            System.out.println(e);
            calories.setText("0");
        }
    }

    private void addFood(){
        Intent intent = new Intent();
        intent.putExtra("returnCode", 1);
        intent.putExtra("addFoodEdited", editedFood);
        setResult(RESULT_OK, intent);
        finish();
    }
}
