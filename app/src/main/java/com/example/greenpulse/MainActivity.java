package com.example.greenpulse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.greenpulse.activities.AnalyzeActivity;
import com.example.greenpulse.activities.ChatBotActivity;
import com.example.greenpulse.activities.MapActivity;
import com.example.greenpulse.adapters.VPAdapter;
import com.example.greenpulse.databinding.ActivityMainBinding;
import com.example.greenpulse.fragments.HomeFragment;
import com.example.greenpulse.fragments.MediaFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    ActivityMainBinding binding;
    VPAdapter vpAdapter;
    FusedLocationProviderClient fusedLocationClient;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize FusedLocationProviderClient and SharedPreferences
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPreferences = getSharedPreferences("UserLocation", Context.MODE_PRIVATE);
        // Setting up the status bar and navigation bar colors
        setUpBar();
        askLocationPermission();



        // Logic related to the tabLayout
        binding.viewPager.setOffscreenPageLimit(1);
        binding.tabMode.setupWithViewPager(binding.viewPager);
        vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new HomeFragment(), "Home");
        vpAdapter.addFragment(new MediaFragment(), "Agri World");
        binding.viewPager.setAdapter(vpAdapter);

        binding.bottomBar.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.analyze_bottom) {
                startActivity(new Intent(MainActivity.this, AnalyzeActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else if (id == R.id.map_bottom) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else if (id == R.id.schedule_bottom) {
                startActivity(new Intent(MainActivity.this, AnalyzeActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else if (id == R.id.bot_bottom) {
                startActivity(new Intent(MainActivity.this, ChatBotActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
            return false;
        });
    }

    private void askLocationPermission() {
        // Check if the permission is already granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, fetch location
            fetchLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch location
                fetchLocation();
            } else {
                // Permission denied, show a message
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchLocation() {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            // Get the location (latitude and longitude)
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // Save the location in SharedPreferences
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("latitude", String.valueOf(latitude));
//                            editor.putString("longitude", String.valueOf(longitude));
//                            editor.apply();
                            SharedPrefManager sm = new SharedPrefManager(MainActivity.this);
                            sm.saveLocation(latitude,longitude);
                            // Display location (for debugging purposes)
                            Toast.makeText(MainActivity.this, "Location: " + latitude + ", " + longitude, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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


