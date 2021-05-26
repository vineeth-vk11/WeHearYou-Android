package com.wehearyou.TherapiesUI.TherapyBookingHistoryUI;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wehearyou.R;

public class TherapyBookingViewHolder extends RecyclerView.ViewHolder {

    TextView therapyName, date, cost;

    public TherapyBookingViewHolder(@NonNull View itemView) {
        super(itemView);

        therapyName = itemView.findViewById(R.id.therapyName);
        date = itemView.findViewById(R.id.dateOfBooking);
        cost = itemView.findViewById(R.id.therapyCost);

    }
}
