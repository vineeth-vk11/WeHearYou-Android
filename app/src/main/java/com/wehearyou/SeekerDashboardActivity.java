package com.wehearyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.BecomeListenerUI.BecomeListenerInfoActivity;
import com.wehearyou.DedicatedChatsUI.DedicatedChatsActivity;
import com.wehearyou.FindListenerUI.MatchingActivity;
import com.wehearyou.FindListenerUI.TopicSelectionUI.TopicSelectionActivity;
import com.wehearyou.JournalUI.JournalActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class SeekerDashboardActivity extends AppCompatActivity {

    CardView findListener, profile, journal, dedicated, becomeListener;

    TextView talking;

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_dashboard);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        FirebaseMessaging.getInstance().subscribeToTopic("X").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        talking = findViewById(R.id.textView53);
        talking.setText("Talking Now: " + getRandomNumberString());

        findListener = findViewById(R.id.findListener);
        profile = findViewById(R.id.profile);
        journal = findViewById(R.id.journal);
        dedicated = findViewById(R.id.dedicated);
        becomeListener = findViewById(R.id.becomeListener);

        becomeListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Become Listener Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), BecomeListenerInfoActivity.class);
                startActivity(intent);
            }
        });

        journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Seeker Journal Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), JournalActivity.class);
                startActivity(intent);
            }
        });

        dedicated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Seeker Dedicated Chat Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), DedicatedChatsActivity.class);
                startActivity(intent);
            }
        });

        findListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Seeker Find Listener Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), TopicSelectionActivity.class);
                startActivity(intent);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Seeker Profile Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    public static String getRandomNumberString() {

        Random rnd = new Random();
        int number = rnd.nextInt(200);

        return String.format("%03d", number);
    }
}