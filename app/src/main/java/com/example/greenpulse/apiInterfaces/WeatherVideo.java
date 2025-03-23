package com.example.greenpulse.apiInterfaces;

import com.example.greenpulse.responses.WeatherDisease;
import com.example.greenpulse.responses.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherVideo {
    @GET("weather_disease")
    Call<WeatherDisease> getWeatherDisease(
            @Query("lat") String latitude,
            @Query("lon") String longitude
    );
}
