package edu.northeastern.numad23sp_group39project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkoutItemAdapter extends RecyclerView.Adapter<WorkoutItemAdapter.CardViewHolder> {

    private List<WorkoutItem> cardItems;
    private OnItemClickListener onItemClickListener;

    public WorkoutItemAdapter(List<WorkoutItem> cardItems, OnItemClickListener onItemClickListener) {
        this.cardItems = cardItems;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_workout_card, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        WorkoutItem cardItem = cardItems.get(position);
        holder.tvName.setText(cardItem.getName());
        holder.tvCaloriesPerHour.setText("Calories per hour: " + cardItem.getCaloriesPerHour());
        holder.tvDurationMinutes.setText("Recommend Duration: " + cardItem.getDurationMinutes() + " mins");
        holder.tvTotalCalories.setText("Total calories: " + cardItem.getTotalCalories());
        holder.tvUser.setText("Type: " + cardItem.getType());
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvName, tvCaloriesPerHour, tvDurationMinutes, tvTotalCalories, tvUser;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCaloriesPerHour = itemView.findViewById(R.id.tvCaloriesPerHour);
            tvDurationMinutes = itemView.findViewById(R.id.tvDurationMinutes);
            tvTotalCalories = itemView.findViewById(R.id.tvTotalCalories);
            tvUser = itemView.findViewById(R.id.tvUser);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
