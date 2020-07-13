package com.example.todolistproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecyclerSubAdapter extends FirestoreRecyclerAdapter<ItemModel, RecyclerSubAdapter.Viewholder> {

    public RecyclerSubAdapter(@NonNull FirestoreRecyclerOptions<ItemModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull ItemModel model) {
        holder.itemName.setText(model.getItemName());


    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_sub_list,parent,false);
        return new Viewholder(view);
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView itemName = itemView.findViewById(R.id.list_sub_name);


        public Viewholder(@NonNull View itemView) {
            super(itemView);

        }
    }

}
