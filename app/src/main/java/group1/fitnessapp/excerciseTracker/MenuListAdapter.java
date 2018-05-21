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

import de.hdodenhof.circleimageview.CircleImageView;
import group1.fitnessapp.R;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuListViewHolder> {

    private Context context;
    private ArrayList<ExerciseMenuItem> list;

    public MenuListAdapter(Context context, ArrayList<ExerciseMenuItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MenuListAdapter.MenuListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_menu_item_exercise_tracker, null);
        return new MenuListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuListAdapter.MenuListViewHolder holder, int position) {
        ExerciseMenuItem menuItem = list.get(position);
        holder.itemImage.setImageResource(menuItem.getDrawableImage());
        holder.itemText.setText(menuItem.getItemText());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class MenuListViewHolder extends RecyclerView.ViewHolder {

        TextView itemText;
        CircleImageView itemImage;
        CardView cardView;

        public MenuListViewHolder(View view) {
            super(view);
            itemText = (TextView) view.findViewById(R.id.itemText);
            itemImage = (CircleImageView) view.findViewById(R.id.itemImage);
            cardView = (CardView) view.findViewById(R.id.menuCard);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewExercisesActivity.class);
                    intent.putExtra("reqCode", list.get(getAdapterPosition()).getReqCode());
                    context.startActivity(intent);
                }
            });
        }

    }
}
