package com.example.greenpulse.apiInterfaces;

import android.content.Context;

import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.responses.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherApiUtil {
    public static final WeatherApi weatherApi = RetrofitInstance.WeatherFit.create(WeatherApi.class);;
    public static final int DAILY = 1;
    public static final int FORECAST = 2;
    public Context context;
    public WeatherApiUtil(){}

    public WeatherApiUtil(Context context) {
        this.context = context;
    }

    public <T> void getWeather(double lat, double lon, int tag, WeatherCallBack callBack)
    {
        Call<T>call = null;
        if(tag==DAILY)
        {
            call = (Call<T>) weatherApi.getWeather(lat,lon,context.getString(R.string.weatherKey));
        } else if (tag==FORECAST) {
            //do something with alerts
        }
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if(response.body()!=null && response.isSuccessful())
                {
                    try{
                        if(tag== DAILY){
                            WeatherResponse weatherResponse = (WeatherResponse) response.body();
                            callBack.onSuccess(weatherResponse.data);
                        }

                    }catch (Exception e)
                    {
                        callBack.onError(e.getMessage());
                    }
                }
                else{
                    callBack.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable throwable) {
                callBack.onError(throwable.getLocalizedMessage());
            }
        });
    }

    public interface WeatherCallBack<T>{
        public void onSuccess(List<T>resultList);
        public void onError(String errorMessage);
    }

}
