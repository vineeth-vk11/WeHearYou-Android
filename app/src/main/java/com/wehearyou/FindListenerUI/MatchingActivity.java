package com.wehearyou.FindListenerUI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.Date;

public class MatchingActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String chatId;
    ImageButton closeChat;

    String feeling, onMind;

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        Intent intent = getIntent();
        chatId = intent.getStringExtra("chatId");
        feeling = intent.getStringExtra("feeling");
        onMind = intent.getStringExtra("onMind");

        db.collection("Chats").document(chatId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.i("called","now");
                if(value.exists()){
                    String listener = value.getString("listener");
                    String topic = value.getString("topic");
                    Boolean listenerJoined = value.getBoolean("listenerJoined");

                    assert listener != null;
                    if(!listener.equals("waiting") && !listenerJoined){
                        Intent intent = new Intent(getApplicationContext(), RequestAcceptedActivity.class);
                        intent.putExtra("listener",listener);
                        intent.putExtra("chatId",chatId);
                        intent.putExtra("topic", topic);
                        intent.putExtra("feeling", feeling);
                        intent.putExtra("onMind", onMind);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        closeChat = findViewById(R.id.closeChat);
        closeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExit();
            }
        });
    }

    private void deleteChat(String from){
        db.collection("ChatRequests").document(chatId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(from.equals("exit")){
                    Intent intent = new Intent(getApplicationContext(), MatchingExitFeedbackActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    finish();
                }
            }
        });
    }

    private void showExit(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MatchingActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_close_chat,
                        (LinearLayout)findViewById(R.id.closeChatDialog)
                );
        bottomSheetView.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Closed in Matching Screen", props);

                deleteChat("exit");
            }
        });

        bottomSheetView.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    @Override
    public void onBackPressed() {
        showExit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteChat("destroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        deleteChat("stop");
    }

}