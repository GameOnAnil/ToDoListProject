package com.example.todolistproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddSubItem extends AppCompatActivity {
    private static final String TAG = "AddList";

    private EditText addSubDescription;
    private Button btnStore;
    private Button goBack;
    String toStore;
    String documentId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_item);

        addSubDescription = findViewById(R.id.add_sub_description);
        btnStore = findViewById(R.id.btn_store_sub);
        goBack = findViewById(R.id.go_to_main_sub);

        Toolbar toolbar_add_list = findViewById(R.id.toolbar_add_list);
        setSupportActionBar(toolbar_add_list);

        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toStore = addSubDescription.getText().toString();
                ItemModel itemModel = new ItemModel(toStore);

                if (!toStore.equals("")) {

                    Intent intent = getIntent();
                    documentId = intent.getStringExtra("id");

                    db.collection("List").document(documentId).collection("Sub list").add(itemModel)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(AddSubItem.this, "Data added", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure!!!!!!!!: " + e.toString());
                        }
                    });
                }else{
                    Toast.makeText(AddSubItem.this, "Please enter list name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(),SubItemPage.class);
                intent.putExtra("id",documentId);
                startActivity(intent);
            }
        });
    }
}