package com.example.todolistproject;

import android.service.voice.AlwaysOnHotwordDetector;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecyclerSubAdapter extends FirestoreRecyclerAdapter<ItemModel, RecyclerSubAdapter.Viewholder> {
    private static final String TAG = "RecyclerSubAdapter";
    

    public RecyclerSubAdapter(@NonNull FirestoreRecyclerOptions<ItemModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull ItemModel model) {
        holder.itemName.setText(model.getItem());
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_sub_list, parent, false);
        return new Viewholder(view);
    }

    public void removeItem(int position,View view) {
        DocumentSnapshot snapshot1 = getSnapshots().getSnapshot(position);
         final DocumentReference documentReference = snapshot1.getReference();
         final ItemModel itemModel = snapshot1.toObject(ItemModel.class);
        
        Log.d(TAG, "Remove method called for"+snapshot1.getId());
        
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Data deleted");
            }
        });
        Snackbar.make(view,"Are you sure",Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        documentReference.set(itemModel);
                    }
                }).show();

    }


    public class Viewholder extends RecyclerView.ViewHolder {
        TextView itemName = itemView.findViewById(R.id.list_sub_name); 

        public Viewholder(@NonNull View itemView) {
            super(itemView);

        }


    }



}
