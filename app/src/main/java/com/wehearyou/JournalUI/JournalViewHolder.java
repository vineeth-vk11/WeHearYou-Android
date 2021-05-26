package com.wehearyou.JournalUI;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wehearyou.R;

public class JournalViewHolder extends RecyclerView.ViewHolder {

    TextView name, topic, date;
    ImageView photo, arrow;
    CardView journal;

    public JournalViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        topic = itemView.findViewById(R.id.topic);
        date = itemView.findViewById(R.id.date);
        photo = itemView.findViewById(R.id.imageView26);
        arrow = itemView.findViewById(R.id.imageView27);

        journal = itemView.findViewById(R.id.journalCard);

    }
}
