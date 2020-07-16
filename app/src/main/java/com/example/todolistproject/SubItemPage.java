package com.example.todolistproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class SubItemPage extends AppCompatActivity implements RecyclerSubAdapter.SubItemListener {
    private static final String TAG = "SubItemPage";
    private RecyclerView recyclerView;
    private String documentId;
    private Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;

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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = findViewById(R.id.coordinator_sub);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_sub);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddSubItem.class);
                intent.putExtra("id", documentId);
                startActivity(intent);
            }
        });


        setUpRecyclerView();

    }

    public void setUpRecyclerView() {
        Log.d(TAG, "setUpRecyclerView: setUpRecyclerView() called");
        Intent intent = getIntent();
        documentId = intent.getStringExtra("id");

        Query query = db.collection("List").document(documentId).collection("Sub list");
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
        String id = snapshot.getId();
        Log.d(TAG, "onItemClicked: !!!!!!!!!!!!! and id:" + id);
        Toast.makeText(this, "Item clicked" + id, Toast.LENGTH_SHORT).show();
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

}