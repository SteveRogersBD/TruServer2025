package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.models.Comment;
import com.example.greenpulse.requests.LogInRequest;
import com.example.greenpulse.requests.RegisterRequest;
import com.example.greenpulse.responses.AllPostResponse;
import com.example.greenpulse.responses.CropResponse;
import com.example.greenpulse.responses.PostDetailsResponse;
import com.example.greenpulse.responses.PostResponse;
import com.example.greenpulse.responses.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GPApi {
    @POST("user/register")
    Call<UserResponse>registerUser(@Body RegisterRequest request);

    @POST("user/login")
    Call<UserResponse>logInUser(@Body LogInRequest request);

    @Multipart
    @POST("post/create/user/{userId}")
    Call<PostResponse> createPost(
            @Path("userId") Long userId,
            @Part("title") RequestBody title,
            @Part("content") RequestBody content,
            @Part MultipartBody.Part file // To handle file uploads
    );
    // Define the GET request for retrieving all posts
    @GET("admin/all")
    Call<AllPostResponse> getAllPosts();

    @GET("crop/get/name/{name}")
    Call<CropResponse>getCropByName(@Path("name") String name);

    @GET("crop/get/{id}")
    Call<CropResponse>getCropById(@Path("id") int id);

    @GET("user/id/{id}")
    Call<UserResponse>getUserById(@Path("id") Long id);

    @GET("post/get/id/{id}")
    Call<PostDetailsResponse>getPostDetails(@Path("id") Long id);

    @POST("comment/create/user/{userId}/post/{postId}")
    Call<Comment>createComment(@Path("userId") Long userId,
                               @Path("postId") Long postId,
                               @Query("comment") String comment);

}
