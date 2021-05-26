package com.wehearyou.TherapiesUI.TherapyBookingUI;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wehearyou.R;

public class TherapiesViewHolder extends RecyclerView.ViewHolder {

    TextView therapyName, therapyCost;
    ImageView arrow;
    CardView therapy;

    public TherapiesViewHolder(@NonNull View itemView) {
        super(itemView);

        therapyName = itemView.findViewById(R.id.therapyName);
        therapyCost = itemView.findViewById(R.id.therapyCost);
        arrow = itemView.findViewById(R.id.arrow);
        therapy = itemView.findViewById(R.id.therapy);
    }
}
