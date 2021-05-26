package com.wehearyou.DedicatedChatsUI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.wehearyou.ChatUI.ChatActivity;
import com.wehearyou.JournalUI.JournalModel;
import com.wehearyou.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DedicatedChatsAdapter extends RecyclerView.Adapter<DedicatedChatsViewHolder> implements Filterable {

    Context context;
    ArrayList<DedicatedChatModel> dedicatedChatModelArrayList;
    ArrayList<DedicatedChatModel> dedicatedChatModelArrayListAll;

    public DedicatedChatsAdapter(Context context, ArrayList<DedicatedChatModel> dedicatedChatModelArrayList) {
        this.context = context;
        this.dedicatedChatModelArrayList = dedicatedChatModelArrayList;
        this.dedicatedChatModelArrayListAll = new ArrayList<>(dedicatedChatModelArrayList);
    }

    @NonNull
    @Override
    public DedicatedChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_dedicated_chats, parent, false);
        return new DedicatedChatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DedicatedChatsViewHolder holder, int position) {

        holder.name.setText(dedicatedChatModelArrayList.get(position).getListenerName());
        holder.topic.setText(dedicatedChatModelArrayList.get(position).getTopic());
        holder.photo.setImageResource(R.drawable.ic_matched);
        holder.arrow.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24_journal);

        holder.dedicatedChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DedicatedChattingActivity.class);
                intent.putExtra("listener", dedicatedChatModelArrayList.get(position).getListener());
                intent.putExtra("listenerName", dedicatedChatModelArrayList.get(position).getListenerName());
                intent.putExtra("chatId", dedicatedChatModelArrayList.get(position).getChatId());
                intent.putExtra("type", dedicatedChatModelArrayList.get(position).getType());
                intent.putExtra("topic", dedicatedChatModelArrayList.get(position).getTopic());
                intent.putExtra("id",dedicatedChatModelArrayList.get(position).getId());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dedicatedChatModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<DedicatedChatModel> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(dedicatedChatModelArrayListAll);
            }
            else {
                for(int i = 0; i<dedicatedChatModelArrayListAll.size();i++){
                    if(dedicatedChatModelArrayListAll.get(i).getListenerName().toLowerCase().contains(constraint.toString().toLowerCase())
                            || dedicatedChatModelArrayListAll.get(i).getTopic().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(dedicatedChatModelArrayListAll.get(i));
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dedicatedChatModelArrayList.clear();
            dedicatedChatModelArrayList.addAll((Collection<? extends DedicatedChatModel>) results.values);
            notifyDataSetChanged();
        }
    };
}
