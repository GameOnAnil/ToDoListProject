package com.example.todolistproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.Objects;

public class AddSubItem extends AppCompatActivity {
    private static final String TAG = "AddList";

    private EditText addSubDescription;

    String toStore;
    String documentId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_item);

        addSubDescription = findViewById(R.id.add_sub_description);

        Toolbar toolbar_add_list = findViewById(R.id.toolbar_add_sub);
        setSupportActionBar(toolbar_add_list);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.add_save:

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
                } else {
                    Toast.makeText(AddSubItem.this, "Please enter list name", Toast.LENGTH_SHORT).show();
                }

                return true;

            case android.R.id.home:

                Log.d(TAG, "onOptionsItemSelected: back arrow");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        Intent intent = new Intent(getApplicationContext(), SubItemPage.class);
        intent.putExtra("id", documentId);

    }
}