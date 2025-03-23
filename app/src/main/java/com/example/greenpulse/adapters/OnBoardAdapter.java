package com.example.greenpulse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.R;
import com.example.greenpulse.models.OnBoardItem;
import java.util.List;

public class OnBoardAdapter extends RecyclerView.Adapter<OnBoardAdapter.ViewHolder>{

    Context context;
    List<OnBoardItem>list;

    public OnBoardAdapter(Context context, List<OnBoardItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.board_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OnBoardItem item = list.get(position);
        holder.textView.setText(item.getTitle());
        holder.imageView.setImageResource(item.getImage());
        holder.sTextView.setText(item.getSubTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        TextView sTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.onboard_item_title);
            sTextView = itemView.findViewById(R.id.onboard_item_sub);
            imageView = itemView.findViewById(R.id.onboard_item_poster);
        }
    }
}
