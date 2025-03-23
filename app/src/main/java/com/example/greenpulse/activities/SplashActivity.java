package com.example.greenpulse.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.greenpulse.BaseActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity {

    ActivitySplashBinding binding;
    Animation top,bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //hide the actionbar
        ActionBar actionBar = getActionBar();
        if(actionBar!=null) actionBar.hide();

        //initialize
        top = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //hooks
        binding.logo.setAnimation(top);
        binding.name.setAnimation(bottom);
        binding.slogan.setAnimation(bottom);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        },3000);

    }
}