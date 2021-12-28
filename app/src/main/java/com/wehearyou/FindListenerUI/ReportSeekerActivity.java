package com.wehearyou.FindListenerUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wehearyou.R;

import java.util.HashMap;

public class ReportSeekerActivity extends AppCompatActivity {

    String seekerId;

    TextView feedback;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_seeker);

        Intent intent = getIntent();
        seekerId = intent.getStringExtra("listenerId");

        feedback = findViewById(R.id.feedback_edit);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String report = feedback.getText().toString().trim();

                if(TextUtils.isEmpty(report)){
                    Toast.makeText(getApplicationContext(), "Enter your issue and submit", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("report", report);
                    data.put("user", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    data.put("seeker", seekerId);

                    db.collection("Seeker Reports").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<DocumentReference> task) {
                            finish();
                        }
                    });
                }
            }
        });
    }
}