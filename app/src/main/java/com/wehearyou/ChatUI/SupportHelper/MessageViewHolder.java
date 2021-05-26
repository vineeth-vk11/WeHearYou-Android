package com.wehearyou.ChatUI.SupportHelper;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wehearyou.R;


public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView message;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        message = itemView.findViewById(R.id.message);
    }
}
