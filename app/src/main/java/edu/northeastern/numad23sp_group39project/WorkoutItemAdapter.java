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

    public WorkoutItemAdapter(List<WorkoutItem> cardItems) {
        this.cardItems = cardItems;
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
        holder.tvDurationMinutes.setText("Duration (minutes): " + cardItem.getDurationMinutes());
        holder.tvTotalCalories.setText("Total calories: " + cardItem.getTotalCalories());
        holder.tvUser.setText("User: " + cardItem.getUser());
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvCaloriesPerHour, tvDurationMinutes, tvTotalCalories, tvUser;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCaloriesPerHour = itemView.findViewById(R.id.tvCaloriesPerHour);
            tvDurationMinutes = itemView.findViewById(R.id.tvDurationMinutes);
            tvTotalCalories = itemView.findViewById(R.id.tvTotalCalories);
            tvUser = itemView.findViewById(R.id.tvUser);
        }
    }
}
