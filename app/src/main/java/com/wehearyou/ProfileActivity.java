package com.wehearyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.CallsUI.CallHistoryUI.CallHistoryActivity;
import com.wehearyou.LoginUI.LoginMainActivity;
import com.wehearyou.Others.AgeChangeActivity;
import com.wehearyou.Others.FeedbackActivity;
import com.wehearyou.TherapiesUI.TherapyBookingHistoryUI.TherapyBookingsActivity;
import com.wehearyou.TherapiesUI.TherapyBookingUI.TherapiesActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    CardView callHistory, therapies, myTherapies, becomeListener, refer, logout, ageRange, privacyPolicy, terms, feedback;
    TextView name;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        callHistory = findViewById(R.id.materialCardView);
        therapies = findViewById(R.id.materialCardView2);
        myTherapies = findViewById(R.id.materialCardView3);
        becomeListener = findViewById(R.id.materialCardView4);
        refer = findViewById(R.id.materialCardView5);
        logout = findViewById(R.id.logout);
        name = findViewById(R.id.name);
        ageRange = findViewById(R.id.materialCardView8);
        feedback = findViewById(R.id.feedback);

        privacyPolicy = findViewById(R.id.privacy);
        terms = findViewById(R.id.tc);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("App Feedback Button Clicked", props);

                Intent intent = new Intent(ProfileActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Privacy Policy Button Clicked", props);

                String url = "https://docs.google.com/document/d/1am33nbtgCMV7qfr9p9sE2YBOnsnnb_-J_RLPsM0G9-k/edit?usp=sharing";//here goes your url
                Intent i = new
                        Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i); //this starts it in the browser

            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Terms Button Clicked", props);

                String url = "https://docs.google.com/document/d/1zDihPzqLGK9RntzesCkVwTdxVX5xOJzR5o5rmHD-mJA/edit?usp=sharing";//here goes your url
                Intent i = new
                        Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i); //this starts it in the browser

            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();

                if(documentSnapshot.getBoolean("firstCallDone") != null){

                    if(documentSnapshot.getBoolean("firstCallDone")){

                        myTherapies.setVisibility(View.VISIBLE);
                        therapies.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        ageRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Age Range Change Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), AgeChangeActivity.class);
                startActivity(intent);
            }
        });

        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Refer Button Clicked", props);

                String number = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                String finalNumber;

                finalNumber = number.substring(3,13);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Install the W.H.Y app now!. Download the app at https://play.google.com/store/apps/details?id=com.wehearyou");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                name.setText(task.getResult().getString("name"));
            }
        });

        therapies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Therapies Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), TherapiesActivity.class);
                startActivity(intent);
            }
        });

        myTherapies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("My Therapies Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), TherapyBookingsActivity.class);
                startActivity(intent);
            }
        });
        callHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Call History Button Clicked", props);

                Intent intent = new Intent(getApplicationContext(), CallHistoryActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Logout Button Clicked", props);

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}