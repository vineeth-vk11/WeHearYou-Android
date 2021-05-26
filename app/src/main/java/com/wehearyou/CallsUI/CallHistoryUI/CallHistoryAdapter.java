package com.wehearyou.CallsUI.CallHistoryUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wehearyou.R;

import java.util.ArrayList;

public class CallHistoryAdapter extends RecyclerView.Adapter<CallHistoryViewHolder> {

    Context context;
    ArrayList<CallHistoryModel> callHistoryModelArrayList;

    public CallHistoryAdapter(Context context, ArrayList<CallHistoryModel> callHistoryModelArrayList) {
        this.context = context;
        this.callHistoryModelArrayList = callHistoryModelArrayList;
    }

    @NonNull
    @Override
    public CallHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_call_history, parent, false);
        return new CallHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallHistoryViewHolder holder, int position) {
        holder.date.setText(callHistoryModelArrayList.get(position).getDate());
        holder.time.setText(callHistoryModelArrayList.get(position).getTime());
        holder.price.setText(callHistoryModelArrayList.get(position).getAmountPaid());
    }

    @Override
    public int getItemCount() {
        return callHistoryModelArrayList.size();
    }
}
