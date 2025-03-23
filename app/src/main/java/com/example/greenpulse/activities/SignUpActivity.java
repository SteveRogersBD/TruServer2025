package com.example.greenpulse.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.greenpulse.MainActivity;
import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.requests.RegisterRequest;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.SharedPrefManager;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.databinding.ActivitySignUpBinding;
import com.example.greenpulse.responses.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends OtherActivity {

    ActivitySignUpBinding binding;
    GPApi gpApi;
    SharedPrefManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sm = new SharedPrefManager(SignUpActivity.this);
        gpApi = RetrofitInstance.gpApi();
        doAnimation();
        //add onClickListenerOnButtons
        binding.signInTvSignUp.setOnClickListener(onClickListener);
        binding.signUpBtnSignUp.setOnClickListener(onClickListener);
        String[] roles = {
                "Farmer", "Student", "Researcher", "Analyst",
                "Explorer"
        };

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, roles);
        typeAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.roleSpinner.setAdapter(typeAdapter);
        binding.roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String role = parent.getSelectedItem().toString();
                binding.roleTv.setText(role);
                binding.roleTv.setTextColor(getColor(R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void doAnimation()
    {
        Pair[] pairs = new Pair[9];
        pairs[0]=new Pair<View,String>(binding.logoSignUp,"logo_image");
        pairs[1]=new Pair<View,String>(binding.htSignUp,"title");
        pairs[2]=new Pair<View,String>(binding.wbSignUp,"subtitle");
        pairs[3]=new Pair<View,String>(binding.subSignUp,"sub_anim");
        pairs[4]=new Pair<View,String>(binding.emailSignUp,"email_anim");
        pairs[5]=new Pair<View,String>(binding.passwordSignUp,"pw_anim");
        pairs[6]=new Pair<View,String>(binding.signUpBtnSignUp,"button_anim");
        pairs[7]=new Pair<View,String>(binding.googleBtnSignUp,"google_anim");
        pairs[8]=new Pair<View,String>(binding.bottomLinearSignUp,"linear_anim");
        ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,
                pairs);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id==binding.signInTvSignUp.getId())
            {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
            else if(id==binding.signUpBtnSignUp.getId())
            {
                registerUser();
            }
        }
    };

    private void registerUser()
    {
        String userName = binding.nameSignUp.getText().toString();
        String email = binding.emailSignUp.getText().toString();
        String password = binding.passwordSignUp.getText().toString();
        String role = binding.roleTv.getText().toString();
        if(userName.equals("")) binding.nameSignUp.setError("Enter a valid username");
        if(email.equals("")) binding.emailSignUp.setError("Enter a valid username");
        if(password.equals("")) binding.passwordSignUp.setError("Enter a valid username");
        if(role.equals("")) binding.roleTv.setError("Enter a valid username");
        RegisterRequest req= new RegisterRequest(userName,email,password,role);
        gpApi.registerUser(req).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
//                    UserResponse.Data user = response.body().data;
//                    sm.saveUserInfo(user.username,user.email,user.role, (String) user.dp);
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable throwable) {
                Toast.makeText(SignUpActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}