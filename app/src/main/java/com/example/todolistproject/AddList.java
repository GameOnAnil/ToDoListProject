package com.example.todolistproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddList extends AppCompatActivity {
    private static final String TAG = "AddList";

    private EditText addTitle;
    private Button btnStore;
    private Button goBack;
    String toStore;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        addTitle = findViewById(R.id.add_title);
        btnStore = findViewById(R.id.btn_store);
        goBack = findViewById(R.id.go_to_main);

        Toolbar toolbar_add_list = findViewById(R.id.toolbar_add_list);
        setSupportActionBar(toolbar_add_list);

        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toStore = addTitle.getText().toString();
                ListModel listModel = new ListModel(toStore);

                if (!toStore.equals("")) {

                    db.collection("List").add(listModel)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(AddList.this, "Data added", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure!!!!!!!!: " + e.toString());
                        }
                    });
                }else{
                    Toast.makeText(AddList.this, "Please enter list name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}