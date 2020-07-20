package com.example.todolistproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class AddSubItem extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "AddList";

    private TextInputEditText addSubDescription;
    private TextInputEditText txt_date;
    private TextInputEditText txt_time;

    String toStoreDesc;
    String toStoreDate;
    String toStoreTime;
    Boolean toStoreComplete = false;
    String documentId;
    String subDocumentId;
    String userId;
    Boolean isUpdatePage;
    Toolbar toolbar_add_list;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_item);

        addSubDescription = findViewById(R.id.txt_sub_description);
        txt_date = findViewById(R.id.txt_date);
        txt_time = findViewById(R.id.txt_time);

        toolbar_add_list = findViewById(R.id.toolbar_add_sub);
        setSupportActionBar(toolbar_add_list);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "date picker");
            }
        });

        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), "time picker");
            }
        });

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        documentId = intent.getStringExtra("id");
        subDocumentId = intent.getStringExtra("subDocumentId");
        isUpdatePage = intent.getBooleanExtra("toUpdate", false);


        if (isUpdatePage == true) {
            initUpdatePage();
            toolbar_add_list.setTitle("Update task");
        }


    }

    private void initUpdatePage() {
        db.collection("User").document(userId).collection("List").document(documentId).collection("Sub list").document(subDocumentId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if (snapshot.exists()) {
                    String itemFromDb = snapshot.getString("item");
                    String dateFromDb = snapshot.getString("date");
                    String timeFromDb = snapshot.getString("time");
                    Boolean completedFromDb = snapshot.getBoolean("completed");

                    txt_date.setText(dateFromDb);
                    txt_time.setText(timeFromDb);
                    addSubDescription.setText(itemFromDb);

                }
            }
        });
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

                toStoreDesc = addSubDescription.getText().toString();
                toStoreDate = txt_date.getText().toString();
                toStoreTime = txt_time.getText().toString();

                if (isUpdatePage == true) {
                    updateOld();


                } else {
                    saveNew();
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

    public void saveNew() {
        ItemModel itemModel = new ItemModel(toStoreDesc, toStoreDate, toStoreTime, toStoreComplete);
        if (!toStoreDesc.equals("")) {
            db.collection("User").document(userId).collection("List").document(documentId).collection("Sub list").add(itemModel)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AddSubItem.this, "Data added", Toast.LENGTH_SHORT).show();
                            finish();
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
    }

    public void updateOld() {

        ItemModel itemModel = new ItemModel(toStoreDesc, toStoreDate, toStoreTime, toStoreComplete);
        if (!toStoreDesc.equals("")) {
            db.collection("User").document(userId).collection("List").document(documentId).collection("Sub list").document(subDocumentId)
                    .update("item", itemModel.getItem(), "date", itemModel.getDate(), "time", itemModel.getTime())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddSubItem.this, "Data updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

        } else {
            Toast.makeText(AddSubItem.this, "Please enter list name", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        Intent intent = new Intent(getApplicationContext(), SubItemPage.class);
        intent.putExtra("id", documentId);

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);

        String currentDateSting = DateFormat.getDateInstance().format(calendar.getTime());
        txt_date.setText(currentDateSting);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String amPm;
        if (hour >= 12) {
            amPm = "PM";
        } else {
            amPm = "AM";
        }
        txt_time.setText(String.format("%02d:%02d ", hour, minute) + amPm);

    }
}