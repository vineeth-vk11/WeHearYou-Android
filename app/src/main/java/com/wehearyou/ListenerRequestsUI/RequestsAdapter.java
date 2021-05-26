package com.wehearyou.ListenerRequestsUI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wehearyou.ChatUI.ChatActivity;
import com.wehearyou.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsViewHolder> {

    ArrayList<RequestsModel> requestsModelArrayList;
    Context context;

    public RequestsAdapter(ArrayList<RequestsModel> requestsModelArrayList, Context context) {
        this.requestsModelArrayList = requestsModelArrayList;
        this.context = context;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @NonNull
    @Override
    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_item_request, parent, false);
        return new RequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsViewHolder holder, int position) {
        holder.topic.setText(requestsModelArrayList.get(position).getTopic());
        holder.name.setText(requestsModelArrayList.get(position).getName());

        holder.accept.setImageResource(R.drawable.ic_baseline_check_circle_24_accept);
        holder.image.setImageResource(R.drawable.ic_matched);
        String chatId = requestsModelArrayList.get(position).getChatId();
        String listener = requestsModelArrayList.get(position).getUser();
        String listenerName = requestsModelArrayList.get(position).getName();
        String topic = requestsModelArrayList.get(position).getTopic();

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Listeners").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();

                        if(documentSnapshot.exists()){
                            String name = documentSnapshot.getString("name");
                            int sessions = (int)(long) documentSnapshot.get("sessions");

                            HashMap<String, Object> data1 = new HashMap<>();
                            data1.put("sessions",sessions+1);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("listener", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            data.put("listenerName", name);

                            db.collection("Listeners").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(data1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    db.collection("ChatRequests").document(chatId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            db.collection("Chats").document(chatId).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent intent = new Intent(context, ChatActivity.class);
                                                    intent.putExtra("listener",listener);
                                                    intent.putExtra("chatId",chatId);
                                                    intent.putExtra("listenerName",listenerName);
                                                    intent.putExtra("type","listener");
                                                    intent.putExtra("topic",topic);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    context.startActivity(intent);

                                                }
                                            });
                                        }
                                    });

                                }
                            });

                        }
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return requestsModelArrayList.size();
    }
}
