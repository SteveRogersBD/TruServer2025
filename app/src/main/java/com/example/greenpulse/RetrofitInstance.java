package com.example.greenpulse;

import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.apiInterfaces.NewsApi;
import com.example.greenpulse.apiInterfaces.VideoApi;
import com.example.greenpulse.apiInterfaces.WeatherApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static final Retrofit NewsFit = new Retrofit.Builder().
            baseUrl("https://serpapi.com/").
            addConverterFactory(GsonConverterFactory.create())
            .build();
    public static final Retrofit WeatherFit = new Retrofit.Builder().
            baseUrl("https://api.weatherbit.io/v2.0/").
            addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final Retrofit GPFit = new Retrofit.Builder().
            baseUrl("http://150.243.210.141:8080/").
            addConverterFactory(GsonConverterFactory.create())
            .build();


    public static NewsApi newsApi(){return NewsFit.create(NewsApi.class);}
    public static WeatherApi weatherApi(){return WeatherFit.create(WeatherApi.class);}
    public static GPApi gpApi(){return GPFit.create(GPApi.class);}

}