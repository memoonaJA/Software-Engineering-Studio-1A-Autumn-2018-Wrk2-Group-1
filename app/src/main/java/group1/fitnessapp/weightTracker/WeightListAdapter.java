package group1.fitnessapp.weightTracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import group1.fitnessapp.R;

public class WeightListAdapter extends ArrayAdapter<Weight>{
    private final Activity context;
    private final ArrayList<Weight> weights;

    public WeightListAdapter(Activity context, ArrayList<Weight> weights){
        super(context, R.layout.weight_list_row, weights);
        this.context = context;
        this.weights = weights;
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent){
        Weight w = weights.get(position);
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.weight_list_row, null, true);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd MMM YYYY");


        // Getting GUI elements
        TextView weightDate = rowView.findViewById(R.id.weightDate);
        TextView weight = rowView.findViewById(R.id.weight_log);

        // Setting text
        weightDate.setText(df.format(w.getLogDate()));
        weight.setText(String.format("%s %s", Double.toString(w.getWeight()), w.getUnits()));

        return rowView;
    }
}
