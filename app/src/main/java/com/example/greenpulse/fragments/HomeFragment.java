package com.example.greenpulse.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.activities.SearchActivity;
import com.example.greenpulse.adapters.NewsAdapter;
import com.example.greenpulse.adapters.TextAdapter;
import com.example.greenpulse.adapters.WeatherAdapter;
import com.example.greenpulse.apiInterfaces.NewsApiUtil;
import com.example.greenpulse.apiInterfaces.WeatherApi;
import com.example.greenpulse.databinding.FragmentHomeBinding;
import com.example.greenpulse.responses.NewsResponse;
import com.example.greenpulse.responses.WeatherResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private FragmentHomeBinding binding;
    private List<String> headers;
    private TextAdapter headerAdapter;
    private NewsAdapter newsAdapter;
    private List<NewsResponse.NewsResult> newsResults = new ArrayList<>();
    private NewsApiUtil newsApiUtil;
    private WeatherAdapter weatherAdapter;
    private FusedLocationProviderClient locationProviderClient;
    private final int LOCATION_REQUEST_CODE = 100;
    private SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        sp = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        askLocationPermission();
        addTheHeaders();
        initializeRecyclerViews();
        binding.searchViewHome.setOnClickListener((v)->{
            startActivity(new Intent(getContext(), SearchActivity.class));
        });

        newsApiUtil = new NewsApiUtil(requireContext());
        binding.newsPd.setVisibility(View.VISIBLE);
        updateNewsArticles("Farming & Agriculture");
        getWeatherInfo();
        return binding.getRoot();
    }


    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            getAndStoreLocation();
            getWeatherInfo();
        }
    }

    private void addTheHeaders() {
        headers = new ArrayList<>();
        headers.add("All");
        headers.add("Trending");
        headers.add("Farming");
        headers.add("Hacks");
        headers.add("Fertilizers");
        headers.add("Sustainability");
        headers.add("Agriculture");
        headers.add("Technology");
        headerAdapter = new TextAdapter(requireContext(), headers, new TextAdapter.OnItemClickListener() {
            @Override
            public void onClick(String item) {
                binding.newsPd.setVisibility(View.VISIBLE);
                updateNewsArticles(item);
            }
        });
        binding.textRecycler.setAdapter(headerAdapter);
        binding.textRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }


    public void updateNewsArticles(String query) {
        binding.newsPd.setVisibility(View.VISIBLE);
        newsApiUtil.getNews(query, new NewsApiUtil.NewsCallBack() {
            @Override
            public void onSuccess(List<NewsResponse.NewsResult> newsList) {
                binding.newsPd.setVisibility(View.INVISIBLE);
                newsResults.clear();
                newsResults.addAll(newsList);
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                binding.newsPd.setVisibility(View.INVISIBLE);
                Toast.makeText(requireContext(), "Failed to load news: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initializeRecyclerViews() {
        newsAdapter = new NewsAdapter(requireContext(), newsResults);
        binding.newsRecyclerHome.setAdapter(newsAdapter);
        binding.newsRecyclerHome.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }



    private void getWeatherInfo() {

        WeatherApi weatherApi = RetrofitInstance.weatherApi();
        weatherApi.getWeather(34.67,99.89,getString(R.string.weatherKey)).
                enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    weatherAdapter = new WeatherAdapter(getContext(),response.body().data);
                    binding.weatherRecyclerHome.setAdapter(weatherAdapter);
                    binding.weatherRecyclerHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable throwable) {
                binding.errorTv.setText(throwable.getLocalizedMessage());
                //Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getAndStoreLocation() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("lat", String.valueOf(lat));
                editor.putString("lon", String.valueOf(lon));
                editor.apply();
            }
        });
    }

    private void retrieveLocation(String[] location) {
        location[0] = sp.getString("lat", null);
        location[1] = sp.getString("lon", null);
    }
}

