package com.example.greenpulse.activities;


import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.greenpulse.GeminiHelper;
import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.databinding.ActivityAnalyzeBinding;
import com.example.greenpulse.models.DiseasedCrop;


public class AnalyzeActivity extends OtherActivity {

    ActivityAnalyzeBinding binding;
    GeminiHelper gm;
    private final int REQUEST_STORAGE_PERMISSION = 1000;
    private final int REQUEST_IMAGE_PICK = 2000;
    DiseasedCrop diseasedCrop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnalyzeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gm = new GeminiHelper();

        dealWithTheSpinners();
        binding.picker.setOnClickListener(onClickListener);
        binding.analyzeButton.setOnClickListener(onClickListener);

    }

    private void dealWithTheSpinners()
    {
        String[] cropTypes = {
                "Fruits", "Vegetables", "Legumes", "Grains", "Lentils",
                "Oilseeds", "Tubers", "Spices", "Herbs", "Nuts",
                "Cereals", "Pulses", "Cash Crops", "Beverage Crops",
                "Fiber Crops", "Forage Crops", "Medicinal Crops", "Aquatic Crops"
        };

        String[] growthStages = {
                "Seedling", "Vegetative", "Bud Formation", "Flowering",
                "Pollination", "Fruit Set", "Maturity", "Harvest"
        };

        ArrayAdapter<String>typeAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                cropTypes);
        typeAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.typeSpinner.setAdapter(typeAdapter);

        ArrayAdapter<String>growthAdapter = new ArrayAdapter<>(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                growthStages);
        growthAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.growthSpinner.setAdapter(growthAdapter);


        binding.typeSpinner.setOnItemSelectedListener(itemSelectedListener);
        binding.growthSpinner.setOnItemSelectedListener(itemSelectedListener);



    }
    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int parentId = parent.getId();
            String selectedItem = parent.getSelectedItem().toString();
            if(parentId==binding.typeSpinner.getId())
            {
                binding.typeTv.setText(selectedItem);
                binding.typeTv.setTextColor(getColor(R.color.black));
            } else if (parentId==binding.growthSpinner.getId()) {
                binding.growthTv.setText(selectedItem);
                binding.growthTv.setTextColor(getColor(R.color.black));
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==binding.picker.getId())
            {
                handleImageViewClick();
            }

            else if(v.getId()==binding.analyzeButton.getId())
            {
                createTheCrop();
                String prompt = createPrompt(diseasedCrop);
                binding.picker.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(binding.picker.getDrawingCache());
                if(bitmap==null)
                    Toast.makeText(AnalyzeActivity.this, "Pick an image please!!!", Toast.LENGTH_SHORT).show();
                gm.callGeminiWithImage(prompt, bitmap, new GeminiHelper.GeminiCallback() {
                    @Override
                    public void onSuccess(String result, int tag) {
                        updateByGemini(result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        updateByGemini(t.getLocalizedMessage());
                    }
                });


            }
        }
    };

    private void updateByGemini(String result) {
        runOnUiThread(()->{
            //Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AnalyzeActivity.this,DiseaseActivity.class);
            intent.putExtra("disease",result);
            intent.putExtra("crop",diseasedCrop.getName());
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        });
    }

    private void createTheCrop() {
        diseasedCrop = new DiseasedCrop();
        String name = binding.materialEditTextName.getText().toString();
        String type = binding.typeTv.getText().toString();
        String stage = binding.growthTv.getText().toString();
        String symptoms = binding.symptomET.getText().toString();
        String note = binding.addNoteET.getText().toString();
        diseasedCrop.setName(name);
        diseasedCrop.setType(type);
        diseasedCrop.setGrowthStage(stage);
        diseasedCrop.setSymptoms(symptoms);
        diseasedCrop.setExtraInfo(note);

    }


    private void handleImageViewClick() {
        askPermission();
    }

    private void askPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
        else{
            callImagePicker();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callImagePicker();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void callImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                binding.picker.setImageURI(selectedImageUri);
                binding.picker.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }
        }
    }

    private String createPrompt(DiseasedCrop diseasedCrop)
    {
        String prompt = "You are an intelligent assistant specialized in agricultural crop disease diagnosis. " +
                "I need you to identify the disease affecting a crop based on the following details: " +
                "Crop Name: \"" + diseasedCrop.getDiseaseName() + "\", " +
                "Type: \"" + diseasedCrop.getType() + "\", " +
                "Growth Stage: \"" + diseasedCrop.getGrowthStage() + "\", " +
                "Disease Name (if known): \"" + diseasedCrop.getDiseaseName() + "\", " +
                "Symptoms: \"" + diseasedCrop.getSymptoms() + "\", " +
                "Additional Information: \"" + diseasedCrop.getExtraInfo() + "\", " +
                "Location: \"" + diseasedCrop.getLocation() + "\", " +
                "Weather Information: \"" + diseasedCrop.getWeatherInfo() + "\". " +
                "The photo contains visual details of the affected crop. Please analyze both the textual" +
                " details and the photo, " + "and analyze the disease. Give me just the name within 2/3" +
                "words. Nothing more, nothing less.";
        return prompt;
    }

}