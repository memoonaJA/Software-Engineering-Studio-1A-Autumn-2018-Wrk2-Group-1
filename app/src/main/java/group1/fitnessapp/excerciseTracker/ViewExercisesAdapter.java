package group1.fitnessapp.excerciseTracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import group1.fitnessapp.R;

public class ViewExercisesAdapter extends RecyclerView.Adapter<ViewExercisesAdapter.ViewExercisesViewHolder> {

    private Context context;
    private ArrayList<Exercise> list;
    private ExerciseTrackerDatabaseHelper helper;

    public ViewExercisesAdapter(Context context, ArrayList<Exercise> list) {
        this.context = context;
        this.list = list;
        this.helper = new ExerciseTrackerDatabaseHelper(this.context);
    }

    @Override
    public ViewExercisesAdapter.ViewExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.recycler_view_exercise, null);
        return new ViewExercisesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewExercisesAdapter.ViewExercisesViewHolder holder, int position) {
        Exercise exercise = list.get(position);
        holder.viewExName.setText(exercise.getName());
        holder.viewExDesc.setText(exercise.getDesc());
        Cursor cursor = helper.readSetsFromExercise(exercise.getId());
        holder.viewExSets.setText("Sets: " + cursor.getCount());
    }

    public void refresh(ArrayList<Exercise> list) { this.list = list; }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class ViewExercisesViewHolder extends RecyclerView.ViewHolder {
        CardView viewExCard;
        TextView viewExName, viewExDesc, viewExSets;
        ImageView deleteIv;

        public ViewExercisesViewHolder(View view) {
            super(view);
            viewExCard = (CardView) view.findViewById(R.id.viewExCard);
            viewExName = (TextView) view.findViewById(R.id.viewExName);
            viewExDesc = (TextView) view.findViewById(R.id.viewExDesc);
            viewExSets = (TextView) view.findViewById(R.id.viewExSets);
            deleteIv = (ImageView) view.findViewById(R.id.deleteIv);
            deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Exercise exercise = list.get(getAdapterPosition());
                    helper.deleteExercise(exercise.getId());
                    list.remove(exercise);
                    notifyDataSetChanged();
                }
            });
            viewExCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Exercise exercise = list.get(getAdapterPosition());
                    Intent intent = new Intent(context, ViewSetsActivity.class);
                    intent.putExtra("exercise", exercise);
                    context.startActivity(intent);
                }
            });
        }

    }

}
