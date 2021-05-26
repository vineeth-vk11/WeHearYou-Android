package com.wehearyou.CallsUI.CallBookingUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CallBookingAfterPaymentActivity extends AppCompatActivity {

    CardView timePicker, datePicker;
    TextView dateSelected, timeSelected;
    EditText mobileNumber;
    Button bookCall;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int tHour, tMinute;

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_booking_after_payment);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        timePicker = findViewById(R.id.materialCardView7);
        datePicker = findViewById(R.id.materialCardView6);
        dateSelected = findViewById(R.id.dateSelected);
        timeSelected = findViewById(R.id.timeSelected);
        mobileNumber = findViewById(R.id.phone_edit);
        bookCall = findViewById(R.id.bookCall);

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });

        bookCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dateSelectedUser = dateSelected.getText().toString();
                String timeSelectedUser = timeSelected.getText().toString();
                String mobileNumberEntered = mobileNumber.getText().toString().trim();
                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(dateSelectedUser.equals("Date")){

                }
                else if(timeSelectedUser.equals("Time")){

                }
                else if(TextUtils.isEmpty(mobileNumberEntered)){

                }
                else {

                    JSONObject props = new JSONObject();
                    try {
                        props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mixpanel.track("After Payment Booking Completed", props);

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("date", dateSelectedUser);
                    data.put("time", timeSelectedUser);
                    data.put("number",mobileNumberEntered);
                    data.put("user",user);

                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("callBookings").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            db.collection("callBookings").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                    HashMap<String, Object> data = new HashMap<>();
                                    data.put("firstCallDone", true);

                                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(getApplicationContext(), CallBookingSuccessfulActivity.class);
                                            intent.putExtra("date",dateSelectedUser);
                                            intent.putExtra("time",timeSelectedUser);
                                            intent.putExtra("number",mobileNumberEntered);
                                            intent.putExtra("amountPaid","₹ 250");
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void handleTimeButton(){
        Calendar calendar = Calendar.getInstance();
        int Hour = calendar.get(Calendar.HOUR);
        int Minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                tHour = hourOfDay;
                tMinute = minute;

                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,tHour,tMinute);

                String time = hourOfDay + ":" + minute;
                timeSelected.setText(DateFormat.format("hh:mm aa", calendar));
            }
        },Hour,Minute,false);
        timePickerDialog.show();
    }

    private void handleDateButton(){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                int correctedMonth = dayOfMonth + 1;

                Calendar calendar = Calendar.getInstance();
                calendar.set(year-1,correctedMonth-1,dayOfMonth-1);
                dateSelected.setText(DateFormat.format("dd/MM/yyyy", calendar));
            }
        }, year, month, date);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, 1);

        datePickerDialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());

        datePickerDialog.show();
    }
}