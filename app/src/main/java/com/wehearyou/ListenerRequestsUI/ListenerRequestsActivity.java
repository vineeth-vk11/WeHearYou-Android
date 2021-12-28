package com.wehearyou.ListenerRequestsUI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.wehearyou.R;

import java.util.ArrayList;

public class ListenerRequestsActivity extends AppCompatActivity {

    ArrayList<RequestsModel> requestsModelArrayList;
    RecyclerView requestsRecycler;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int age;
    ArrayList<String> topics = new ArrayList<>();
    int minAge, maxAge;
    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listener_requests);

        requestsModelArrayList = new ArrayList<>();

        requestsRecycler = findViewById(R.id.requestsRecycler);
        requestsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        requestsRecycler.setHasFixedSize(true);


        db.collection("Listeners").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                Log.i("topic","ok1");

                if(documentSnapshot.exists()){

                    Log.i("topic","ok2");

                    if(documentSnapshot.getString("age") != null){
                        age = Integer.parseInt(documentSnapshot.getString("age"));
                    }

                    if(documentSnapshot.get("Topics") != null){
                        topics = (ArrayList<String>) documentSnapshot.get("Topics");
                    }
                    else {
                        topics = null;
                    }

                    db.collection("ChatRequests").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                            Log.i("topic","ok3");

                            requestsModelArrayList.clear();

                            for(DocumentSnapshot documentSnapshot: value.getDocuments()){

                                Log.i("topic","ok4");

                                RequestsModel requestsModel = new RequestsModel();
                                requestsModel.setName(documentSnapshot.getString("userName"));
                                requestsModel.setChatId(documentSnapshot.getId());
                                requestsModel.setTopic(documentSnapshot.getString("topic"));
                                requestsModel.setUser(documentSnapshot.getString("user"));

//                                topic = documentSnapshot.getString("topic");
//                                minAge = (int) ((long)documentSnapshot.get("minAge"));
//                                maxAge = (int) ((long)documentSnapshot.get("maxAge"));

                                requestsModelArrayList.add(requestsModel);

//                                if(age>= minAge && age <= maxAge){
//                                    if(topics != null){
//                                        if(topics.contains(topic)){
//                                            requestsModelArrayList.add(requestsModel);
//                                        }
//                                    }
//                                    else {
//                                    }
//                                }

                            }

                            RequestsAdapter requestsAdapter = new RequestsAdapter(requestsModelArrayList, getApplicationContext());
                            requestsRecycler.setAdapter(requestsAdapter);
                        }
                    });
                }
            }
        });


    }
}