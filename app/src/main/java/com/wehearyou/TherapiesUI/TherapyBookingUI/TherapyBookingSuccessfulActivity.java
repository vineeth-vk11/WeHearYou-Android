package com.wehearyou.TherapiesUI.TherapyBookingUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wehearyou.R;
import com.wehearyou.TherapiesUI.TherapyBookingHistoryUI.TherapyBookingsActivity;

public class TherapyBookingSuccessfulActivity extends AppCompatActivity {

    TextView paidAmount;

    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy_booking_successful);

        paidAmount = findViewById(R.id.textView35);
        finish = findViewById(R.id.finish);

        paidAmount.setText("₹ " + getIntent().getStringExtra("cost"));

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TherapyBookingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}