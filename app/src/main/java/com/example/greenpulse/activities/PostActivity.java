package com.example.greenpulse.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.TimeFormatter;
import com.example.greenpulse.adapters.CommentAdapter;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.databinding.ActivityPostBinding;
import com.example.greenpulse.models.Comment;
import com.example.greenpulse.responses.PostDetailsResponse;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends OtherActivity {

    ActivityPostBinding binding;
    GPApi gpApi;
    CommentAdapter commentAdapter;
    List<PostDetailsResponse.Comment>comments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Toolbar actionBar = findViewById(R.id.toolbar_post);
        if(actionBar!=null)
        {
            setSupportActionBar(actionBar);
            actionBar.setTitle("Post");
        }

        Intent intent = getIntent();
        int id = intent.getIntExtra("id",1);
        //Toast.makeText(PostActivity.this, id+"", Toast.LENGTH_SHORT).show();
        gpApi = RetrofitInstance.gpApi();
        gpApi.getPostDetails(Long.valueOf(id)).enqueue(new Callback<PostDetailsResponse>() {
            @Override
            public void onResponse(Call<PostDetailsResponse> call, Response<PostDetailsResponse> response) {
                if(response.body()!=null && response.isSuccessful())
                {
                    try{
                        runOnUiThread(()->{
                            updateUI(response.body());
                        });
                    }catch (Exception e)
                    {
                        Toast.makeText(PostActivity.this, "Error!!!",
                                Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(PostActivity.this, "Error!!!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PostDetailsResponse> call, Throwable throwable) {
                Toast.makeText(PostActivity.this, "Error!!!",
                        Toast.LENGTH_LONG).show();
            }
        });
        binding.sendButtonPA.setOnClickListener((v)->{
            String commentContent = binding.messageInputPA.getText().toString();
            if(commentContent.equals(""))
            {
                binding.messageInputPA.setError("Add a comment first!!!");
                return;
            }
            gpApi.createComment(1L,Long.valueOf(id),commentContent).enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    if(response.isSuccessful() && response.body()!=null)
                    {
                        try{
                            Toast.makeText(PostActivity.this, "Comment Created Successfully!!!",
                                    Toast.LENGTH_SHORT).show();
                            binding.messageInputPA.setText("");
                            //commentAdapter.notifyDataSetChanged();
                            //comments.add(response.body());
                            commentAdapter.notifyItemInserted(comments.size()-1);
                            binding.commentRecycler.scrollToPosition(comments.size()-1);
                        }catch (Exception e)
                        {
                            Toast.makeText(PostActivity.this, "Error catch!!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(PostActivity.this, "Error else!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable throwable) {
                    Toast.makeText(PostActivity.this, "Error failure!!!", Toast.LENGTH_SHORT).show();
                }
            });

        });

    }

    private void updateUI(PostDetailsResponse post) {
        Picasso.get().load(post.data.image).into(binding.postImagePA);
        Picasso.get().load(post.data.user.dp).into(binding.profileImagePA);
        binding.usernamePA.setText(post.data.user.username);
        binding.rolePA.setText(post.data.user.role);
        getPostDate(binding.postDatePA,post.data.createdAt);
        binding.captionPA.setText(post.data.title);
        binding.descriptionPA.setText(post.data.content);
        comments = new ArrayList<>();
        comments.addAll(post.data.comments);
        commentAdapter = new CommentAdapter(PostActivity.this,comments);
        binding.commentRecycler.setAdapter(commentAdapter);
        binding.commentRecycler.setLayoutManager(new LinearLayoutManager(PostActivity.this));

    }


    private void getPostDate(TextView postDatePA, String createdAt) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            postDatePA.setText(TimeFormatter.formatter(LocalDateTime.parse(createdAt)));
        }
    }
}