package com.wehearyou.CallsUI.CallHistoryUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.CallsUI.CallBookingUI.CallBookingBeforePaymentActivity;
import com.wehearyou.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CallHistoryActivity extends AppCompatActivity {

    Button bookCall;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<CallHistoryModel> callHistoryModelArrayList = new ArrayList<>();

    RecyclerView callHistoryRecycler;

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_history);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        bookCall = findViewById(R.id.bookCall);

        bookCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Book Call Clicked in Call History Screen", props);

                Intent intent = new Intent(getApplicationContext(), CallBookingBeforePaymentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        callHistoryRecycler = findViewById(R.id.callHistoryRecycler);
        callHistoryRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        callHistoryRecycler.setHasFixedSize(true);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("callBookings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                callHistoryModelArrayList.clear();

                for(DocumentSnapshot documentSnapshot: task.getResult()){

                    CallHistoryModel callHistoryModel = new CallHistoryModel();
                    callHistoryModel.setTime(documentSnapshot.getString("time"));
                    callHistoryModel.setAmountPaid(documentSnapshot.getString("amountPaid"));
                    callHistoryModel.setDate(documentSnapshot.getString("date"));
                    callHistoryModel.setNumber(documentSnapshot.getString("number"));

                    callHistoryModelArrayList.add(callHistoryModel);
                }

                CallHistoryAdapter callHistoryAdapter = new CallHistoryAdapter(getApplicationContext(), callHistoryModelArrayList);
                callHistoryRecycler.setAdapter(callHistoryAdapter);
            }
        });
    }
}