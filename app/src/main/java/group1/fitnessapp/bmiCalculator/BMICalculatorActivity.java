package group1.fitnessapp.bmiCalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import group1.fitnessapp.R;
import group1.fitnessapp.dietTracker.DietDBHandler;
import group1.fitnessapp.weightTracker.WeightDBHandler;

public class BMICalculatorActivity extends AppCompatActivity {
    // GUI ELEMENTS
    private EditText height;
    private EditText weight;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmicalculator);

        //Getting GUI elements
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        result = findViewById(R.id.result);
        Button calculateBMI = findViewById(R.id.calc);

        // Listeners
        calculateBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String heightStr = height.getText().toString();
                String weightStr = weight.getText().toString();

                if (!"".equals(heightStr) && !"".equals(weightStr)) {
                    try{
                        if (Float.parseFloat(heightStr) > 0 && Float.parseFloat(weightStr) > 0){
                            float heightValue = Float.parseFloat(heightStr) / 100;
                            float weightValue = Float.parseFloat(weightStr);
                            float bmi = weightValue / (heightValue * heightValue);

                            displayBMI(bmi);
                        }
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        // Setup
        // Get last weight from the log
        WeightDBHandler db = new WeightDBHandler(this);
        double lastWeight = db.getLastWeight();
        if (lastWeight > 0){
            weight.setText(Double.toString(lastWeight));
        }

    }

    private void displayBMI(float bmi) {
        String bmiLabel;

        if (Float.compare(bmi, 15f) <= 0) {
            bmiLabel = "Very Severely Underweight";
        } else if (Float.compare(bmi, 15f) > 0  &&  Float.compare(bmi, 16f) <= 0) {
            bmiLabel = "Severely Underweight";
        } else if (Float.compare(bmi, 16f) > 0  &&  Float.compare(bmi, 18.5f) <= 0) {
            bmiLabel = "Underweight";
        } else if (Float.compare(bmi, 18.5f) > 0  &&  Float.compare(bmi, 25f) <= 0) {
            bmiLabel = "Normal";
        } else if (Float.compare(bmi, 25f) > 0  &&  Float.compare(bmi, 30f) <= 0) {
            bmiLabel = "Overweight";
        } else if (Float.compare(bmi, 30f) > 0  &&  Float.compare(bmi, 35f) <= 0) {
            bmiLabel = "Obese Class I";
        } else if (Float.compare(bmi, 35f) > 0  &&  Float.compare(bmi, 40f) <= 0) {
            bmiLabel = "Obese Class II";
        } else {
            bmiLabel = "Obese Class III";
        }
        DecimalFormat twoDP = new DecimalFormat("##.00");
        bmiLabel = "Your BMI is " + twoDP.format(bmi) + "\n" + bmiLabel;
        result.setText(bmiLabel);
    }
}
