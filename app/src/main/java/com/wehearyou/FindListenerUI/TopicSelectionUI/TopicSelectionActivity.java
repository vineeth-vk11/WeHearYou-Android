package com.wehearyou.FindListenerUI.TopicSelectionUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wehearyou.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TopicSelectionActivity extends AppCompatActivity {

    RecyclerView topicsRecycler;
    ArrayList<TopicsModel> topicsModelArrayList;
    TopicsAdapter topicsAdapter;
    FirebaseFirestore db;

    EditText search;

    int minAge, maxAge;
    boolean ageGroupExists = false;
    String userName;

    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_selection);

        db = FirebaseFirestore.getInstance();

        topicsModelArrayList = new ArrayList<>();

        topicsRecycler = findViewById(R.id.topicsRecycler);
        topicsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        topicsRecycler.setHasFixedSize(true);

        getUserData();

        search = findViewById(R.id.searchBox);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                topicsAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getUserData(){
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if(documentSnapshot.exists()){

                    if(documentSnapshot.getString("minAge") != null){
                        minAge = Integer.parseInt(documentSnapshot.getString("minAge"));
                        ageGroupExists = true;
                    }

                    if(documentSnapshot.getString("maxAge") != null){
                        maxAge = Integer.parseInt(documentSnapshot.getString("maxAge"));
                        ageGroupExists = true;
                    }

                    if(documentSnapshot.getString("minAge") != null && documentSnapshot.getString("maxAge") != null){
                        ageGroupExists = true;
                    }

                    userName = documentSnapshot.getString("name");

                }

                getTopics();
            }
        });
    }

    private void getTopics(){
        db.collection("Topics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                topicsModelArrayList.clear();

                for(DocumentSnapshot documentSnapshot: task.getResult()){

                    TopicsModel topicsModel = new TopicsModel();
                    topicsModel.setTopicName(documentSnapshot.getString("name"));
                    topicsModel.setTopicTalkingNumber(getRandomNumberString());
                    topicsModel.setNumber((int)(long)documentSnapshot.get("number"));

                    topicsModelArrayList.add(topicsModel);
                }

                Collections.sort(topicsModelArrayList,new TopicsModel());

                topicsAdapter = new TopicsAdapter(topicsModelArrayList, getApplicationContext(), minAge, maxAge, ageGroupExists, userName, activity);
                topicsRecycler.setAdapter(topicsAdapter);
            }
        });
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(30);

        // this will convert any number sequence into 6 character.
        return String.format("%02d", number);
    }
}