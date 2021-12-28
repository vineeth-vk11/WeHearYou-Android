package com.wehearyou.LoginUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.ListenerDashboardActivity;
import com.wehearyou.MainActivity;
import com.wehearyou.R;
import com.wehearyou.SeekerDashboardActivity;
import com.wehearyou.SplashActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginDetailsCollectionActivity extends AppCompatActivity {

    ArrayList<String> ages = new ArrayList<>();
    EditText name, age;
    Button getStarted;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RadioGroup genders;
    RadioButton selectedGenderButton;

    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_details_collection);

        age = findViewById(R.id.age_edit);
        name = findViewById(R.id.name_edit);
        getStarted = findViewById(R.id.getStarted);

        genders = findViewById(R.id.radioGroup);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredName = name.getText().toString().trim();
                String selectedAge = age.getText().toString().trim();

                int selectedGender = genders.getCheckedRadioButtonId();

                if(selectedGender == -1){
                    Toast.makeText(getApplicationContext(), "Please select your gender",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredName)){
                    Toast.makeText(getApplicationContext(), "Please enter your name",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(selectedAge)){
                    Toast.makeText(getApplicationContext(), "Please select your age", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(selectedAge) > 100 || Integer.parseInt(selectedAge) <18){
                    Toast.makeText(getApplicationContext(), "Please enter a age between 18 and 100", Toast.LENGTH_SHORT).show();
                }
                else {

                    selectedGenderButton = findViewById(selectedGender);
                    if(selectedGenderButton.getText().toString().equals("Male")){
                        gender = "Male";
                    }
                    else if(selectedGenderButton.getText().toString().equals("Female")){
                        gender = "Female";
                    }

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("name",enteredName);
                    data.put("age",Integer.parseInt(selectedAge));
                    data.put("isListener",false);
                    data.put("gender",gender);

                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            AppEventsLogger logger = AppEventsLogger.newLogger(getApplicationContext());
                            logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);

                            Intent intent = new Intent(getApplicationContext(), SeekerDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });

    }
}