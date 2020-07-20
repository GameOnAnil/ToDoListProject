package com.example.todolistproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecyclerAdapter extends FirestoreRecyclerAdapter<ListModel, RecyclerAdapter.Viewholder>  {
    private static final String TAG = "RecyclerAdapter";

    ListItemListener listItemListener;

    public RecyclerAdapter(@NonNull FirestoreRecyclerOptions<ListModel> options,ListItemListener listItemListener) {
        super(options);
        this.listItemListener = listItemListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull ListModel model) {
            holder.titleTv.setText(model.getTitle());

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list, parent, false);
        return new Viewholder(view);
    }

    public void deleteItem(int position) {
        listItemListener.handleDeleteItem(getSnapshots().getSnapshot(position));

    }
    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTv = itemView.findViewById(R.id.list_title);

        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                listItemListener.onItemClicked(getSnapshots().getSnapshot(getAdapterPosition()));
            }


        }
    }
    public interface ListItemListener{
        void onItemClicked(DocumentSnapshot snapshot);
        void handleDeleteItem(DocumentSnapshot snapshot);

    }


}


