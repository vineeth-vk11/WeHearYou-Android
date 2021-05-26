package com.wehearyou.JournalUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

public class JournalChatViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MessageModel> messageModelArrayList = new ArrayList<>();

    TextView header;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String chatId, listenerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_chat_view);

        chatId = getIntent().getStringExtra("chatId");
        listenerName = getIntent().getStringExtra("listenerName");

        header = findViewById(R.id.textView9);

        header.setText("Chat with " + listenerName);

        recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        getMessages();

    }

    private void getMessages() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(chatId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModelArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MessageModel messageModel = new MessageModel();
                    messageModel.setMessage(dataSnapshot.child("message").getValue().toString());
                    messageModel.setSentUser(dataSnapshot.child("sentUser").getValue().toString());
                    messageModel.setReceivedUser(dataSnapshot.child("receivedUser").getValue().toString());

                    messageModelArrayList.add(messageModel);
                }

                MessageAdapter messageAdapter = new MessageAdapter(getApplicationContext(),messageModelArrayList);
                recyclerView.setAdapter(messageAdapter);
                recyclerView.getLayoutManager().scrollToPosition(messageModelArrayList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}