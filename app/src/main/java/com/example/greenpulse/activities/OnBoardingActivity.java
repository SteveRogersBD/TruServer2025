package com.example.greenpulse.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.greenpulse.BaseActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.adapters.OnBoardAdapter;
import com.example.greenpulse.databinding.ActivityOnBoardingBinding;
import com.example.greenpulse.models.OnBoardItem;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends BaseActivity {

    OnBoardAdapter adapter;
    List<OnBoardItem> boardItems;
    ActivityOnBoardingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null) actionBar.hide();

        String [] titles = {
                "Social Media",
                "Map Activity",
                "Youtube/Google",
                "Plant Analyzer",
                "ChatBot"
        };

        String []subTitles = {getString(R.string.social),
                getString(R.string.map),
                getString(R.string.trends),
                getString(R.string.disease_analyzer),
                getString(R.string.bot)};
        int []images = {R.drawable.geofencewithoubg,
                R.drawable.map,
                R.drawable.reels,
                R.drawable.analyze,
                R.drawable.bot};

        boardItems = new ArrayList<>();
        for(int i=0;i<5;i++)
        {
            OnBoardItem item = new OnBoardItem(titles[i],subTitles[i],images[i]);
            boardItems.add(item);
        }

        adapter = new OnBoardAdapter(this,boardItems);
        binding.pagerOnboard.setAdapter(adapter);
        binding.dots.setViewPager2(binding.pagerOnboard);
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = binding.pagerOnboard.getCurrentItem();
                if (currentItem < binding.pagerOnboard.getAdapter().getItemCount() - 1) {
                    //do something later
                    binding.pagerOnboard.setCurrentItem(currentItem + 1);

                } else {
                    OnBoardingActivity.this.startActivity(new Intent(OnBoardingActivity.this,
                            StartingActivity.class));
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            }
        });

    }
}