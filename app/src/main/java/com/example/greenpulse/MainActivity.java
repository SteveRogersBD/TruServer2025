package com.example.greenpulse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.greenpulse.activities.AnalyzeActivity;
import com.example.greenpulse.activities.ChatBotActivity;
import com.example.greenpulse.activities.MapActivity;
import com.example.greenpulse.adapters.VPAdapter;
import com.example.greenpulse.databinding.ActivityMainBinding;
import com.example.greenpulse.fragments.HomeFragment;
import com.example.greenpulse.fragments.MediaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    VPAdapter vpAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setting up the status bar and navigation bar colors
        setUpBar();



        //logic related to the tabLayout
        binding.viewPager.setOffscreenPageLimit(1);
        binding.tabMode.setupWithViewPager(binding.viewPager);
        vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new HomeFragment(),"Home");
        vpAdapter.addFragment(new MediaFragment(),"Agri World");
        binding.viewPager.setAdapter(vpAdapter);

        binding.bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.analyze_bottom)
                {
                    startActivity(new Intent(MainActivity.this, AnalyzeActivity.class));
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
                else if(id==R.id.map_bottom)
                {
                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                }else if(id==R.id.schedule_bottom)
                {
                    startActivity(new Intent(MainActivity.this, AnalyzeActivity.class));
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
                else if(id==R.id.bot_bottom)
                {
                    startActivity(new Intent(MainActivity.this, ChatBotActivity.class));
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
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