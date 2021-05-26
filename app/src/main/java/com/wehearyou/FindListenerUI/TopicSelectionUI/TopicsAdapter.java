package com.wehearyou.FindListenerUI.TopicSelectionUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.FindListenerUI.AgeSelectionActivity;
import com.wehearyou.FindListenerUI.CurrentFeelingActivity;
import com.wehearyou.FindListenerUI.MatchingActivity;
import com.wehearyou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsViewHolder> implements Filterable {

    ArrayList<TopicsModel> topicsModelArrayList;
    Context context;
    ArrayList<TopicsModel> topicsModelArrayListAll;

    int minAge, maxAge;
    boolean ageRangeExists;
    String name;

    Activity activity;

    public TopicsAdapter(ArrayList<TopicsModel> topicsModelArrayList, Context context, int minAge, int maxAge, boolean ageRangeExists, String name, Activity activity) {
        this.topicsModelArrayList = topicsModelArrayList;
        this.context = context;
        this.topicsModelArrayListAll = new ArrayList<>(topicsModelArrayList);
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.ageRangeExists = ageRangeExists;
        this.name = name;
        this.activity = activity;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MixpanelAPI mixpanel;

    @NonNull
    @Override
    public TopicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_topic, parent, false);
        return new TopicsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicsViewHolder holder, int position) {
        holder.topicName.setText(topicsModelArrayList.get(position).getTopicName());
        holder.topicTalkingNumber.setText(String.format("%s talking now", topicsModelArrayList.get(position).getTopicTalkingNumber()));

        if(position % 2 == 0){
            holder.topic.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }
        else {
            holder.topic.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
        }

        holder.arrow.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);

        holder.topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String event = topicsModelArrayList.get(position).getTopicName() + " Topic Button Clicked";

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel = MixpanelAPI.getInstance(context, "d1729e123d88f668650b9197c333f789\n");
                mixpanel.track(event, props);

                if(!ageRangeExists){
                    Intent intent = new Intent(context, AgeSelectionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("topic",topicsModelArrayList.get(position).getTopicName());
                    context.startActivity(intent);
                    activity.finish();
                }
                else {
                    Intent intent = new Intent(context, CurrentFeelingActivity.class);
                    intent.putExtra("minAge", minAge);
                    intent.putExtra("maxAge", maxAge);
                    intent.putExtra("topic", topicsModelArrayList.get(position).getTopicName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    activity.finish();

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return topicsModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<TopicsModel> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(topicsModelArrayListAll);
            }
            else {
                for(int i = 0; i<topicsModelArrayListAll.size();i++){
                    if(topicsModelArrayListAll.get(i).getTopicName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(topicsModelArrayListAll.get(i));
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            topicsModelArrayList.clear();
            topicsModelArrayList.addAll((Collection<? extends TopicsModel>) results.values);
            notifyDataSetChanged();
        }
    };
}
