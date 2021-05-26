package com.wehearyou.CallsUI.CallHistoryUI;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wehearyou.R;

public class CallHistoryViewHolder extends RecyclerView.ViewHolder {

    TextView date, time, price;

    public CallHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        date = itemView.findViewById(R.id.date);
        time = itemView.findViewById(R.id.time);
        price = itemView.findViewById(R.id.cost);
    }
}
