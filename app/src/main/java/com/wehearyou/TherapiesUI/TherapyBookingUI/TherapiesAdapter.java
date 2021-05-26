package com.wehearyou.TherapiesUI.TherapyBookingUI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TherapiesAdapter extends RecyclerView.Adapter<TherapiesViewHolder> implements Filterable {

    ArrayList<TherapyModel> therapyModelArrayList;
    ArrayList<TherapyModel> therapyModelArrayListAll;
    Context context;

    public TherapiesAdapter(ArrayList<TherapyModel> therapyModelArrayList, Context context) {
        this.therapyModelArrayList = therapyModelArrayList;
        this.context = context;
        this.therapyModelArrayListAll = new ArrayList<>(therapyModelArrayList);
    }

    MixpanelAPI mixpanel;

    @NonNull
    @Override
    public TherapiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_therapy, parent, false);
        return new TherapiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TherapiesViewHolder holder, int position) {
        holder.therapyName.setText(therapyModelArrayList.get(position).getTherapyName());
        holder.therapyCost.setText("₹ " + therapyModelArrayList.get(position).getTherapyCost());

        holder.arrow.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);

        holder.therapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String event = therapyModelArrayList.get(position).getTherapyName() + " Therapy Clicked";

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel = MixpanelAPI.getInstance(context, "d1729e123d88f668650b9197c333f789\n");
                mixpanel.track(event, props);

                Intent intent = new Intent(context, TherapyDetailsActivity.class);
                intent.putExtra("name",therapyModelArrayList.get(position).getTherapyName());
                intent.putExtra("cost",therapyModelArrayList.get(position).getTherapyCost());
                intent.putExtra("description",therapyModelArrayList.get(position).getDescription());
                intent.putExtra("images",therapyModelArrayList.get(position).getTherapyImages());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return therapyModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<TherapyModel> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(therapyModelArrayListAll);
            }
            else {
                for(int i = 0; i<therapyModelArrayListAll.size();i++){
                    if(therapyModelArrayListAll.get(i).getTherapyName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(therapyModelArrayListAll.get(i));
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            therapyModelArrayList.clear();
            therapyModelArrayList.addAll((Collection<? extends TherapyModel>) results.values);
            notifyDataSetChanged();
        }
    };
}
