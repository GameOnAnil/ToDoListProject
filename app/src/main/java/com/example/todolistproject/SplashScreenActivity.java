package com.example.todolistproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.HashMap;
import java.util.Map;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashScreenActivity";

    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();


        //setting up auth user

            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "onComplete: ");

                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: auth sucessful");
                        mCurrentUser = mAuth.getCurrentUser();

                        initNewCollection(mCurrentUser);
                        db.collection("User").document(mCurrentUser.getUid()).collection("Liset").document().delete();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                mCurrentUser = mAuth.getCurrentUser();
                                String userId = mCurrentUser.getUid();
                                Log.d(TAG, "run: uid is : " + userId);
                                intent.putExtra("userId", userId);
                                startActivity(intent);
                                finish();
                            }
                        }, 850);



                    } else {
                        Log.d(TAG, "onComplete:auth failure");
                    }
                }
            });






    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentUser = mAuth.getCurrentUser();
    }

    public void initNewCollection(FirebaseUser mCurrentUser) {
        final String userId = mCurrentUser.getUid();
        Log.d(TAG, "initNewCollection: userId for db" + userId);

        int extra = 100;

        final Map<String, Object> user = new HashMap<>();
        user.put("check",extra);

        final DocumentReference documentReference;
        documentReference = db.collection("User").document(userId);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "onFailure: Database exists");
                } else {
                    Log.d(TAG, "onSuccess: Database doesnt exist");
                    db.collection("User").document(userId).collection("List").add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "onSuccess: InitNewCollection successful");
                                    String docId = documentReference.getId();
                                    db.collection("User").document(userId).collection("List").document(docId).delete();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: InitNewCollection failed");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}