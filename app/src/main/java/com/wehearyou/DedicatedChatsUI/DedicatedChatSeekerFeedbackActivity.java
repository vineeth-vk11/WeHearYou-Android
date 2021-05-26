package com.wehearyou.DedicatedChatsUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wehearyou.R;

import java.util.HashMap;

public class DedicatedChatSeekerFeedbackActivity extends AppCompatActivity {

    Button submit;

    EditText feedback;

    String listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dedicated_chat_seeker_feedback);

        listener = getIntent().getStringExtra("listener");

        submit = findViewById(R.id.submit);
        feedback = findViewById(R.id.feedback_edit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredFeedback = feedback.getText().toString();

                if(!TextUtils.isEmpty(enteredFeedback)){

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("feedback", enteredFeedback);
                    data.put("listener", listener);
                    data.put("user", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("DedicatedChatExitFeedbacks").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            finish();
                        }
                    });

                }
                else {
                    finish();
                }
            }
        });
    }
}