package group1.fitnessapp.dietTracker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import group1.fitnessapp.R;

/**
 * Created by aaron on 1/04/2018.
 */

public class FoodListAdapter extends ArrayAdapter<Food>{
    private final Activity context;
    private final ArrayList<Food> food;

    public FoodListAdapter(Activity context,
                        ArrayList<Food> food) {
        super(context, R.layout.diet_list_row, food);
        this.context = context;
        this.food = food;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.diet_list_row, null, true);
        TextView foodName = (TextView) rowView.findViewById(R.id.txt_food_name);
        TextView foodSubTxt = (TextView) rowView.findViewById(R.id.txt_food_subtxt);
        TextView foodCalories = (TextView) rowView.findViewById(R.id.txt_food_calories);

        foodName.setText(food.get(position).getName());
        foodSubTxt.setText(food.get(position).getSubText());
        foodCalories.setText(Integer.toString(food.get(position).getCalories()));

        return rowView;
    }
}