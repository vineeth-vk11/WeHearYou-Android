package com.wehearyou.FindListenerUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.Notifications.FcmNotificationsSender;
import com.wehearyou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;

public class CurrentFeelingActivity extends AppCompatActivity {

    Button findListener;
    RadioGroup feelings;
    RadioButton selectedFeelingButton;
    EditText mind;
    String feeling;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String topic;
    String userName;

    int minAge, maxAge;

    String  token = "null";

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
        setContentView(R.layout.activity_current_feeling);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        Intent intent = getIntent();;
        minAge = intent.getIntExtra("minAge",18);
        maxAge = intent.getIntExtra("maxAge", 24);
        topic = intent.getStringExtra("topic");

        feelings = findViewById(R.id.radioGroup);
        findListener = findViewById(R.id.getStarted);
        mind = findViewById(R.id.mind_edit);

        Log.i("topic", topic);

        if(topic.trim().equals("Academic Pressure")){
            token = "/topics/AcademicPressure";
        }
        else if(topic.equals("Bullying")){
            token = "/topics/Bullying";
        }
        else if(topic.equals("COVID 19")){
            token = "/topics/COVID19";
        }
        else if(topic.equals("Health Issues")){
            token = "/topics/HealthIssues";
        }
        else if(topic.equals("I just want to talk")){
            token = "/topics/IJustWantToTalk";

        }
        else if(topic.equals("LGBTQ & Identity")){
            token = "/topics/LGBTQ&Identity";
        }
        else if(topic.equals("Loneliness")){
            token = "/topics/Loneliness";
        }
        else if(topic.equals("Low Energy")){
            token = "/topics/LowEnergy";
        }
        else if(topic.equals("Motivation and Confidence")){
            token = "/topics/MotivationAndConfidence";
        }
        else if(topic.equals("Overthinking")){
            token = "/topics/Overthinking";
        }
        else if(topic.equals("Parenting")){
            token = "/topics/Parenting";
        }
        else if(topic.equals("Relationships")){
            token = "/topics/RelationShips";
        }
        else if(topic.equals("Sleep")){
            token = "/topics/Sleep";
        }
        else if(topic.equals("Work and Productivity")){
            token = "/topics/WorkAndProductivity";
        }

        findListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedFeeling = feelings.getCheckedRadioButtonId();

                if (selectedFeeling == -1) {
                    Toast.makeText(getApplicationContext(), "Please select your feeling", Toast.LENGTH_SHORT).show();
                }
                else {
                    selectedFeelingButton = findViewById(selectedFeeling);

                    if (selectedFeelingButton.getText().toString().equals("  \uD83D\uDE00  Very Good")) {
                        feeling = "Very Good";
                    }
                    else if (selectedFeelingButton.getText().toString().equals("  \uD83D\uDE42  Good")) {
                        feeling = "Good";
                    }
                    else if (selectedFeelingButton.getText().toString().equals("  \uD83D\uDE10  Ok")) {
                        feeling = "Ok";
                    }
                    else if (selectedFeelingButton.getText().toString().equals("  \uD83D\uDE14  Bad")) {
                        feeling = "Bad";
                    }
                    else if (selectedFeelingButton.getText().toString().equals("  \uD83D\uDE2B  Worst")) {
                        feeling = "Worst";
                    }
                    else {
                        feeling = "Very Good";
                    }

                    String event = feeling + " Feeling Selected";

                    JSONObject props = new JSONObject();
                    try {
                        props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mixpanel.track(event, props);

                    String enteredOnMind = mind.getText().toString();

                    String chatId = UUID.randomUUID().toString();
                    String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("topic", topic);
                    data.put("minAge", minAge);
                    data.put("maxAge", maxAge);
                    data.put("user", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    data.put("userName", userName);

                    HashMap<String, Object> data1 = new HashMap<>();
                    data1.put("topic", topic);
                    data1.put("minAge", minAge);
                    data1.put("maxAge", maxAge);
                    data1.put("feeling", feeling);
                    data1.put("onMind",enteredOnMind);
                    data1.put("user", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    data1.put("userName", userName);
                    data1.put("listener", "waiting");
                    data1.put("listenerName", "waiting");
                    data1.put("date", date);
                    data1.put("isClosedBySeeker", false);
                    data1.put("isClosedByListener", false);
                    data1.put("paymentActivated", false);
                    data1.put("isAddedToDedicatedChats", false);
                    data1.put("listenerJoined", false);

                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("minAge", String.valueOf(minAge));
                    userData.put("maxAge", String.valueOf(maxAge));

                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            db.collection("ChatRequests").document(chatId).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    db.collection("Chats").document(chatId).set(data1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Log.i("token",token);

                                            FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(token,"New Request", "You have a new request", getApplicationContext(), CurrentFeelingActivity.this);

                                            fcmNotificationsSender.SendNotifications();

                                            Intent intent = new Intent(getApplicationContext(), MatchingActivity.class);
                                            intent.putExtra("chatId", chatId);
                                            intent.putExtra("feeling",feeling);
                                            intent.putExtra("onMind",enteredOnMind);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    });

                }

            }
        });
    }
}