package com.example.greenpulse.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenpulse.Disease;
import com.example.greenpulse.GeminiHelper;
import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.adapters.ImageAdapter;
import com.example.greenpulse.adapters.NewsAdapter;
import com.example.greenpulse.apiInterfaces.NewsApiUtil;
import com.example.greenpulse.databinding.ActivityDiseaseBinding;
import com.example.greenpulse.responses.ImageResponse;
import com.example.greenpulse.responses.NewsResponse;

import java.util.List;

public class DiseaseActivity extends OtherActivity {

    GeminiHelper gm;
    ActivityDiseaseBinding binding;
    Disease disease;
    String crop;
    NewsApiUtil newsApiUtil;
    List<ImageResponse.ImagesResult>imageList;
    ImageAdapter imageAdapter;
    List<NewsResponse.NewsResult>newsList;
    NewsAdapter newsAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiseaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String diseaseNameS = intent.getStringExtra("disease");
        String cropS = intent.getStringExtra("crop");
        disease = new Disease();
        disease.name = diseaseNameS;
        binding.diseaseName.setText(disease.name);
        crop = cropS;
        gm = new GeminiHelper();
        newsApiUtil = new NewsApiUtil(DiseaseActivity.this);

        setSupportActionBar(binding.toolbarDisease.getRoot());
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setTitle("Disease Analysis Result");

        getHelpFromGemini(binding.diseaseDesc,descriptionPrompt(disease.name));
        binding.loadImage.setIndeterminate(true);
        getImages(disease.name);
        getHelpFromGemini(binding.diseaseSymptomsTv,symptomPrompt(disease.name,crop));
        getHelpFromGemini(binding.diseasePotentialTv,potentialThreatPrompt(disease.name,crop));
        getHelpFromGemini(binding.diseasePreventionTv,preventionPrompt(disease.name,crop));
        getHelpFromGemini(binding.diseaseTreatmentTv,treatmentPrompt(disease.name,crop));
        getHelpFromGemini(binding.diseasePostTv,postManagementPrompt(disease.name,crop));
        getArticles(disease.name);
        binding.loadNews.setIndeterminate(true);



    }

    private String descriptionPrompt(String disease){
        String prompt = "Give me a precise description of the disease named "+disease+". Write everyhting" +
                "in one paragraph within 100 words. No more no less.";
        return prompt;
    }
    private String symptomPrompt(String disease, String crop){
        String prompt = "What are the symptoms of "+disease+" in a "+crop+" give each symptom using" +
                " bullet numbers (like 1,2,3,...). Nothing more nothing less.";
        return prompt;
    }
    private String potentialThreatPrompt(String disease, String crop){
        String prompt = "What are the potential threats of "+disease+" in "+crop+"s.";
        return prompt;
    }
    private String preventionPrompt(String disease, String crop){
        String prompt = "What are the prevention methods for "+disease+" in "+crop+"s?";
        return prompt;
    }
    private String treatmentPrompt(String disease, String crop)
    {
        String prompt = "What are the treatment methods for "+disease+" in "+crop+"s?";
        return prompt;
    }
    private String postManagementPrompt(String disease, String crop)
    {
        String prompt = "If in a crop some "+crop+"s have been affected by "+disease+", then " +
                "what are some post disease management strategies that one should follow to avoid" +
                "further attack from that disease";
        return prompt;
    }
    private void getHelpFromGemini(TextView tv, String prompt) {
        int id = tv.getId();
        gm.callGemini(prompt, new GeminiHelper.GeminiCallback() {
            @Override
            public void onSuccess(String result, int tag) {
                runOnUiThread(()->{
                    tv.setText(result);
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getImages(String query)
    {
        binding.loadImage.setVisibility(View.VISIBLE);
        newsApiUtil.getResources(query, NewsApiUtil.IMAGES,
                new NewsApiUtil.NewsCallBack2<ImageResponse.ImagesResult>(
                ) {
            @Override
            public void onSuccess(List<ImageResponse.ImagesResult>resourceList)
            {
                runOnUiThread(()->{
                    binding.loadImage.setVisibility(View.INVISIBLE);
                    imageAdapter = new ImageAdapter(DiseaseActivity.this,resourceList);
                    binding.imageViewPager.setAdapter(imageAdapter);
                });

            }

            @Override
            public void onError(String errorMessage) {
                binding.loadImage.setVisibility(View.INVISIBLE);
                Toast.makeText(DiseaseActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getArticles(String query)
    {
        //binding.loadNews.setVisibility(View.VISIBLE);
        newsApiUtil.getResources(query, NewsApiUtil.NEWS,
                new NewsApiUtil.NewsCallBack2<NewsResponse.NewsResult>() {
            @Override
            public void onSuccess(List<NewsResponse.NewsResult> resourceList) {
                runOnUiThread(()->{
                    binding.loadImage.setVisibility(View.INVISIBLE);
                    newsAdapter = new NewsAdapter(DiseaseActivity.this,resourceList);
                    binding.articlesRecycler.setAdapter(newsAdapter);
                    binding.articlesRecycler.setLayoutManager(new LinearLayoutManager(DiseaseActivity.this,
                            LinearLayoutManager.HORIZONTAL,false));
                });

            }

            @Override
            public void onError(String errorMessage) {
                binding.loadImage.setVisibility(View.INVISIBLE);
                Toast.makeText(DiseaseActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}