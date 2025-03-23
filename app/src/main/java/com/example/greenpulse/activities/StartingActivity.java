package com.example.greenpulse.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.greenpulse.BaseActivity;
import com.example.greenpulse.MainActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.databinding.ActivityStartingBinding;

public class StartingActivity extends BaseActivity {

    ActivityStartingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar action = getSupportActionBar();
        if(action!=null) action.hide();

        binding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartingActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });


    }
}