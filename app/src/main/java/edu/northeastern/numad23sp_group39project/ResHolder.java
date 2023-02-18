package edu.northeastern.numad23sp_group39project;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResHolder extends RecyclerView.ViewHolder {

    public TextView resTitle;
    public TextView resRating;
    public ResHolder(@NonNull View itemView) {
        super(itemView);
        resTitle = itemView.findViewById(R.id.res_title);
        resRating = itemView.findViewById(R.id.res_rating);
    }
}
