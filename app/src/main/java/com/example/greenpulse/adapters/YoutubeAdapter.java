package com.example.greenpulse.adapters;

import android.content.ActivityNotFoundException;
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
import com.example.greenpulse.responses.YouTubeResponse;
import com.example.greenpulse.responses.YoutubeVideo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.ViewHolder>{

    Context context;
    List<YoutubeVideo.VideoResult>videos;

    public YoutubeAdapter(Context context, List<YoutubeVideo.VideoResult> videos) {
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the video item
        YoutubeVideo.VideoResult item = videos.get(position);

        // Set video title
        holder.caption.setText(item.title);

        // Set channel name
        holder.chanelName.setText(item.channel.name);

        // Set published date
        holder.publishedDate.setText(item.published_date);

        // Load video thumbnail using Glide
        Glide.with(context)
                .load(item.thumbnail.mystatic)
                .into(holder.videoThumbnail);

        // Load channel thumbnail (if available) using Glide
        Glide.with(context)
                .load(item.channel.thumbnail)
                .into(holder.chanelThumbnail);

        // Set view count (if available in statistics)
        holder.views.setText(item.views);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the URL for the YouTube video
                String videoUrl = item.link;

                // Create an intent to open the video in the YouTube app or browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://com.google.android.youtube"));

                // Try to open the YouTube app, if not available, open the browser
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // If YouTube is not installed, fallback to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnail;
        CircleImageView chanelThumbnail;
        TextView caption, chanelName, views, publishedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.poster_video);
            chanelThumbnail = itemView.findViewById(R.id.chanel_img);
            caption = itemView.findViewById(R.id.caption);
            chanelName = itemView.findViewById(R.id.chanel_name);
            views = itemView.findViewById(R.id.views_video);
            publishedDate = itemView.findViewById(R.id.date_video);
        }
    }
}
