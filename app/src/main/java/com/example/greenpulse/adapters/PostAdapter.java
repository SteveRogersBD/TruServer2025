package com.example.greenpulse.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.TimeFormatter;
import com.example.greenpulse.activities.PostActivity;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.responses.AllPostResponse;
import com.example.greenpulse.responses.UserResponse;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    Context context;
    List<AllPostResponse.Datum>postItems;

    public PostAdapter(Context context, List<AllPostResponse.Datum> postItems) {
        this.context = context;
        this.postItems = postItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllPostResponse.Datum postItem = postItems.get(position);
        holder.title.setText(postItem.title);
        Picasso.get().load(postItem.image).into(holder.postImage);
        getUserName(postItem.userId,holder.username,holder.userDP,holder.roleTV);
        getCreatedAt(postItem.createdAt,holder.createdAt);
        holder.commentCount.setText(postItem.commentCount+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra("id",postItem.postId);
                context.startActivity(intent);
            }
        });
    }

    private void getCreatedAt(String createdAt, TextView tv) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime time = LocalDateTime.parse(createdAt);
            tv.setText(TimeFormatter.formatter(time));
        }
    }

    private void getUserName(int userId,TextView tv,CircleImageView dp,TextView role) {
        GPApi nameApi = RetrofitInstance.gpApi();
        Long id = Long.valueOf(userId);
        nameApi.getUserById((long) userId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    tv.setText(response.body().data.username);
                    Picasso.get().load(response.body().data.dp).into(dp);
                    role.setText(response.body().data.role);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable throwable) {
                Toast.makeText(context, "error!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userDP;
        ImageView postImage;
        TextView username,createdAt,title,commentCount,roleTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userDP = itemView.findViewById(R.id.profile_image_post_item);
            commentCount = itemView.findViewById(R.id.tv_comment_count);
            username = itemView.findViewById(R.id.tv_user_name_post_item);
            createdAt = itemView.findViewById(R.id.pub_date_tv_post_item);
            title = itemView.findViewById(R.id.title_post_item);
            postImage = itemView.findViewById(R.id.post_image_post_item);
            roleTV = itemView.findViewById(R.id.role_tv_post_item);
        }
    }
}
