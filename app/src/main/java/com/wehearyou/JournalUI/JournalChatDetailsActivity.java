package com.wehearyou.JournalUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wehearyou.ChatUI.SupportHelper.MessageAdapter;
import com.wehearyou.ChatUI.SupportHelper.MessageModel;
import com.wehearyou.R;

import java.util.ArrayList;

public class JournalChatDetailsActivity extends AppCompatActivity {

    TextView name, topic, date;
    Button viewChat;

    String chatId, listenerName, dateValue, topicValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_chat_details);

        name = findViewById(R.id.name);
        topic = findViewById(R.id.topic);
        date = findViewById(R.id.date);
        viewChat = findViewById(R.id.viewChat);

        chatId = getIntent().getStringExtra("chatId");
        listenerName = getIntent().getStringExtra("listenerName");
        dateValue = getIntent().getStringExtra("date");
        topicValue = getIntent().getStringExtra("topic");

        name.setText(listenerName);
        topic.setText(topicValue);
        date.setText(dateValue);

        viewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JournalChatViewActivity.class);
                intent.putExtra("chatId",chatId);
                intent.putExtra("listenerName",listenerName);
                startActivity(intent);
            }
        });
    }
}