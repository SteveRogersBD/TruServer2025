package com.example.greenpulse.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.adapters.ImageAdapter;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.apiInterfaces.NewsApiUtil;
import com.example.greenpulse.databinding.ActivityCropDetailsBinding;
import com.example.greenpulse.responses.CropResponse;
import com.example.greenpulse.responses.ImageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropDetailsActivity extends AppCompatActivity {

    ActivityCropDetailsBinding binding;
    NewsApiUtil newsApiUtil;
    ImageAdapter imageAdapter;
    CropResponse.Data crop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCropDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int id = getIntent().getIntExtra("id",0);
        Toast.makeText(this, ""+id, Toast.LENGTH_LONG).show();

        newsApiUtil = new NewsApiUtil(CropDetailsActivity.this);
        getTheCrop(id);
        //updateUI();

    }



    private void getTheCrop(int id) {
        GPApi cropAPI = RetrofitInstance.gpApi();
        cropAPI.getCropById(id).enqueue(new Callback<CropResponse>() {
            @Override
            public void onResponse(Call<CropResponse> call, Response<CropResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    crop = response.body().data;
                    updateUI();
                }
            }

            @Override
            public void onFailure(Call<CropResponse> call, Throwable throwable) {
                Toast.makeText(CropDetailsActivity.this, "Error!!!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void updateUI() {
        getImages(crop.cropName);
        if (crop != null) {
            // Set the text for each TextView using the crop object
            binding.tvCropName.setText("Crop Name: " + crop.cropName);
            binding.tvScientificName.setText("Scientific Name: " + crop.scientificName);
            binding.tvCropType.setText("Crop Type: " + crop.cropType);
            binding.tvTemperature.setText("Optimal Temperature: " + crop.optimalTemperatureMin + "°C - " + crop.optimalTemperatureMax + "°C");
            binding.tvSoilType.setText("Soil Type: " + crop.soilType);
            binding.tvSoilPh.setText("Soil pH: " + crop.soilPhMin + " - " + crop.soilPhMax);
            binding.tvWaterRequirement.setText("Water Requirement: " + crop.waterRequirement);
            binding.tvSunlightRequirement.setText("Sunlight: " + crop.sunlightRequirement);
            binding.tvPlantingSeason.setText("Planting Season: " + crop.plantingSeason);
            binding.tvHarvestingPeriod.setText("Harvesting: " + crop.harvestingPeriod);
            binding.tvFertilizer.setText("Fertilizer: " + crop.fertilizerRecommendations);
            binding.tvNutrients.setText("Nutrient Requirements: " + crop.nutrientRequirements);
            binding.tvPests.setText("Pests: " + crop.pests);
            binding.tvDiseases.setText("Diseases: " + crop.diseases);
            binding.tvTreatment.setText("Treatment: " + crop.treatmentSuggestions);
            binding.tvIrrigation.setText("Irrigation: " + crop.irrigationMethods);
            binding.tvStorage.setText("Storage: " + crop.storageConditions);
            binding.tvShortDescription.setText("Description: " + crop.shortDescription);
        }

    }

    private void getImages(String query) {
        newsApiUtil.getResources(query, NewsApiUtil.IMAGES,
                new NewsApiUtil.NewsCallBack2<ImageResponse.ImagesResult>() {
                    @Override
                    public void onSuccess(List<ImageResponse.ImagesResult> resourceList) {
                        runOnUiThread(() -> {
                            // Set the adapter for the RecyclerView
                            imageAdapter = new ImageAdapter(CropDetailsActivity.this, resourceList);
                            binding.imageRecycler.setAdapter(imageAdapter);

                            // Set the layout manager for the RecyclerView
                            binding.imageRecycler.setLayoutManager(
                                    new LinearLayoutManager(CropDetailsActivity.this,
                                            LinearLayoutManager.HORIZONTAL, false)
                            );
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(CropDetailsActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

}