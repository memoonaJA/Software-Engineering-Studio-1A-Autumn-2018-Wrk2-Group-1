package group1.fitnessapp.excerciseTracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import group1.fitnessapp.R;

public class ViewSetsAdapter extends RecyclerView.Adapter<ViewSetsAdapter.ViewSetsViewHolder> {

    private Context context;
    private ArrayList<Set> list;
    private ExerciseTrackerDatabaseHelper helper;

    public ViewSetsAdapter(Context context, ArrayList<Set> list) {
        this.context = context;
        this.list = list;
        this.helper = new ExerciseTrackerDatabaseHelper(context);
    }

    @Override
    public ViewSetsAdapter.ViewSetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.recycler_view_sets, null);
        return new ViewSetsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewSetsAdapter.ViewSetsViewHolder holder, int position) {
        Set set = list.get(position);
        int difference = set.getRepGoal() - set.getRepsRemaining();
        String setNo = "Set " + set.getSetNumber();
        String repGoal = "Reps Completed: " + difference + "/" + set.getRepGoal();
        holder.setNoTxt.setText(setNo);
        holder.repGoalTxt.setText(repGoal);
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        holder.lastModifiedTxt.setText(dateFormat.format(set.getLastModified()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refresh(ArrayList<Set> list) { this.list = list; }

    class ViewSetsViewHolder extends RecyclerView.ViewHolder {

        TextView setNoTxt, repGoalTxt, lastModifiedTxt;
        CardView setCard;
        ImageView repeatIv, deleteSetIv;

        public ViewSetsViewHolder(View view) {
            super(view);
            setNoTxt = (TextView) view.findViewById(R.id.setNoTxt);
            repGoalTxt = (TextView) view.findViewById(R.id.repGoalTxt);
            lastModifiedTxt = (TextView) view.findViewById(R.id.lastModifiedTxt);
            repeatIv = (ImageView) view.findViewById(R.id.repeatIv);
            repeatIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Set set = list.get(getAdapterPosition());
                    //set.setContext(context);
                    set.changeReps(set.getRepGoal());
                    notifyDataSetChanged();
                }
            });
            deleteSetIv = (ImageView) view.findViewById(R.id.deleteSetIv);
            deleteSetIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Set set = list.get(getAdapterPosition());
                    //set.setContext(context);
                    helper.deleteSet(set.getId());
                    set.getExercise().removeSet(set);
                    notifyDataSetChanged();
                }
            });
            setCard = (CardView) view.findViewById(R.id.setCard);
            setCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Set set = list.get(getAdapterPosition());
                    Intent intent = new Intent(context, ViewSelectedSetActivity.class);
                    intent.putExtra("set", set);
                    intent.putExtra("exercise", set.getExercise());
                    context.startActivity(intent);
                }
            });
        }

    }

}
