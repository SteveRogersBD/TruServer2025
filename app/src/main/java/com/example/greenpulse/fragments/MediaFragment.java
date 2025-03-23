package com.example.greenpulse.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.activities.CreatePostActivity;
import com.example.greenpulse.adapters.PostAdapter;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.databinding.FragmentMediaBinding;
import com.example.greenpulse.models.Post;
import com.example.greenpulse.responses.AllPostResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaFragment extends Fragment {
    public MediaFragment() {
        // Required empty public constructor
    }
    FragmentMediaBinding binding;
    GPApi gpApi;
    PostAdapter postAdapter;
    List<AllPostResponse.Datum>postList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMediaBinding.inflate(inflater, container, false);
        //gpApi = RetrofitInstance.gpApi();
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(),postList);
        binding.postRecycler.setAdapter(postAdapter);
        binding.postRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        getPosts();
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshPosts();
        });
        binding.questionTv.setOnClickListener((v)->{
            getContext().startActivity(new Intent(getContext(), CreatePostActivity.class));
        });
        binding.postRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(binding.postRecycler.canScrollVertically(-1))
                {
                    getPosts();
                }
            }
        });

        return binding.getRoot();
    }
    private void getPosts()
    {
        gpApi = RetrofitInstance.gpApi();
        gpApi.getAllPosts().enqueue(new Callback<AllPostResponse>() {
            @Override
            public void onResponse(Call<AllPostResponse> call, Response<AllPostResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    postList.addAll(response.body().data);
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<AllPostResponse> call, Throwable throwable) {
                Toast.makeText(getContext(), throwable.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    private void refreshPosts() {
        // Clear the current post list and fetch fresh posts
        postList.clear();
        getPosts();

        // Stop the refreshing animation
        binding.swipeRefreshLayout.setRefreshing(false);
    }
}