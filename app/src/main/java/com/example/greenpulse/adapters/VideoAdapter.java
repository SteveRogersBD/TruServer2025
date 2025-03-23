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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final Context context;
    private final List<YouTubeResponse.Datum> list; // Assuming 'Item' is the video data object.

    public VideoAdapter(Context context, List<YouTubeResponse.Datum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the video item layout
        View view = LayoutInflater.from(context).inflate(R.layout.video_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the video item
        YouTubeResponse.Datum item = list.get(position);

        // Set video title
        holder.caption.setText(item.title);

        // Set channel name
        holder.chanelName.setText(item.channelTitle);

        // Set published date
        holder.publishedDate.setText(item.publishedTimeText);

        // Load video thumbnail using Glide
//        Glide.with(context)
//                .load(item.thumbnail.get(0).url)
//                .into(holder.videoThumbnail);

        // Load channel thumbnail (if available) using Glide
        Glide.with(context)
                .load(item.channelThumbnail.get(0).url)
                .into(holder.chanelThumbnail);

        // Set view count (if available in statistics)
        if (item.viewCount != null) {
            holder.views.setText(formatViews(item.viewCount));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the video ID from the item (using the `videoId` field)
                String videoId = item.videoId;

                // Create the URL for the YouTube video
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

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
        return list != null ? list.size() : 0;
    }

    // ViewHolder class
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

    // Helper method to format view count
    private String formatViews(String viewCount) {
        if (viewCount == null) return "N/A";
        long views = Long.parseLong(viewCount);
        if (views >= 1_000_000) {
            return (views / 1_000_000) + "M views";
        } else if (views >= 1_000) {
            return (views / 1_000) + "K views";
        } else {
            return views + " views";
        }
    }
}
