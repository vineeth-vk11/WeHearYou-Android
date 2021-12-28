package com.wehearyou.Others;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.wehearyou.FindListenerUI.CurrentFeelingActivity;
import com.wehearyou.R;

import java.util.HashMap;

public class AgeChangeActivity extends AppCompatActivity {

    Button continueAge;
    RadioGroup ageGroups;
    RadioButton selectedAgeGroupButton;
    int minAge, maxAge;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RadioButton group1, group2, group3, group4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_change);

        continueAge = findViewById(R.id.continueAge);
        ageGroups = findViewById(R.id.radioGroup);

        group1 = findViewById(R.id.group1);
        group2 = findViewById(R.id.group2);
        group3 = findViewById(R.id.group3);
        group4 = findViewById(R.id.group4);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();

                if(documentSnapshot.getString("minAge") != null){


                    String minAge = documentSnapshot.getString("minAge");


                    if(minAge.equals("18")){
                        group1.setChecked(true);
                    }
                    else if(minAge.equals("25")){
                        group2.setChecked(true);
                    }
                    else if(minAge.equals("35")){
                        group3.setChecked(true);
                    }
                    else if(minAge.equals("51")){
                        group4.setChecked(true);
                    }
                }
            }
        });

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

                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("minAge",String.valueOf(minAge));
                    userData.put("maxAge", String.valueOf(maxAge));

                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
                }
            }
        });
    }
}