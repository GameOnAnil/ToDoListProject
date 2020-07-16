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
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class AddSubItem extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "AddList";

    private TextInputEditText addSubDescription;
    private TextInputEditText txt_date;
    private TextInputEditText txt_time;

    String toStoreDesc;
    String toStoreDate;
    String toStoreTime;
    String documentId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_item);

        addSubDescription = findViewById(R.id.txt_sub_description);
        txt_date = findViewById(R.id.txt_date);
        txt_time = findViewById(R.id.txt_time);

        Toolbar toolbar_add_list = findViewById(R.id.toolbar_add_sub);
        setSupportActionBar(toolbar_add_list);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(),"date picker");
            }
        });

        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(),"time picker");
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

                ItemModel itemModel = new ItemModel(toStoreDesc,toStoreDate,toStoreTime);

                if (!toStoreDesc.equals("")) {

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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,i);
        calendar.set(Calendar.MONTH,i1);
        calendar.set(Calendar.DAY_OF_MONTH,i2);

        String currentDateSting = DateFormat.getDateInstance().format(calendar.getTime());
        txt_date.setText(currentDateSting);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String amPm;
        if(hour>=12){
            amPm = "PM";
        }else {
            amPm = "AM";
        }
        txt_time.setText(String.format("%02d:%02d ",hour,minute)+amPm);

    }
}