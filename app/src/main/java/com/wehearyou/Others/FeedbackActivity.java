package com.wehearyou.Others;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wehearyou.R;

import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {

    Button submit;
    EditText feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        submit = findViewById(R.id.submit);
        feedback = findViewById(R.id.feedback_edit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredFeedback = feedback.getText().toString();

                if(!TextUtils.isEmpty(enteredFeedback)){

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("feedback", enteredFeedback);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("AppFeedbacks").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            finish();
                        }
                    });

                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter a feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}