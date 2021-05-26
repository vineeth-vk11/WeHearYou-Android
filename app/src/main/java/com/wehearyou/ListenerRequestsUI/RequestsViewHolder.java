package com.wehearyou.ListenerRequestsUI;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wehearyou.R;

public class RequestsViewHolder extends RecyclerView.ViewHolder {

    ImageButton accept, reject;
    TextView name, topic;
    ImageView image;

    public RequestsViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        topic = itemView.findViewById(R.id.topic);
        accept = itemView.findViewById(R.id.accept);
        image = itemView.findViewById(R.id.imageView26);
    }
}
