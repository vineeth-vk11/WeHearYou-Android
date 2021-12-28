package com.wehearyou;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.DedicatedChatsUI.DedicatedChatsActivity;
import com.wehearyou.FindListenerUI.TopicSelectionUI.TopicSelectionActivity;
import com.wehearyou.JournalUI.JournalActivity;
import com.wehearyou.ListenerRequestsUI.ListenerRequestsActivity;
import com.wehearyou.ListenerRequestsUI.RequestsAdapter;
import com.wehearyou.ListenerRequestsUI.RequestsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.prefs.PreferenceChangeEvent;

public class ListenerDashboardActivity extends AppCompatActivity {

    CardView requests, profile, journal, dedicatedChats, findListener;

    TextView talking;

    TextView number;

    int age;
    ArrayList<String> topics = new ArrayList<>();
    int minAge, maxAge;
    String topic;

    int totalRequests;

    MixpanelAPI mixpanel;

    ArrayList<String> topics1 = new ArrayList<>();

    public static final String MyPREFERENCES = "WHY_PREFS";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listener_dashboard);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        topics1.add("AcademicPressure");
        topics1.add("Bullying");
        topics1.add("COVID19");
        topics1.add("HealthIssues");
        topics1.add("IJustWantToTalk");
        topics1.add("LGBTQ&Identity");
        topics1.add("Loneliness");
        topics1.add("LowEnergy");
        topics1.add("MotivationAndConfidence");
        topics1.add("Overthinking");
        topics1.add("Parenting");
        topics1.add("RelationShips");
        topics1.add("Sleep");
        topics1.add("WorkAndProductivity");

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        findListener = findViewById(R.id.journal);
        profile = findViewById(R.id.cardView4);
        journal = findViewById(R.id.dedicated);
        dedicatedChats = findViewById(R.id.profile);
        requests = findViewById(R.id.findListener);
        number = findViewById(R.id.textView63);

        talking = findViewById(R.id.textView53);
        talking.setText("Talking Now: " + getRandomNumberString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Listeners").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if(documentSnapshot.exists()){

                    if(documentSnapshot.getString("age") != null){
                        age = Integer.parseInt(documentSnapshot.getString("age"));
                    }

                    if(documentSnapshot.get("Topics") != null){
                        topics = (ArrayList<String>) documentSnapshot.get("Topics");
                    }
                    else {
                        topics = null;
                    }

                    db.collection("ChatRequests").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                            totalRequests = 0;

                            for(DocumentSnapshot documentSnapshot: value.getDocuments()){

//                                topic = documentSnapshot.getString("topic");
//                                minAge = (int) ((long)documentSnapshot.get("minAge"));
//                                maxAge = (int) ((long)documentSnapshot.get("maxAge"));

                                totalRequests += 1;

//                                if(age>= minAge && age <= maxAge){
//                                    if(topics != null){
//                                        if(topics.contains(topic)){
//                                        }
//                                    }
//                                    else {
//                                        totalRequests += 1;
//                                    }
//                                }

                            }
                            number.setText(String.valueOf(totalRequests));
                        }
                    });
                }
            }
        });

        if(!sharedpreferences.getBoolean("isNotificationSubscribed", false)){

            Log.i("subscribing", "now");

            for(int i = 0; i<topics1.size();i++){
                int finalI = i;
                FirebaseMessaging.getInstance().subscribeToTopic(topics1.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(finalI == topics1.size() - 1){
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean("isNotificationSubscribed", true);
                            editor.apply();
                        }
                    }
                });
            }
        }

        findListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Listener Find Listener Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), TopicSelectionActivity.class);
                startActivity(intent);
            }
        });

        dedicatedChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Listener Dedicated Chat Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), DedicatedChatsActivity.class);
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

                mixpanel.track("Listener Journal Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), JournalActivity.class);
                startActivity(intent);
            }
        });

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("My Requests Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), ListenerRequestsActivity.class);
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

                mixpanel.track("Listener Profile Button Clicked", props);

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