package com.wehearyou.FindListenerUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wehearyou.ListenerDashboardActivity;
import com.wehearyou.R;

public class ListenerChatEndedActivity extends AppCompatActivity {

    Button finish;
    TextView message;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listener_chat_ended);

        finish = findViewById(R.id.finish);
        message = findViewById(R.id.message);

        type = getIntent().getStringExtra("type");

        if (type.equals("userEnded")){
            message.setText("Chat ended by user");
        }
        else if(type.equals("paymentActivated")){
            message.setText("Payment activated for seeker and chat ended successfully.");
        }
        else if(type.equals("addedToDedicatedChats")){
            message.setText("Seeker added to dedicated chats and chat ended successfully");
        }
        else if(type.equals("closedByListener")){
            message.setText("Chat ended successfully");
        }

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListenerDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}