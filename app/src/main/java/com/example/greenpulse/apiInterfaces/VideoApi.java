package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.responses.YouTubeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface VideoApi {
    @Headers({
            "X-RapidAPI-Key: 97c39a77e0msh797e036b282f6e8p108b8ajsn0e96ed05f8e6",
            "X-RapidAPI-Host: yt-api.p.rapidapi.com"
    })
    @GET("search")
    Call<YouTubeResponse> searchVideos(
            @Query("query") String query
    );

}
