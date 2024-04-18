package com.example.artermis2.Component.Clock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artermis2.R;

public class MinuteAdapter extends RecyclerView.Adapter<MinuteAdapter.MinuteViewHolder> {

    public int DefaultPosition;

    public MinuteAdapter(int DefaultPosition){
        this.DefaultPosition = DefaultPosition;
    }
    // Phương thức setter để thiết lập vị trí mặc định

    @NonNull
    @Override
    public MinuteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_minute, parent, false);
        MinuteViewHolder minuteViewHolder = new MinuteViewHolder(itemView);
        return minuteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MinuteViewHolder holder, int position) {

        holder.bindMinute(position%60);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public static class MinuteViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMinute;

        public MinuteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMinute = itemView.findViewById(R.id.textViewMinute);
        }

        public void bindMinute(int minute) {
            textViewMinute.setText(String.format("%02d", minute)); // Định dạng số giờ thành chuỗi với hai chữ số

        }
    }
}
