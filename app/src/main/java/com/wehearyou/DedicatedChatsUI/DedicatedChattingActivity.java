package com.wehearyou.DedicatedChatsUI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.wehearyou.ChatUI.ChatActivity;
import com.wehearyou.ChatUI.SupportHelper.MessageAdapter;
import com.wehearyou.ChatUI.SupportHelper.MessageModel;
import com.wehearyou.FindListenerUI.ListenerChatEndedActivity;
import com.wehearyou.FindListenerUI.SeekerChatReviewActivity;
import com.wehearyou.R;
import com.wehearyou.SeekerDashboardActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DedicatedChattingActivity extends AppCompatActivity {

    ImageButton sendButtom;
    EditText txtMessage;

    RecyclerView recyclerView;
    ArrayList<MessageModel> messageModelArrayList;

    String listener, chatId, listenerName, type, topic, id;

    TextView header;

    ImageButton closeButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dedicated_chatting);

        Intent intent = getIntent();
        listener = intent.getStringExtra("listener");
        chatId = intent.getStringExtra("chatId");
        listenerName = intent.getStringExtra("listenerName");
        type = intent.getStringExtra("type");
        topic = intent.getStringExtra("topic");
        id = intent.getStringExtra("id");

        sendButtom = findViewById(R.id.sendButton);
        txtMessage = findViewById(R.id.message_edit);
        header = findViewById(R.id.textView9);

        closeButton = findViewById(R.id.closeButton);

        header.setText("Chat with " + listenerName);

        sendButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        messageModelArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getMessages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("DedicatedChats").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                
                if(!value.exists()){

                    if(type.equals("seeker")){

                    }
                    else {
                        finish();
                    }
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(DedicatedChattingActivity.this)
                        .setTitle("Close Chat")
                        .setMessage("Are you sure you want to close the chat?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("DedicatedChats").document(listener).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        db.collection("users").document(listener).collection("DedicatedChats").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(type.equals("seeker")){
                                                    Intent intent1 = new Intent(getApplicationContext(), DedicatedChatSeekerFeedbackActivity.class);
                                                    intent1.putExtra("listener",listener);
                                                    startActivity(intent1);
                                                    finish();
                                                }
                                                else {
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();

            }
        });
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

    private void sendMessage() {

        String messageSent = txtMessage.getText().toString().trim();
        String sentUserFinal = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String receivedUserFinal = listener;

        if(messageSent.equals("")){
            Toast.makeText(getApplicationContext(),"Enter a message",Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();

        HashMap<String , Object> message = new HashMap<>();
        message.put("message",messageSent);
        message.put("sentUser",sentUserFinal);
        message.put("receivedUser",receivedUserFinal);

        databaseReference.child("Chats").child(sentUserFinal).child(chatId).push().setValue(message);
        databaseReference1.child("Chats").child(receivedUserFinal).child(chatId).push().setValue(message);

        txtMessage.setText("");

        recyclerView.getLayoutManager().scrollToPosition(messageModelArrayList.size()-1);
    }

}