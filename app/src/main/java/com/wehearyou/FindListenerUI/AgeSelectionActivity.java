package com.wehearyou.FindListenerUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class AgeSelectionActivity extends AppCompatActivity {

    Button continueAge;
    RadioGroup ageGroups;
    RadioButton selectedAgeGroupButton;
    int minAge, maxAge;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String topic;

    String userName;

    MixpanelAPI mixpanel;

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                userName = task.getResult().getString("name");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_selection);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        continueAge = findViewById(R.id.continueAge);
        ageGroups = findViewById(R.id.radioGroup);

        Intent intent = getIntent();;
        topic = intent.getStringExtra("topic");

        continueAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedAgeGroup = ageGroups.getCheckedRadioButtonId();

                if(selectedAgeGroup == -1){
                    Toast.makeText(getApplicationContext(), "Please select age group",Toast.LENGTH_SHORT).show();
                }
                else {
                    selectedAgeGroupButton = findViewById(selectedAgeGroup);
                    if(selectedAgeGroupButton.getText().toString().equals("18 - 24 years")){
                        minAge = 18;
                        maxAge = 24;
                    }
                    else if(selectedAgeGroupButton.getText().toString().equals("25 - 34 years")){
                        minAge = 25;
                        maxAge = 34;
                    }
                    else if(selectedAgeGroupButton.getText().toString().equals("35 - 50 years")){
                        minAge = 35;
                        maxAge = 50;
                    }
                    else if(selectedAgeGroupButton.getText().toString().equals("51 years and above")){
                        minAge = 51;
                        maxAge = 100;
                    }
                    else {
                        minAge = 18;
                        maxAge = 24;
                    }

                    String event = String.valueOf(minAge) + " - " + String.valueOf(maxAge) + " Age group selected";

                    JSONObject props = new JSONObject();
                    try {
                        props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mixpanel.track(event, props);

                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("minAge",String.valueOf(minAge));
                    userData.put("maxAge", String.valueOf(maxAge));

                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Intent intent = new Intent(getApplicationContext(), CurrentFeelingActivity.class);
                            intent.putExtra("minAge", minAge);
                            intent.putExtra("maxAge", maxAge);
                            intent.putExtra("topic", topic);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}