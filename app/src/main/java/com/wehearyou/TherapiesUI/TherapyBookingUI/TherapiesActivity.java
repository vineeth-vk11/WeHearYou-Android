package com.wehearyou.TherapiesUI.TherapyBookingUI;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wehearyou.R;

import java.util.ArrayList;

public class TherapiesActivity extends AppCompatActivity {

    FirebaseFirestore db;
    ArrayList<TherapyModel> therapyModelArrayList;
    RecyclerView therapiesRecycler;
    TherapiesAdapter therapiesAdapter;

    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapies);

        db = FirebaseFirestore.getInstance();

        therapyModelArrayList = new ArrayList<>();

        therapiesRecycler = findViewById(R.id.therapiesRecycler);
        therapiesRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        therapiesRecycler.setHasFixedSize(true);

        db.collection("Therapies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                therapyModelArrayList.clear();

                for(DocumentSnapshot documentSnapshot: task.getResult()){

                    TherapyModel therapyModel = new TherapyModel();
                    therapyModel.setTherapyName(documentSnapshot.getString("name"));
                    therapyModel.setTherapyCost(documentSnapshot.getString("cost"));
                    therapyModel.setDescription(documentSnapshot.getString("description"));
                    therapyModel.setTherapyImages((ArrayList<String>) documentSnapshot.get("images"));

                    therapyModelArrayList.add(therapyModel);
                }

                therapiesAdapter = new TherapiesAdapter(therapyModelArrayList, getApplicationContext());
                therapiesRecycler.setAdapter(therapiesAdapter);

            }
        });

        search = findViewById(R.id.searchBox);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                therapiesAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}