package com.wehearyou.JournalUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wehearyou.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class JournalActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<JournalModel> journalModelArrayList = new ArrayList<>();
    RecyclerView journalsRecycler;
    JournalAdapter journalAdapter;

    EditText search;

    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        info = findViewById(R.id.textView52);

        journalsRecycler = findViewById(R.id.journalRecycler);
        journalsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        journalsRecycler.setHasFixedSize(true);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Journal").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                journalModelArrayList.clear();

                for(DocumentSnapshot documentSnapshot: task.getResult()){

                    JournalModel journalModel = new JournalModel();
                    journalModel.setListenerName(documentSnapshot.getString("listenerName"));
                    journalModel.setTopic(documentSnapshot.getString("topic"));
                    journalModel.setDate(documentSnapshot.getString("date"));
                    journalModel.setChatId(documentSnapshot.getString("chatId"));

                    try {
                        journalModel.setDateD(new SimpleDateFormat("dd/MM/yyyy").parse(documentSnapshot.getString("date")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    journalModelArrayList.add(journalModel);
                }

                if(journalModelArrayList.size() == 0){
                    info.setVisibility(View.VISIBLE);
                }
                else{
                    info.setVisibility(View.INVISIBLE);
                }

                Collections.sort(journalModelArrayList);
                Collections.reverse(journalModelArrayList);

                journalAdapter = new JournalAdapter(getApplicationContext(), journalModelArrayList);
                journalsRecycler.setAdapter(journalAdapter);
            }
        });

        search = findViewById(R.id.searchBox);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                journalAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}