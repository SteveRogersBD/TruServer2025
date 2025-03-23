package com.example.greenpulse.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.greenpulse.MainActivity;
import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.databinding.ActivityCreatePostBinding;
import com.example.greenpulse.responses.PostResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends OtherActivity {

    private static final String TAG = "CreatePostActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private ActivityCreatePostBinding binding;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Image Picker
        binding.pickerCreatePost.setOnClickListener(v -> {
            Log.d(TAG, "Image picker clicked");
            openImagePicker();
        });

        // Create Post
        binding.askBtn.setOnClickListener(v -> {
            String des = binding.descriptionEditText.getText().toString();
            String title = binding.titleEditText.getText().toString();

            if (des.isEmpty()) {
                binding.descriptionEditText.setError("Enter a valid description!");
                return;
            }
            if (title.isEmpty()) {
                binding.titleEditText.setError("Enter a valid title!");
                return;
            }

            if (selectedImageUri != null) {
                try {
                    createPost(title, des, selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error creating post: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please select an image!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Open image picker
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Handle image picker result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                binding.pickerCreatePost.setImageURI(selectedImageUri);
                binding.pickerCreatePost.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Log.d(TAG, "Image selected: " + selectedImageUri.toString());
            }
        }
    }

    /**
     * Upload the image and create the post
     */
    private void createPost(String title, String description, Uri imageUri) throws IOException {
        GPApi gpApi = RetrofitInstance.gpApi();

        // Convert Uri to File
        File imageFile = uriToFile(imageUri);
        if (imageFile == null) {
            Toast.makeText(this, "Failed to get image file", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Multipart
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);

        // API Call
        Call<PostResponse> call = gpApi.createPost(1L, titlePart, descriptionPart, body);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreatePostActivity.this, "Post created successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(CreatePostActivity.this, "Error creating post: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(CreatePostActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Convert Uri to File for Multipart upload
     */
    private File uriToFile(Uri uri) throws IOException {
        File file = new File(getCacheDir(), "upload_image.jpg");
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {

            if (inputStream != null) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                Log.d(TAG, "File created successfully: " + file.getAbsolutePath());
            } else {
                Log.e(TAG, "Failed to create file from Uri");
                return null;
            }
        }
        return file;
    }
}
