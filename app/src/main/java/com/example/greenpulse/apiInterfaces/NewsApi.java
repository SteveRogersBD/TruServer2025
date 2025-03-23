package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.responses.ImageResponse;
import com.example.greenpulse.responses.NewsResponse;
import com.example.greenpulse.responses.YoutubeVideo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("search.json")
    Call<NewsResponse>getNews(@Query("api_key") String apiKey,
                              @Query("engine") String engine,
                              @Query("q") String query,
                              @Query("gl") String gl,
                              @Query("hl") String hl);

    @GET("search.json")
    Call<ImageResponse>getImages(@Query("api_key") String apiKey,
                                 @Query("engine") String engine,
                                 @Query("q") String query);

    @GET("search.json")
    Call<YoutubeVideo> getVideos(
            @Query("api_key") String apiKey,
            @Query("engine") String engine,
            @Query("search_query") String query
    );
}
