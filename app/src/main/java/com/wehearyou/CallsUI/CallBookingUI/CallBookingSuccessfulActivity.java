package com.wehearyou.CallsUI.CallBookingUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wehearyou.CallsUI.CallHistoryUI.CallHistoryActivity;
import com.wehearyou.R;

public class CallBookingSuccessfulActivity extends AppCompatActivity {

    TextView detailsText;

    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_booking_successful);

        detailsText = findViewById(R.id.detailsText);
        finish = findViewById(R.id.finish);

        Intent intent = getIntent();

        detailsText.setText(String.format("Your booking is confirmed on %s, %s. You will receive a call on the mobile number %s.", intent.getStringExtra("time"), intent.getStringExtra("date"), intent.getStringExtra("number")));

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CallHistoryActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}