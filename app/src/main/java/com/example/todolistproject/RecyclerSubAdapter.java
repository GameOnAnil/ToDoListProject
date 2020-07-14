package com.example.todolistproject;

import android.graphics.Paint;
import android.service.voice.AlwaysOnHotwordDetector;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

import org.w3c.dom.Text;

public class RecyclerSubAdapter extends FirestoreRecyclerAdapter<ItemModel, RecyclerSubAdapter.Viewholder> {
    private static final String TAG = "RecyclerSubAdapter";
    SubItemListener subItemListener;

    public RecyclerSubAdapter(@NonNull FirestoreRecyclerOptions<ItemModel> options,SubItemListener subItemListener) {
        super(options);
        this.subItemListener = subItemListener;
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

    public void removeItem(int position) {
        subItemListener.handleDeleteItem(getSnapshots().getSnapshot(position));


    }


    public class Viewholder extends RecyclerView.ViewHolder {
        TextView itemName = itemView.findViewById(R.id.list_sub_name);
        CheckBox subCheckboc = itemView.findViewById(R.id.subCheckbox);

        public Viewholder(@NonNull final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        subItemListener.subItemClicked(getSnapshots().getSnapshot(getAdapterPosition()));
                    }
                }
            });

            subCheckboc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!itemName.getPaint().isStrikeThruText()){
                        itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }else{
                        itemName.setPaintFlags(itemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG) );
                    }

                }
            });

        }
    }

    interface SubItemListener{
        public void subItemClicked(DocumentSnapshot snapshot);
        public void handleDeleteItem(DocumentSnapshot snapshot);
    }



}
