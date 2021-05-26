package com.wehearyou.DedicatedChatsUI;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wehearyou.R;

public class DedicatedChatsViewHolder extends RecyclerView.ViewHolder {

    TextView name, topic;
    CardView dedicatedChat;
    ImageView photo, arrow;

    public DedicatedChatsViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        topic = itemView.findViewById(R.id.topic);
        dedicatedChat = itemView.findViewById(R.id.dedicatedChatCard);
        photo = itemView.findViewById(R.id.imageView26);
        arrow = itemView.findViewById(R.id.imageView27);
    }
}
