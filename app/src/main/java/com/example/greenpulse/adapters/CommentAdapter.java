package com.example.greenpulse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.R;
import com.example.greenpulse.TimeFormatter;
import com.example.greenpulse.responses.PostDetailsResponse;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    Context context;
    List<PostDetailsResponse.Comment>comments;

    public CommentAdapter(Context context, List<PostDetailsResponse.Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostDetailsResponse.Comment comment = comments.get(position);
        holder.username.setText(comment.user.username);
        holder.role.setText(comment.user.role);
        getCreatedAt(holder.pubDate,comment.createdAt.toString());
        Picasso.get().load(comment.user.dp).error(R.drawable.user).into(holder.dp);
        holder.content.setText(comment.comment);
    }

    private void getCreatedAt(TextView tv,String date) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime past = LocalDateTime.parse(date);
            String formattedDate = TimeFormatter.formatter(past);
            tv.setText(formattedDate);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, role, pubDate,content;
        CircleImageView dp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_user_name_comment_item);
            role = itemView.findViewById(R.id.role_tv_comment_item);
            pubDate = itemView.findViewById(R.id.pub_date_tv_comment_item);
            content = itemView.findViewById(R.id.comment_content);
            dp = itemView.findViewById(R.id.profile_image_comment_item);
        }
    }
}
