package com.example.greenpulse;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        GradientDrawable gradientDrawable = (GradientDrawable) getResources().
                getDrawable(R.drawable.grad_3);
        int startColor = gradientDrawable.getColors()[0];
        int endColor = gradientDrawable.getColors()[1];
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(endColor);
        //int bottomColor = Color.parseColor("#153E50");
        window.setNavigationBarColor(startColor);
    }
}