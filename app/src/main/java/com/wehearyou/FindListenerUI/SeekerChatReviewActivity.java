package com.wehearyou.FindListenerUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.CallsUI.CallBookingUI.CallBookingBeforePaymentActivity;
import com.wehearyou.R;
import com.wehearyou.SeekerDashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SeekerChatReviewActivity extends AppCompatActivity {

    Button continueNext;

    String type;

    RadioGroup feelings;
    RadioButton feelingButton;
    String selectedFeeling;

    RatingBar ratingBar;
    EditText experience;

    String listener;

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_chat_review);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        continueNext = findViewById(R.id.continueNext);

        feelings = findViewById(R.id.radioGroup);
        ratingBar = findViewById(R.id.ratingBar);
        experience = findViewById(R.id.experience_edit);

        listener = getIntent().getStringExtra("listener");
        type = getIntent().getStringExtra("type");

        if(type.equals("payment")){
            continueNext.setText("Make Payment");
        }
        else {
            continueNext.setText("Finish");
        }

        continueNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = feelings.getCheckedRadioButtonId();

                if(id == -1){
                    Toast.makeText(getApplicationContext(), "Please select your feeling", Toast.LENGTH_SHORT).show();
                }
                else {

                    feelingButton = findViewById(id);

                    String feeling = feelingButton.getText().toString();

                    if(feeling.equals("Yes")){
                        selectedFeeling = "Yes";
                    }
                    else if(feeling.equals("Not Sure")){
                        selectedFeeling = "Not Sure";
                    }
                    else if(feeling.equals("No")){
                        selectedFeeling = "No";
                    }
                    else {
                        selectedFeeling = "None";
                    }

                    float rating = ratingBar.getRating();
                    String enteredExperience = experience.getText().toString();

                    String event = selectedFeeling + " Option Selected After Chat";
                    String event1 = String.valueOf(rating) + " Rating given by user";

                    JSONObject props = new JSONObject();
                    try {
                        props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mixpanel.track(event, props);
                    mixpanel.track(event1, props);

                    HashMap<String,Object> data = new HashMap<>();
                    data.put("feeling", selectedFeeling);
                    data.put("rating", rating);
                    data.put("experience", enteredExperience);
                    data.put("listener",listener);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("SeekerFeedback").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(type.equals("payment")){

                                JSONObject props = new JSONObject();
                                try {
                                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                mixpanel.track("Make Payment Button Clicked After Review", props);

                                Intent intent = new Intent(getApplicationContext(), CallBookingBeforePaymentActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), SeekerDashboardActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}