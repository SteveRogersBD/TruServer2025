package com.example.greenpulse.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.R;
import com.example.greenpulse.responses.ImageResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    Context context;
    List<ImageResponse.ImagesResult>images;

    public ImageAdapter(Context context, List<ImageResponse.ImagesResult> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.google_image_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageResponse.ImagesResult image = images.get(position);
        Picasso.get().load(image.thumbnail).resize(300,200).
                centerCrop().
                into(holder.poster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(image.link)); // Replace with your desired URL
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.google_image);
        }
    }
}
