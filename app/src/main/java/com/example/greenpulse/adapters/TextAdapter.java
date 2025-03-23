package com.example.greenpulse.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.MainActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.fragments.HomeFragment;

import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder>{

    Context context;
    List<String>titles;
    OnItemClickListener onItemClickListener;

    public TextAdapter(Context context, List<String> titles, OnItemClickListener clickListener) {
        this.context = context;
        this.titles = titles;
        this.onItemClickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = titles.get(position);
        holder.textView.setText(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(item);
            }
        });
    }
    public interface OnItemClickListener{
        public void onClick(String item);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title_list);
        }
    }
}
