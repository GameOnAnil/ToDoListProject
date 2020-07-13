package com.example.todolistproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SubItemPage extends AppCompatActivity {
    private static final String TAG = "SubItemPage";
    private RecyclerView recyclerView;
    private String documentId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerSubAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_item_page);

        recyclerView = findViewById(R.id.recycler_sub_view);

        Toolbar toolbar = findViewById(R.id.toolbar_sub_page);
        setSupportActionBar(toolbar);


        setUpRecyclerView();

    }

    public void setUpRecyclerView() {
        Intent intent = getIntent();
        documentId = intent.getStringExtra("id");

        Query query = db.collection("List").document(documentId).collection("sub");
        FirestoreRecyclerOptions<ItemModel> options = new FirestoreRecyclerOptions.Builder<ItemModel>()
                .setQuery(query, ItemModel.class)
                .build();

        adapter = new RecyclerSubAdapter(options);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}