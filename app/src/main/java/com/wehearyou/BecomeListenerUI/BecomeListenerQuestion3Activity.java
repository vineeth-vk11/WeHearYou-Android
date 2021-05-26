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

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.wehearyou.R;

import java.util.ArrayList;

public class BecomeListenerQuestion3Activity extends AppCompatActivity {

    Button next;

    RadioGroup radioGroup;

    ArrayList<String> answers = new ArrayList<>();

    int correctAnswers;

    String firstName, lastName, email, city, country, bio;
    ArrayList<String> topics, topics1;

    RadioButton selectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_listener_question3);

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

        answers.add("By providing them time and space to share openly talk about their issues.");
        answers.add("Listen to them with acceptance and validation.");
        answers.add("Be mindful of their emotions.");

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "G0DtWpmK8rU";
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

                    Intent intent = new Intent(getApplicationContext(), BecomeListenerQuestion4Activity.class);
                    intent.putExtra("correctAnswers", correctAnswers);
                    intent.putExtra("firstName", firstName);
                    intent.putExtra("lastName", lastName);
                    intent.putExtra("email", email);
                    intent.putExtra("city", city);
                    intent.putExtra("country", country);
                    intent.putExtra("bio", bio);
                    intent.putExtra("topics", topics);
                    intent.putExtra("topics1", topics1);

                    startActivity(intent);
                }
            }
        });

    }
}