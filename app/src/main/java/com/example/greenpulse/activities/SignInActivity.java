package com.example.greenpulse.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.greenpulse.MainActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.SharedPrefManager;
import com.example.greenpulse.requests.LogInRequest;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.databinding.ActivitySignInBinding;
import com.example.greenpulse.responses.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    SharedPrefManager sm;
    GPApi gpApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sm = new SharedPrefManager(SignInActivity.this);
        gpApi = RetrofitInstance.gpApi();
        binding.signUpTvSignIn.setOnClickListener(onClickListener);
        binding.signInBtnSignIn.setOnClickListener(onClickListener);
        binding.googleBtnSignIn.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id==binding.signUpTvSignIn.getId())
            {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            } else if (id==binding.googleBtnSignIn.getId()) {

            } else if (id==binding.signInBtnSignIn.getId()) {

                logInUser();
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        }
    };
    private void logInUser()
    {
        String email = binding.emailSignIn.getText().toString();
        String password = binding.passwordSignIn.getText().toString();
        if(email.equals("")) binding.emailSignIn.setError("Enter a valid username");
        if(password.equals("")) binding.passwordSignIn.setError("Enter a valid password");
        LogInRequest request = new LogInRequest(email,password);
        gpApi.logInUser(request).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    UserResponse.Data user = response.body().data;
                    sm.saveUserInfo(user.username,user.email,user.role, (String) user.dp);
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable throwable) {
                Toast.makeText(SignInActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}