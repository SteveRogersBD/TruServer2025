package com.example.greenpulse.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.R;
import com.example.greenpulse.models.Field;

import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {

    Context context;
    private List<Field> fieldList;

    // Constructor
    public FieldAdapter(Context context,List<Field> fieldList) {
        this.context = context;
        this.fieldList = fieldList;
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_field layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.field_item_layout, parent, false);
        return new FieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        // Get the Field object at the current position
        Field field = fieldList.get(position);

        // Bind the data to the views
        holder.textFieldName.setText(field.getTitle());
        holder.textFieldDescription.setText(field.getDescription());
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    // ViewHolder class to hold the views for each item
    public static class FieldViewHolder extends RecyclerView.ViewHolder {
        TextView textFieldName;
        TextView textFieldDescription;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            textFieldName = itemView.findViewById(R.id.text_field_name);
            textFieldDescription = itemView.findViewById(R.id.text_field_description);
        }
    }

    // Method to update the list of fields (useful for refreshing data)
    public void updateFields(List<Field> newFieldList) {
        this.fieldList = newFieldList;
        notifyDataSetChanged();
    }
}