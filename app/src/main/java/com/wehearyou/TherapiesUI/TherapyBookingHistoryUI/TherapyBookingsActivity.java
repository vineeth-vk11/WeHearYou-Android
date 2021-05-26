package com.wehearyou.TherapiesUI.TherapyBookingHistoryUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.wehearyou.R;

import java.util.ArrayList;

public class TherapyBookingsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<TherapyBookingModel> therapyBookingModelArrayList;
    RecyclerView therapyBookingsRecycler;
    TherapyBookingsAdapter therapyBookingsAdapter;

    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy_bookings);

        therapyBookingModelArrayList = new ArrayList<>();

        therapyBookingsRecycler = findViewById(R.id.therapyBookingsRecycler);
        therapyBookingsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        therapyBookingsRecycler.setHasFixedSize(true);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("TherapyBookings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                therapyBookingModelArrayList.clear();

                for(DocumentSnapshot documentSnapshot: task.getResult()){

                    TherapyBookingModel therapyBookingModel = new TherapyBookingModel();

                    therapyBookingModel.setTherapyName(documentSnapshot.getString("therapyName"));
                    therapyBookingModel.setDate(documentSnapshot.getString("date"));
                    therapyBookingModel.setCost(documentSnapshot.getString("amountPaid"));

                    therapyBookingModelArrayList.add(therapyBookingModel);
                }

                therapyBookingsAdapter = new TherapyBookingsAdapter(getApplicationContext(), therapyBookingModelArrayList);
                therapyBookingsRecycler.setAdapter(therapyBookingsAdapter);
            }
        });

        search = findViewById(R.id.searchBox);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                therapyBookingsAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}