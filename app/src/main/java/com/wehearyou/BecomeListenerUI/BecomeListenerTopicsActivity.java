package com.wehearyou.BecomeListenerUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wehearyou.R;

import java.util.ArrayList;

public class BecomeListenerTopicsActivity extends AppCompatActivity {

    Button next;

    CheckBox selectAll, academicPressure, bullying, covid, health, talk, lgbtq, loneliness, lowEnergy, motivation, overThinking, parenting, relationships, sleep, work;

    ArrayList<String> topics = new ArrayList<>();
    ArrayList<String> topics1 = new ArrayList<>();

    String firstName, lastName, email, city, country, bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_listener_topics);

        Intent intent = getIntent();
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        email = intent.getStringExtra("email");
        city = intent.getStringExtra("city");
        country = intent.getStringExtra("country");
        bio = intent.getStringExtra("bio");

        next = findViewById(R.id.next);

        selectAll = findViewById(R.id.checkBox0);
        academicPressure = findViewById(R.id.checkBox2);
        bullying = findViewById(R.id.checkBox10);
        covid = findViewById(R.id.checkBox7);
        health = findViewById(R.id.checkBox8);
        talk = findViewById(R.id.checkBox6);
        lgbtq = findViewById(R.id.checkBox5);
        loneliness = findViewById(R.id.checkBox11);
        lowEnergy = findViewById(R.id.checkBox14);
        motivation = findViewById(R.id.checkBox12);
        overThinking = findViewById(R.id.checkBox4);
        parenting = findViewById(R.id.checkBox9);
        relationships = findViewById(R.id.checkBox3);
        sleep = findViewById(R.id.checkBox13);
        work = findViewById(R.id.checkBox);

        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    academicPressure.setChecked(true);
                    bullying.setChecked(true);
                    covid.setChecked(true);
                    health.setChecked(true);
                    talk.setChecked(true);
                    lgbtq.setChecked(true);
                    loneliness.setChecked(true);
                    lowEnergy.setChecked(true);
                    motivation.setChecked(true);
                    overThinking.setChecked(true);
                    parenting.setChecked(true);
                    relationships.setChecked(true);
                    sleep.setChecked(true);
                    work.setChecked(true);
                }
                else {

                    academicPressure.setChecked(false);
                    bullying.setChecked(false);
                    covid.setChecked(false);
                    health.setChecked(false);
                    talk.setChecked(false);
                    lgbtq.setChecked(false);
                    loneliness.setChecked(false);
                    lowEnergy.setChecked(false);
                    motivation.setChecked(false);
                    overThinking.setChecked(false);
                    parenting.setChecked(false);
                    relationships.setChecked(false);
                    sleep.setChecked(false);
                    work.setChecked(false);

                }
            }
        });

        academicPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    topics.add("Academic Pressure");
                    topics1.add("AcademicPressure");
                }
                else {
                    topics.remove("Academic Pressure");
                    topics1.remove("AcademicPressure");
                }
            }
        });

        bullying.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("Bullying");
                    topics1.add("Bullying");
                }
                else {
                    topics.remove("Bullying");
                    topics1.remove("Bullying");
                }
            }
        });

        covid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("COVID 19");
                    topics1.add("COVID19");
                }
                else {
                    topics.remove("COVID 19");
                    topics1.remove("COVID19");
                }
            }
        });

        health.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("Health Issues");
                    topics1.add("HealthIssues");
                }
                else {
                    topics.remove("Health Issues");
                    topics1.remove("HealthIssues");
                }
            }
        });

        talk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("I just want to talk");
                    topics1.add("IJustWantToTalk");
                }
                else {
                    topics.remove("I just want to talk");
                    topics1.remove("IJustWantToTalk");
                }
            }
        });

        lgbtq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("LGBTQ & Identity");
                    topics1.add("LGBTQ&Identity");
                }
                else {
                    topics.remove("LGBTQ & Identity");
                    topics1.remove("LGBTQ&Identity");
                }
            }
        });

        loneliness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("Loneliness");
                    topics1.add("Loneliness");
                }
                else {
                    topics.remove("Loneliness");
                    topics1.remove("Loneliness");
                }
            }
        });

        lowEnergy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("Low Energy");
                    topics1.add("LowEnergy");
                }
                else {
                    topics.remove("Low Energy");
                    topics1.remove("LowEnergy");
                }
            }
        });

        motivation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("Motivation and Confidence");
                    topics1.add("MotivationAndConfidence");
                }
                else {
                    topics.remove("Motivation and Confidence");
                    topics1.remove("MotivationAndConfidence");
                }
            }
        });

        overThinking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("Overthinking");
                    topics1.add("Overthinking");
                }
                else {
                    topics.remove("Overthinking");
                    topics1.remove("Overthinking");
                }
            }
        });

        parenting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("Parenting");
                    topics1.add("Parenting");
                }
                else {
                    topics.remove("Parenting");
                    topics1.remove("Parenting");
                }
            }
        });

        relationships.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("Relationships");
                    topics1.add("RelationShips");
                }
                else {
                    topics.remove("Relationships");
                    topics1.remove("RelationShips");
                }
            }
        });

        sleep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("Sleep");
                    topics1.add("Sleep");
                }
                else {
                    topics.remove("Sleep");
                    topics1.remove("Sleep");
                }
            }
        });

        work.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    topics.add("Work and Productivity");
                    topics1.add("WorkAndProductivity");
                }
                else {
                    topics.remove("Work and Productivity");
                    topics1.remove("WorkAndProductivity");
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(topics.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Select atlease one topic", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), BecomeListenerQuestion1Activity.class);

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