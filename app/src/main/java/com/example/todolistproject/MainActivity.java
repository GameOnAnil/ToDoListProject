package com.example.todolistproject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.ListItemListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private String toStore;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar_main = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar_main);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,AddList.class);
//                startActivity(intent);
                showAlertDialog();

            }
        });

        recyclerView = findViewById(R.id.recycler_view);

        setUpRecyclerView();

    }

    private void showAlertDialog() {
        final EditText editText = new EditText(this);
        editText.setHint("Enter list name");

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add list")
                .setView(editText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toStore = editText.getText().toString();
                        ListModel listModel = new ListModel(toStore);

                        if (!toStore.equals("")) {

                            db.collection("List").add(listModel)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(MainActivity.this, "Data added", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure!!!!!!!!: " + e.toString());
                                }
                            });
                        }else{
                            Toast.makeText(MainActivity.this, "Please enter list name", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    public void setUpRecyclerView() {
        Query query = db.collection("List");
        FirestoreRecyclerOptions<ListModel> options = new FirestoreRecyclerOptions.Builder<ListModel>()
                .setQuery(query, ListModel.class)
                .build();

        adapter = new RecyclerAdapter(options,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.d(TAG, "onStop: ");
    }


    @Override
    public void onItemClicked(DocumentSnapshot snapshot) {

        String id = snapshot.getId();
        Log.d(TAG, "onItemClicked: !!!!!!!!!!!!! and id:"+id);

        Intent intent = new Intent(getApplicationContext(),SubItemPage.class);
        intent.putExtra("id",id);
        startActivity(intent);

    }
}