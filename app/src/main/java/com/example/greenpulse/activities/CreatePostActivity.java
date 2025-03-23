package com.example.greenpulse.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.greenpulse.MainActivity;
import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.RetrofitInstance;
import com.example.greenpulse.apiInterfaces.GPApi;
import com.example.greenpulse.databinding.ActivityCreatePostBinding;
import com.example.greenpulse.responses.PostResponse;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends OtherActivity {

    private static final String TAG = "CreatePostActivity";
    private static final int REQUEST_STORAGE_PERMISSION = 1000;
    private ActivityCreatePostBinding binding;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            binding.pickerCreatePost.setImageURI(selectedImageUri);
                            binding.pickerCreatePost.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    }
                });

        binding.pickerCreatePost.setOnClickListener(v -> {
            Log.d(TAG, "Image picker clicked");
            askPermission();
        });

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
                    Toast.makeText(this, "Post Created Successfully!!!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
                } catch (IOException e) {
                    Toast.makeText(this, "Error creating post: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please select an image!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void askPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d(TAG, "Permission state: " + permissionCheck + " (GRANTED=" + PackageManager.PERMISSION_GRANTED + ")");

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission not granted, requesting...");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.d(TAG, "Showing permission rationale");
                Toast.makeText(this, "Storage permission is needed to select images", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            Log.d(TAG, "Permission already granted, calling image picker");
            callImagePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            Log.d(TAG, "Permission result received, grantResults length: " + grantResults.length);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted in result");
                callImagePicker();
            } else {
                Log.d(TAG, "Permission denied in result");
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Log.d(TAG, "Permission permanently denied");
                    Toast.makeText(this, "Permission denied. Please enable it in app settings.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void callImagePicker() {
        Log.d(TAG, "Launching image picker");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void createPost(String title, String description, Uri imageUri) throws IOException {
        GPApi gpApi = RetrofitInstance.gpApi();
        File file = new File(getRealPathFromURI(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);

        Call<PostResponse> call = gpApi.createPost(1L, titlePart, descriptionPart, body);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreatePostActivity.this, "Post created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreatePostActivity.this, "Error creating post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(CreatePostActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        } else {
            return uri.getPath();
        }
    }
}