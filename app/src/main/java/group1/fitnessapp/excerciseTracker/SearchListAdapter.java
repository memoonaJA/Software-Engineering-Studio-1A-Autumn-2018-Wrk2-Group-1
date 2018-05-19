package group1.fitnessapp.excerciseTracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import group1.fitnessapp.R;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder> {

    private Context context;
    private ArrayList<Exercise> list;

    public SearchListAdapter(Context context, ArrayList<Exercise> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public SearchListAdapter.SearchListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.recycler_search_exercise, null);
        return new SearchListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchListAdapter.SearchListViewHolder holder, int position) {
        Exercise exercise = list.get(position);
        holder.exerciseName.setText(exercise.getName());
        holder.exerciseDescription.setText(exercise.getDesc());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class SearchListViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseName, exerciseDescription;
        CardView cardView;

        public SearchListViewHolder(View view) {
            super(view);
            exerciseName = (TextView) view.findViewById(R.id.apiExerciseName);
            exerciseDescription = (TextView) view.findViewById(R.id.apiExerciseDescription);
            cardView = (CardView) view.findViewById(R.id.resultCard);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddSelectedExerciseActivity.class);
                    intent.putExtra("exName", list.get(getAdapterPosition()).getName());
                    intent.putExtra("exDesc", list.get(getAdapterPosition()).getDesc());
                    intent.putExtra("category", list.get(getAdapterPosition()).getCategory());
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setSearchOperation(ArrayList<Exercise> list) {
        this.list = new ArrayList<>();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
