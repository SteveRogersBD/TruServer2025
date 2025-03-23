package com.example.greenpulse.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.greenpulse.BaseActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.responses.CropResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setUpBar();


        //searchView logic
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        SearchView searchView = findViewById(R.id.searchView2);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                GPApi cropApi = RetrofitInstance.gpApi();
                cropApi.getCropByName(query).enqueue(new Callback<CropResponse>() {
                    @Override
                    public void onResponse(Call<CropResponse> call, Response<CropResponse> response) {
                        if(response.isSuccessful() && response.body()!=null)
                        {
                            Intent intent = new Intent(SearchActivity.this,
                                    CropDetailsActivity.class);
                            intent.putExtra("id", response.body().data.cropId);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<CropResponse> call, Throwable throwable) {
                        Toast.makeText(SearchActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setUpBar() {
        GradientDrawable gradientDrawable = (GradientDrawable) getResources().
                getDrawable(R.drawable.grad_3);
        int startColor = gradientDrawable.getColors()[0];
        int endColor = gradientDrawable.getColors()[1];
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(endColor);
        int bottomColor = Color.parseColor("#153E50");
        window.setNavigationBarColor(bottomColor);
    }
}