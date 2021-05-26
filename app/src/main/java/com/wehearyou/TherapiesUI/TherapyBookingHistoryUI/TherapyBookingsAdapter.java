package com.wehearyou.TherapiesUI.TherapyBookingHistoryUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wehearyou.R;
import com.wehearyou.TherapiesUI.TherapyBookingUI.TherapyModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TherapyBookingsAdapter extends RecyclerView.Adapter<TherapyBookingViewHolder> implements Filterable {

    Context context;
    ArrayList<TherapyBookingModel> therapyBookingModelArrayList;
    ArrayList<TherapyBookingModel> therapyBookingModelArrayListAll;

    public TherapyBookingsAdapter(Context context, ArrayList<TherapyBookingModel> therapyBookingModelArrayList) {
        this.context = context;
        this.therapyBookingModelArrayList = therapyBookingModelArrayList;
        this.therapyBookingModelArrayListAll = new ArrayList<>(therapyBookingModelArrayList);
    }

    @NonNull
    @Override
    public TherapyBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_therapy_booking, parent, false);
        return new TherapyBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TherapyBookingViewHolder holder, int position) {
        holder.therapyName.setText(therapyBookingModelArrayList.get(position).getTherapyName());
        holder.cost.setText("₹ " + therapyBookingModelArrayList.get(position).getCost());
        holder.date.setText(therapyBookingModelArrayList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return therapyBookingModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<TherapyBookingModel> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(therapyBookingModelArrayListAll);
            }
            else {
                for(int i = 0; i<therapyBookingModelArrayListAll.size();i++){
                    if(therapyBookingModelArrayListAll.get(i).getTherapyName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(therapyBookingModelArrayListAll.get(i));
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            therapyBookingModelArrayList.clear();
            therapyBookingModelArrayList.addAll((Collection<? extends TherapyBookingModel>) results.values);
            notifyDataSetChanged();
        }
    };
}
