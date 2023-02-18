package edu.northeastern.numad23sp_group39project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResAdapter extends RecyclerView.Adapter<ResHolder> {
    private ArrayList<String[]> searchRes;
    public ResAdapter(ArrayList<String[]> res){
        this.searchRes = res;
    }
    @Override
    public ResHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_show_layout, parent, false);
        return new ResHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResHolder holder, int position) {
        String[] currentItem = searchRes.get(position);
        holder.resTitle.setText(currentItem[0]);
        holder.resRating.setText(currentItem[1]);
    }

    @Override
    public int getItemCount() {
        return searchRes.size();
    }

}
