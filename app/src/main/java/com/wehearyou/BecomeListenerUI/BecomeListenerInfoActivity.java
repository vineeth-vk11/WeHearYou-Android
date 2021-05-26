package com.wehearyou.BecomeListenerUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.airbnb.lottie.L;
import com.wehearyou.R;

public class BecomeListenerInfoActivity extends AppCompatActivity {

    Button next;

    EditText firstName, lastName, email, city, country, bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_listener_info);

        next = findViewById(R.id.next);

        firstName = findViewById(R.id.first_name_edit);
        lastName = findViewById(R.id.last_name_edit);
        email = findViewById(R.id.email_edit);
        city = findViewById(R.id.city_edit);
        country = findViewById(R.id.country_edit);
        bio = findViewById(R.id.bio_edit);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredFirstName = firstName.getText().toString();
                String enteredLastName = lastName.getText().toString();
                String enteredEmail = email.getText().toString();
                String enteredCity = city.getText().toString();
                String enteredCountry = country.getText().toString();
                String enteredBio = bio.getText().toString();

                if(TextUtils.isEmpty(enteredFirstName)){
                    Toast.makeText(getApplicationContext(), "Enter your first name",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredLastName)){
                    Toast.makeText(getApplicationContext(), "Enter your last name",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredEmail)){
                    Toast.makeText(getApplicationContext(), "Enter your email",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredCity)){
                    Toast.makeText(getApplicationContext(), "Enter your city",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredCountry)){
                    Toast.makeText(getApplicationContext(), "Enter your country",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(enteredBio)){
                    Toast.makeText(getApplicationContext(), "Enter your bio",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), BecomeListenerTopicsActivity.class);

                    intent.putExtra("firstName", enteredFirstName);
                    intent.putExtra("lastName", enteredLastName);
                    intent.putExtra("email", enteredEmail);
                    intent.putExtra("city", enteredCity);
                    intent.putExtra("country", enteredCountry);
                    intent.putExtra("bio", enteredBio);

                    startActivity(intent);
                }

            }
        });
    }
}