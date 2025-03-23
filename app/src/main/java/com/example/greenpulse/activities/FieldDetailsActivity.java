package com.example.greenpulse.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.greenpulse.R;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.apiInterfaces.WeatherVideo;
import com.example.greenpulse.databinding.ActivityFieldDetailsBinding;
import com.example.greenpulse.models.Field;
import com.example.greenpulse.responses.WeatherDisease;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FieldDetailsActivity extends AppCompatActivity {


    ActivityFieldDetailsBinding binding;
    DatabaseReference fieldDb;
    WeatherVideo weatherVideo;
    Field mainField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFieldDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpBar();

        //initalize
        weatherVideo = RetrofitInstance.weatherVideo();


        String field = getIntent().getStringExtra("field");
        fieldDb = FirebaseDatabase.getInstance().getReference().child("fields").
                child(field);

        retrieveField(field);
        binding.buttonThreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherVideo.getWeatherDisease(""+mainField.getCenterPoint().getLatitude(),
                        ""+mainField.getCenterPoint().getLongitude()).
                        enqueue(new Callback<WeatherDisease>() {
                            @Override
                            public void onResponse(Call<WeatherDisease> call, Response<WeatherDisease> response) {
                                if(response.isSuccessful() && response.body()!=null)
                                {
                                    if(response.body().predicted_diseases.size()==0)
                                    {
                                        Toast.makeText(FieldDetailsActivity.this,
                                                "No Alerts", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<WeatherDisease> call, Throwable throwable) {

                            }
                        });
            }
        });
        binding.imageField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to pick an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // Optional: Set type to ensure we only get images
                intent.setType("image/*");

                // Start the activity for result
                // Using startActivityForResult with a request code
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });



    }

    private void retrieveField(String fieldKey) {
        fieldDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Field field = snapshot.getValue(Field.class);

                if (field != null) {
//                    // Display the field details in the UI
                    mainField = field;
                    binding.textFieldAddress.setText("Address: " + field.getAddress());
                    binding.textFieldDescription.setText("Description: " + field.getDescription());
                    // Optionally display points or tasks if needed
                    Toast.makeText(FieldDetailsActivity.this, field.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FieldDetailsActivity.this, "Field not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to retrieve field: " + error.getMessage());
                Toast.makeText(FieldDetailsActivity.this, "Failed to retrieve field", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    // Define the request code as a class constant
    private static final int PICK_IMAGE_REQUEST = 1;

    // Override onActivityResult to handle the selected image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected image
            Uri imageUri = data.getData();

            try {
                // Convert URI to Bitmap and set it to the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                binding.imageField.setImageBitmap(bitmap);

                // Optional: You can also store the URI or bitmap for further use
                // selectedImageUri = imageUri;

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
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