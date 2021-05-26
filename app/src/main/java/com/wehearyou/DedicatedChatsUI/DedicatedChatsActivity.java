package com.wehearyou.DedicatedChatsUI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.wehearyou.JournalUI.JournalAdapter;
import com.wehearyou.JournalUI.JournalModel;
import com.wehearyou.R;

import java.util.ArrayList;

public class DedicatedChatsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<DedicatedChatModel> dedicatedChatModelArrayList = new ArrayList<>();
    RecyclerView dedicatedChatsRecycler;
    DedicatedChatsAdapter dedicatedChatsAdapter;

    EditText search;

    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dedicated_chats);

        info = findViewById(R.id.textView52);

        dedicatedChatsRecycler = findViewById(R.id.dedicatedChatsRecycler);
        dedicatedChatsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        dedicatedChatsRecycler.setHasFixedSize(true);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("DedicatedChats").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                dedicatedChatModelArrayList.clear();

                for(DocumentSnapshot documentSnapshot: value.getDocuments()){

                    DedicatedChatModel dedicatedChatModel = new DedicatedChatModel();
                    dedicatedChatModel.setChatId(documentSnapshot.getString("chatId"));
                    dedicatedChatModel.setDate(documentSnapshot.getString("date"));
                    dedicatedChatModel.setListener(documentSnapshot.getString("listener"));
                    dedicatedChatModel.setListenerName(documentSnapshot.getString("listenerName"));
                    dedicatedChatModel.setTopic(documentSnapshot.getString("topic"));
                    dedicatedChatModel.setType(documentSnapshot.getString("type"));
                    dedicatedChatModel.setId(documentSnapshot.getId());

                    dedicatedChatModelArrayList.add(dedicatedChatModel);

                }

                if(dedicatedChatModelArrayList.size() == 0){
                    info.setVisibility(View.VISIBLE);
                }
                else{
                    info.setVisibility(View.INVISIBLE);
                }

                dedicatedChatsAdapter = new DedicatedChatsAdapter(getApplicationContext(), dedicatedChatModelArrayList);
                dedicatedChatsRecycler.setAdapter(dedicatedChatsAdapter);

            }
        });

        search = findViewById(R.id.searchBox);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dedicatedChatsAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}