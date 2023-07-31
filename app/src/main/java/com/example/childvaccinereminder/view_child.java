package com.example.childvaccinereminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.childvaccinereminder.DataClass;
import com.example.childvaccinereminder.MyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class view_child extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DataClass> dataClassArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    FloatingActionButton fbb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_child);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        dataClassArrayList = new ArrayList<>();
        myAdapter = new MyAdapter(view_child.this, dataClassArrayList);

        recyclerView.setAdapter(myAdapter);
        fbb = findViewById(R.id.fbb);

        fbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view_child.this, MainActivity.class);
                startActivity(intent);
            }
        });

        EventChangeListener();
    }

    private void EventChangeListener() {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db.collection("users").document(userId).collection("child")
                .orderBy("cName", Query.Direction.DESCENDING) // Order by descending to show newly added child first
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                DataClass dataClass = dc.getDocument().toObject(DataClass.class);
                                dataClassArrayList.add(0, dataClass); // Add the new child's data at the top
                            }
                        }

                        // Notify the adapter that the dataset has changed
                        myAdapter.notifyDataSetChanged();
                    }
                });
    }
}
