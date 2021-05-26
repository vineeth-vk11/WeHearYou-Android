package com.wehearyou.BecomeListenerUI;

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
import com.google.firebase.messaging.FirebaseMessaging;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.wehearyou.BecomeListenerFailureActivity;
import com.wehearyou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BecomeListenerQuestion4Activity extends AppCompatActivity {

    Button next;

    RadioGroup radioGroup;

    ArrayList<String> answers = new ArrayList<>();

    int correctAnswers;

    String firstName, lastName, email, city, country, bio;
    ArrayList<String> topics, topics1;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RadioButton selectedButton;

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_listener_question4);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        Intent intent = getIntent();
        correctAnswers = intent.getIntExtra("correctAnswers",0);
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        email = intent.getStringExtra("email");
        city = intent.getStringExtra("city");
        country = intent.getStringExtra("country");
        bio = intent.getStringExtra("bio");
        topics = (ArrayList<String>)intent.getSerializableExtra("topics");
        topics1 = (ArrayList<String>)intent.getSerializableExtra("topics1");

        radioGroup = findViewById(R.id.radioGroup);

        answers.add("Do not advise or try to solve their problems.");
        answers.add("Do not share too much personal information");
        answers.add("Share personal experiences only when required.");

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "-mlRqtbZ4sg";
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedId = radioGroup.getCheckedRadioButtonId();

                if(checkedId == -1){
                    Toast.makeText(getApplicationContext(), "Please select an option to continue",Toast.LENGTH_SHORT).show();
                }
                else {

                    selectedButton = findViewById(checkedId);
                    String answer = selectedButton.getText().toString();

                    if(answers.contains(answer)){
                        correctAnswers += 1;
                    }

                    Log.i("Correct",String.valueOf(correctAnswers));

                    if(correctAnswers > 3){

                        JSONObject props = new JSONObject();
                        try {
                            props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mixpanel.track("Become Listener Successful", props);

                        for(int i = 0; i<topics1.size();i++){
                            FirebaseMessaging.getInstance().subscribeToTopic(topics1.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }

                        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                DocumentSnapshot documentSnapshot = task.getResult();

                                int age = (int)((long) documentSnapshot.get("age"));
                                String gender = documentSnapshot.getString("gender");

                                HashMap<String, Object> data = new HashMap<>();
                                data.put("age", String.valueOf(age));
                                data.put("gender",gender);
                                data.put("name",firstName);
                                data.put("lastName", lastName);
                                data.put("email", email);
                                data.put("city",city);
                                data.put("country",country);
                                data.put("bio", bio);
                                data.put("topics",topics);
                                data.put("sessions",0);

                                HashMap<String, Object> data1 = new HashMap<>();
                                data1.put("isListener",true);

                                db.collection("Listeners").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(data1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent1 = new Intent(getApplicationContext(), BecomeListenerSuccessfulActivity.class);
                                                startActivity(intent1);
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    else {

                        JSONObject props = new JSONObject();
                        try {
                            props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mixpanel.track("Become Listener Failure", props);

                        Intent intent1 = new Intent(getApplicationContext(), BecomeListenerFailureActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                }
            }
        });

    }
}