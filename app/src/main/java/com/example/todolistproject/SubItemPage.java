package com.example.todolistproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class SubItemPage extends AppCompatActivity implements RecyclerSubAdapter.SubItemListener {
    private static final String TAG = "SubItemPage";
    private RecyclerView recyclerView;
    private String documentId;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private String userId;

    private ActionMode mActionMode;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerSubAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_item_page);

        recyclerView = findViewById(R.id.recycler_sub_view);

        toolbar = findViewById(R.id.toolbar_sub_page);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = findViewById(R.id.coordinator_sub);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_sub);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddSubItem.class);
                intent.putExtra("id", documentId);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });


        setUpRecyclerView();
        Toast.makeText(this, "Hint: Swipe Right To Delete Task", Toast.LENGTH_LONG).show();

    }

    public void setUpRecyclerView() {
        Log.d(TAG, "setUpRecyclerView: setUpRecyclerView() called");
        Intent intent = getIntent();
        documentId = intent.getStringExtra("id");
        userId = intent.getStringExtra("userId");

        Query query = db.collection("User").document(userId).collection("List").document(documentId).collection("Sub list");
        final FirestoreRecyclerOptions<ItemModel> options = new FirestoreRecyclerOptions.Builder<ItemModel>()
                .setQuery(query, ItemModel.class)
                .build();

        adapter = new RecyclerSubAdapter(options, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: `");

        if (adapter != null) {
            adapter.startListening();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
        Log.d(TAG, "onStop: ");
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.removeItem(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(SubItemPage.this, R.color.red))
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void subItemClicked(DocumentSnapshot snapshot) {
        String subDocumentId = snapshot.getId();
        Log.d(TAG, "onItemClicked: !!!!!!!!!!!!! and id:" + subDocumentId);
        //Toast.makeText(this, "Item clicked" + subDocumentId, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), AddSubItem.class);
        intent.putExtra("userId",userId);
        intent.putExtra("id", documentId);
        intent.putExtra("subDocumentId", subDocumentId);
        intent.putExtra("toUpdate", true);
        startActivity(intent);

    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {
        final DocumentReference documentReference = snapshot.getReference();
        final ItemModel itemModel = snapshot.toObject(ItemModel.class);

        Log.d(TAG, "Remove method called for" + snapshot.getId());

        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Data deleted");
            }
        });
        Snackbar.make(coordinatorLayout, "Are you sure", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        documentReference.set(itemModel);
                    }
                }).show();
    }

    @Override
    public void updateCompleted(DocumentSnapshot snapshot, Boolean choice) {
        DocumentReference documentReference = snapshot.getReference();
        documentReference.update("completed", choice).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Data updated");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getApplicationContext());
        menuInflater.inflate(R.menu.sub_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sub_hint:
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.hint_dialog_layout_sub);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }


    }
}