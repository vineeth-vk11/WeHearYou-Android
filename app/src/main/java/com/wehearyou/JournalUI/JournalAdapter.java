package com.wehearyou.JournalUI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wehearyou.R;
import com.wehearyou.TherapiesUI.TherapyBookingHistoryUI.TherapyBookingModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalViewHolder> implements Filterable {

    Context context;
    ArrayList<JournalModel> journalModelArrayList;
    ArrayList<JournalModel> journalModelArrayListAll;

    public JournalAdapter(Context context, ArrayList<JournalModel> journalModelArrayList) {
        this.context = context;
        this.journalModelArrayList = journalModelArrayList;
        this.journalModelArrayListAll = new ArrayList<>(journalModelArrayList);
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_journal, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        holder.name.setText(journalModelArrayList.get(position).getListenerName());
        holder.topic.setText(journalModelArrayList.get(position).getTopic());
        holder.date.setText(journalModelArrayList.get(position).getDate());

        holder.photo.setImageResource(R.drawable.ic_matched);
        holder.arrow.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24_journal);

        holder.journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, JournalChatDetailsActivity.class);
                intent.putExtra("chatId", journalModelArrayList.get(position).getChatId());
                intent.putExtra("listenerName", journalModelArrayList.get(position).getListenerName());
                intent.putExtra("topic", journalModelArrayList.get(position).getTopic());
                intent.putExtra("date", journalModelArrayList.get(position).getDate());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return journalModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<JournalModel> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(journalModelArrayListAll);
            }
            else {
                for(int i = 0; i<journalModelArrayListAll.size();i++){
                    if(journalModelArrayListAll.get(i).getListenerName().toLowerCase().contains(constraint.toString().toLowerCase())
                    || journalModelArrayListAll.get(i).getDate().toLowerCase().contains(constraint.toString().toLowerCase())
                    || journalModelArrayListAll.get(i).getTopic().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(journalModelArrayListAll.get(i));
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            journalModelArrayList.clear();
            journalModelArrayList.addAll((Collection<? extends JournalModel>) results.values);
            notifyDataSetChanged();
        }
    };
}
