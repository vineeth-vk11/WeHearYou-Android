package com.wehearyou.FindListenerUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.ChatUI.ChatActivity;
import com.wehearyou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RequestAcceptedActivity extends AppCompatActivity {

    String listenerId, listenerName, topic;
    String chatId;

    TextView header, header1, bio, sessionsCompleted;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button joinChat;

    String feeling, onMind;

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_accepted);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        header = findViewById(R.id.header);
        header1 = findViewById(R.id.header1);
        bio = findViewById(R.id.bio);
        joinChat = findViewById(R.id.joinChat);
        sessionsCompleted = findViewById(R.id.textView54);

        Intent intent = getIntent();
        listenerId = intent.getStringExtra("listener");
        chatId = intent.getStringExtra("chatId");
        topic = intent.getStringExtra("topic");
        feeling = intent.getStringExtra("feeling");
        onMind = intent.getStringExtra("onMind");

        db.collection("Listeners").document(listenerId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                header.setText("Request accepted by " + task.getResult().getString("name"));
                header1.setText("About " + task.getResult().getString("name"));
                bio.setText(task.getResult().getString("bio"));
                sessionsCompleted.setText("Session Completed: " + String.valueOf(30 + (int)(long)task.getResult().get("sessions")));

                listenerName = task.getResult().getString("name");
            }
        });


        joinChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Join Chat Button Clicked", props);

                HashMap<String, Object> data = new HashMap<>();
                data.put("listenerJoined",true);

                db.collection("Chats").document(chatId).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        intent.putExtra("listener",listenerId);
                        intent.putExtra("chatId",chatId);
                        intent.putExtra("listenerName", listenerName);
                        intent.putExtra("type", "seeker");
                        intent.putExtra("topic",topic);
                        intent.putExtra("feeling", feeling);
                        intent.putExtra("onMind", onMind);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });


    }
}