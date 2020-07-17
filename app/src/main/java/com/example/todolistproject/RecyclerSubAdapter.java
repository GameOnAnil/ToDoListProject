package com.example.todolistproject;

import android.graphics.Paint;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecyclerSubAdapter extends FirestoreRecyclerAdapter<ItemModel, RecyclerSubAdapter.Viewholder> {
    private static final String TAG = "RecyclerSubAdapter";
    SubItemListener subItemListener;

    public RecyclerSubAdapter(@NonNull FirestoreRecyclerOptions<ItemModel> options, SubItemListener subItemListener) {
        super(options);
        this.subItemListener = subItemListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull ItemModel model) {
        holder.itemName.setText(model.getItem());
        holder.itemDate.setText(model.getDate());
        holder.itemTime.setText(model.getTime());

        Boolean completeFromDb = model.getCompleted();
        if (completeFromDb == false) {
            //holder.subCheckbox.setSelected(false);
            holder.subCheckbox.setChecked(false);
            holder.itemName.setPaintFlags(holder.itemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.itemCompletedTxt.setText("");
        } else {
            //holder.subCheckbox.setSelected(true);
            holder.subCheckbox.setChecked(true);
            holder.itemName.setPaintFlags(holder.itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.itemCompletedTxt.setText("Completed");
        }
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

    public void updateItem(int position, Boolean choice) {
        subItemListener.updateCompleted(getSnapshots().getSnapshot(position), choice);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView itemName = itemView.findViewById(R.id.list_sub_name);
        TextView itemCompletedTxt = itemView.findViewById(R.id.list_sub_completed);
        TextView itemDate = itemView.findViewById(R.id.list_date);
        TextView itemTime = itemView.findViewById(R.id.list_time);
        CheckBox subCheckbox = itemView.findViewById(R.id.subCheckbox);


        public Viewholder(@NonNull final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        subItemListener.subItemClicked(getSnapshots().getSnapshot(getAdapterPosition()));
                    }
                }
            });

            subCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!itemName.getPaint().isStrikeThruText()) {
                        itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        itemCompletedTxt.setText("Completed");
                        updateItem(getAdapterPosition(), true);


                    } else {
                        itemName.setPaintFlags(itemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        itemCompletedTxt.setText("");
                        updateItem(getAdapterPosition(), false);
                    }

                }
            });

        }

    }

    interface SubItemListener {
        public void subItemClicked(DocumentSnapshot snapshot);

        public void handleDeleteItem(DocumentSnapshot snapshot);

        public void updateCompleted(DocumentSnapshot snapshot, Boolean choice);
    }


}
