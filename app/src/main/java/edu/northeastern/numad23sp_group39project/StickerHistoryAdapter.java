package edu.northeastern.numad23sp_group39project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StickerHistoryAdapter extends RecyclerView.Adapter<StickerHistoryAdapter.ViewHolder> {

    private ArrayList<StickerHistoryList> hisRes;

    public StickerHistoryAdapter(ArrayList<StickerHistoryList> res){
        this.hisRes = res;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fromUser;
        private TextView timeReceived;
        private ImageView receivedImage;
        public ViewHolder(final View view) {
            super(view);
            fromUser = view.findViewById(R.id.fromWhichUser);
            timeReceived = view.findViewById(R.id.timeStickerSent);
            receivedImage = view.findViewById(R.id.imageReceived);
        }
    }

    @NonNull
    @Override
    public StickerHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_history_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String userSticker = hisRes.get(position).getUserSticker();
        String fromUser = hisRes.get(position).getFromUser();
        String timeReceived = hisRes.get(position).getTimeReceived();

        if (userSticker.equals("smile.png")) {
            holder.receivedImage.setImageResource(R.drawable.smile);
        } else if (userSticker.equals("angry.png")) {
            holder.receivedImage.setImageResource(R.drawable.angry);
        } else {
            holder.receivedImage.setImageResource(R.drawable.cry);
        }
        holder.fromUser.setText(fromUser);
        holder.timeReceived.setText(timeReceived);
    }


    @Override
    public int getItemCount() {
        return hisRes.size();
    }
}
