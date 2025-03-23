package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.responses.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("forecast/daily")
    Call<WeatherResponse>getWeather(@Query("lat") double lat,
                                    @Query("lon") double lon,
                                    @Query("key") String key);
}
