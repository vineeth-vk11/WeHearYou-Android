package com.wehearyou.FindListenerUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MatchingExitFeedbackActivity extends AppCompatActivity {

    Button done;

    RadioGroup reasons;
    RadioButton selectedReason;

    RadioButton group1, group2, group3, group4;

    String selectedOption;

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_exit_feedback);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        reasons = findViewById(R.id.radioGroup);

        done = findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedReasonId = reasons.getCheckedRadioButtonId();

                if(selectedReasonId == -1){
                    Toast.makeText(getApplicationContext(), "Please select an option to continue", Toast.LENGTH_SHORT).show();
                }
                else {

                    selectedReason = findViewById(selectedReasonId);

                    String reason = selectedReason.getText().toString();

                    if(reason.equals("I've been waiting for long")){
                        selectedOption = "I've been waiting for long";
                    }
                    else if(reason.equals("I’m Nervous")){
                        selectedOption = "I’m Nervous";
                    }
                    else if(reason.equals("Is my information confidential?")){
                        selectedOption = "Is my information confidential?";
                    }
                    else if(reason.equals("I’m not satisfied")){
                        selectedOption = "I’m not satisfied";
                    }
                    else {
                        selectedOption = "None";
                    }

                    String event = selectedOption + " Option selected in exit";

                    JSONObject props = new JSONObject();
                    try {
                        props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mixpanel.track(event, props);

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("reason",selectedOption);
                    data.put("user", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("MatchingExitFeedback").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            finish();
                        }
                    });

                }
            }
        });
    }
}