package com.wehearyou.FindListenerUI.TopicSelectionUI;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wehearyou.R;

public class TopicsViewHolder extends RecyclerView.ViewHolder {

    TextView topicName, topicTalkingNumber;
    CardView topic;
    ImageView arrow;

    public TopicsViewHolder(@NonNull View itemView) {
        super(itemView);

        topicName = itemView.findViewById(R.id.topicName);
        topicTalkingNumber = itemView.findViewById(R.id.topicTakingNumber);
        topic = itemView.findViewById(R.id.topic);
        arrow = itemView.findViewById(R.id.arrow);
    }
}
