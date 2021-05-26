package com.wehearyou.ChatUI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DirectAction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.ChatUI.SupportHelper.MessageAdapter;
import com.wehearyou.ChatUI.SupportHelper.MessageModel;
import com.wehearyou.FindListenerUI.ListenerChatEndedActivity;
import com.wehearyou.FindListenerUI.MatchingActivity;
import com.wehearyou.FindListenerUI.RequestAcceptedActivity;
import com.wehearyou.FindListenerUI.SeekerChatReviewActivity;
import com.wehearyou.R;
import com.wehearyou.SeekerDashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    ImageButton sendButtom;
    EditText txtMessage;

    RecyclerView recyclerView;
    ArrayList<MessageModel> messageModelArrayList;

    String listener, chatId, listenerName, type, topic;

    TextView header;

    ImageButton seekerButton, listenerButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String feeling, onMind;

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        Intent intent = getIntent();
        listener = intent.getStringExtra("listener");
        chatId = intent.getStringExtra("chatId");
        listenerName = intent.getStringExtra("listenerName");
        type = intent.getStringExtra("type");
        topic = intent.getStringExtra("topic");
        feeling = intent.getStringExtra("feeling");
        onMind = intent.getStringExtra("onMind");

        sendButtom = findViewById(R.id.sendButton);
        txtMessage = findViewById(R.id.message_edit);
        header = findViewById(R.id.textView9);

        seekerButton = findViewById(R.id.seekerButton);
        listenerButton = findViewById(R.id.listenerButton);

        if(type.equals("seeker")){
            seekerButton.setVisibility(View.VISIBLE);

            String messageSent = "I am feeling " + feeling + ". " + onMind;
            String sentUserFinal = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String receivedUserFinal = listener;

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();

            HashMap<String , Object> message = new HashMap<>();
            message.put("message",messageSent);
            message.put("sentUser",sentUserFinal);
            message.put("receivedUser",receivedUserFinal);

            databaseReference.child("Chats").child(sentUserFinal).child(chatId).push().setValue(message);
            databaseReference1.child("Chats").child(receivedUserFinal).child(chatId).push().setValue(message);

        }
        else {
            listenerButton.setVisibility(View.VISIBLE);
        }

        seekerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeekerExit();
            }
        });

        listenerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerOptions();
            }
        });

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

        db.collection("Chats").document(chatId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.exists()){

                    Boolean paymentActivated = value.getBoolean("paymentActivated");
                    Boolean isClosedBySeeker = value.getBoolean("isClosedBySeeker");
                    Boolean isClosedByListener = value.getBoolean("isClosedByListener");
                    Boolean isAddedToDedicatedChats = value.getBoolean("isAddedToDedicatedChats");

                    if(paymentActivated && type.equals("seeker")){

                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                        HashMap<String, Object> data1 = new HashMap<>();
                        data1.put("listenerName",listenerName);
                        data1.put("listener",listener);
                        data1.put("topic",topic);
                        data1.put("date",date);
                        data1.put("chatId",chatId);

                        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Journal").add(data1).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Intent intent = new Intent(getApplicationContext(), SeekerChatReviewActivity.class);
                                intent.putExtra("type", "payment");
                                intent.putExtra("listener",listener);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    if(isClosedByListener && type.equals("seeker")){

                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                        HashMap<String, Object> data1 = new HashMap<>();
                        data1.put("listenerName",listenerName);
                        data1.put("listener",listener);
                        data1.put("topic",topic);
                        data1.put("date",date);
                        data1.put("chatId",chatId);

                        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Journal").add(data1).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Intent intent = new Intent(getApplicationContext(), SeekerChatReviewActivity.class);
                                intent.putExtra("listener",listener);
                                intent.putExtra("type", "normal");
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    if(isAddedToDedicatedChats && type.equals("seeker")){
                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                        HashMap<String, Object> data1 = new HashMap<>();
                        data1.put("listenerName",listenerName);
                        data1.put("listener",listener);
                        data1.put("topic",topic);
                        data1.put("date",date);
                        data1.put("chatId",chatId);
                        data1.put("type",type);
                        data1.put("isClosedBySeeker",false);
                        data1.put("isClosedByListener",false);

                        HashMap<String, Object> data = new HashMap<>();
                        data.put("listenerName",listenerName);
                        data.put("listener",listener);
                        data.put("topic",topic);
                        data.put("date",date);
                        data.put("chatId",chatId);

                        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("DedicatedChats").document(listener).set(data1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Journal").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Intent intent = new Intent(getApplicationContext(), SeekerDashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        });
                    }

                    if(isClosedBySeeker && type.equals("listener")){

                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                        HashMap<String, Object> data1 = new HashMap<>();
                        data1.put("listenerName",listenerName);
                        data1.put("listener",listener);
                        data1.put("topic",topic);
                        data1.put("date",date);
                        data1.put("chatId",chatId);

                        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Journal").add(data1).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Intent intent = new Intent(getApplicationContext(), ListenerChatEndedActivity.class);
                                intent.putExtra("type","userEnded");
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
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

        JSONObject props = new JSONObject();
        try {
            props.put("Type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mixpanel.track("Message Sent", props);

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

    private void SeekerExit(){
        new AlertDialog.Builder(ChatActivity.this)
                .setTitle("Close Chat")
                .setMessage("Are you sure you want to close the chat?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        JSONObject props = new JSONObject();
                        try {
                            props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mixpanel.track("Seeker Closed Chat", props);

                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                        HashMap<String, Object> data1 = new HashMap<>();
                        data1.put("listenerName",listenerName);
                        data1.put("listener",listener);
                        data1.put("topic",topic);
                        data1.put("date",date);
                        data1.put("chatId",chatId);

                        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Journal").add(data1).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                HashMap<String, Object> data = new HashMap<>();
                                data.put("isClosedBySeeker", true);

                                db.collection("Chats").document(chatId).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(getApplicationContext(), SeekerDashboardActivity.class);
                                        startActivity(intent);
                                        finish();
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

    private void listenerOptions(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                ChatActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_listener_chat_options,
                        (LinearLayout)findViewById(R.id.listenerChatOptions)
                );

        bottomSheetView.findViewById(R.id.activatePayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                new AlertDialog.Builder(ChatActivity.this)
                        .setTitle("Activate Payment")
                        .setMessage("Are you sure you want to activate payment?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                JSONObject props = new JSONObject();
                                try {
                                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                mixpanel.track("Listener Activated Payment", props);

                                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                                HashMap<String, Object> data1 = new HashMap<>();
                                data1.put("listenerName",listenerName);
                                data1.put("listener",listener);
                                data1.put("topic",topic);
                                data1.put("date",date);
                                data1.put("chatId",chatId);

                                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Journal").add(data1).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        HashMap<String, Object> data = new HashMap<>();
                                        data.put("paymentActivated", true);

                                        db.collection("Chats").document(chatId).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent = new Intent(getApplicationContext(), ListenerChatEndedActivity.class);
                                                intent.putExtra("type","paymentActivated");
                                                startActivity(intent);
                                                finish();
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

        bottomSheetView.findViewById(R.id.addToDedicatedChats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                new AlertDialog.Builder(ChatActivity.this)
                        .setTitle("Add to dedicated chats")
                        .setMessage("Are you sure you want to add to dedicated chats?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                JSONObject props = new JSONObject();
                                try {
                                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                mixpanel.track("Listener Added Chat to Dedicated", props);

                                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                                HashMap<String, Object> data1 = new HashMap<>();
                                data1.put("listenerName",listenerName);
                                data1.put("listener",listener);
                                data1.put("topic",topic);
                                data1.put("date",date);
                                data1.put("chatId",chatId);
                                data1.put("type",type);
                                data1.put("isClosedBySeeker",false);
                                data1.put("isClosedByListener",false);

                                HashMap<String, Object> data = new HashMap<>();
                                data.put("listenerName",listenerName);
                                data.put("listener",listener);
                                data.put("topic",topic);
                                data.put("date",date);
                                data.put("chatId",chatId);

                                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("DedicatedChats").document(listener).set(data1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        HashMap<String, Object> data2 = new HashMap<>();
                                        data2.put("isAddedToDedicatedChats", true);

                                        db.collection("Chats").document(chatId).update(data2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Journal").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        Intent intent = new Intent(getApplicationContext(), ListenerChatEndedActivity.class);
                                                        intent.putExtra("type","addedToDedicatedChats");
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
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

        bottomSheetView.findViewById(R.id.closeChatListener).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                new AlertDialog.Builder(ChatActivity.this)
                        .setTitle("Close Chat")
                        .setMessage("Are you sure you want to close the chat?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                JSONObject props = new JSONObject();
                                try {
                                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                mixpanel.track("Listener Closed Chat", props);

                                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                                HashMap<String, Object> data1 = new HashMap<>();
                                data1.put("listenerName",listenerName);
                                data1.put("listener",listener);
                                data1.put("topic",topic);
                                data1.put("date",date);
                                data1.put("chatId",chatId);

                                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Journal").add(data1).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        HashMap<String, Object> data = new HashMap<>();
                                        data.put("isClosedByListener", true);

                                        db.collection("Chats").document(chatId).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent = new Intent(getApplicationContext(), ListenerChatEndedActivity.class);
                                                intent.putExtra("type","closedByListener");
                                                startActivity(intent);
                                                finish();
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

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}