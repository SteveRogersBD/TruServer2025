package com.example.greenpulse;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        // Get the background drawable (gradient)
        GradientDrawable gradientDrawable = (GradientDrawable) getResources().
                getDrawable(R.drawable.onboard_bg);

        // Extract the start color from the gradient (you can also use the end color if you prefer)
        int startColor = gradientDrawable.getColors()[0];  // First color of the gradient
        int endColor = gradientDrawable.getColors()[1];
        // Set the status bar color to match the gradient start color
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(startColor);  // Set status bar color to the start color of the gradient
        window.setNavigationBarColor(endColor);
    }
}