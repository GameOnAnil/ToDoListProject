package com.example.todolistproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecyclerAdapter extends FirestoreRecyclerAdapter<ListModel, RecyclerAdapter.Viewholder> {

    public RecyclerAdapter(@NonNull FirestoreRecyclerOptions<ListModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull ListModel model) {
        holder.titleTv.setText(model.getTitle());


    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list,parent,false);
        return new Viewholder(view);
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView titleTv = itemView.findViewById(R.id.list_title);


        public Viewholder(@NonNull View itemView) {
            super(itemView);

        }
    }

}
