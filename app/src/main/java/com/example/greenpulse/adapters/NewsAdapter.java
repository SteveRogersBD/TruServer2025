package com.example.greenpulse.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.greenpulse.R;
import com.example.greenpulse.responses.NewsResponse;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    Context context;
    List<NewsResponse.NewsResult> newsResults;

    public NewsAdapter(Context context, List<NewsResponse.NewsResult> newsResults) {
        this.context = context;
        this.newsResults = newsResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsResponse.NewsResult newsItem = newsResults.get(position);
        String imageUrl = newsItem.thumbnail;
        Glide.with(context).load(imageUrl).into(holder.mainPoster);
        holder.publicationDate.setText(newsItem.date);
        holder.title.setText(newsItem.title);
        // Set up the click listener to open the URL
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the URL
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(newsItem.link)); // Use the URL from the article
                context.startActivity(intent); // Start the activity to open the URL
                //add another activity which contains a web view to start the link.
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mainPoster;
        TextView title,publicationDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainPoster = itemView.findViewById(R.id.poster_news);
            publicationDate = itemView.findViewById(R.id.published_at_news);
            title = itemView.findViewById(R.id.caption_news);
        }
    }
}
