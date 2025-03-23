package com.example.greenpulse.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.adapters.ChatAdapter;
import com.example.greenpulse.databinding.ActivityChatBotBinding;
import com.example.greenpulse.models.ChatModel;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatBotActivity extends OtherActivity {

    ChatAdapter chatAdapter;
    List<ChatModel> messageList;
    ActivityChatBotBinding binding;
    Toolbar toolbar;
    private String geminiKey = "AIzaSyDC1PG_TxAl0dUaSW_PMSnv6zv-MAWrhNA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GradientDrawable gradientDrawable = (GradientDrawable) getResources().
                getDrawable(R.drawable.grad_3);
        int startColor = gradientDrawable.getColors()[0];
        int endColor = gradientDrawable.getColors()[1];
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(endColor);
        //int bottomColor = Color.parseColor("#153E50");
        window.setNavigationBarColor(startColor);
        toolbar = binding.chatToolbar;
        toolbar.setBackgroundColor(endColor);
        toolbar.setTitle("AgriBot");
        toolbar.setTitleTextColor(getColor(R.color.white));


        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(ChatBotActivity.this,messageList);
        binding.mainRecycler.setAdapter(chatAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(ChatBotActivity.this);
        binding.mainRecycler.setLayoutManager(lm);


        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.textEt.getText().equals(""))
                {
                    String query = binding.textEt.getText().toString();
                    ChatModel newMessage = new ChatModel(query,ChatModel.SENT_BY_ME);
                    messageList.add(newMessage);
                    chatAdapter.notifyDataSetChanged();
                    binding.mainRecycler.smoothScrollToPosition(messageList.size());
                    binding.textEt.setText("");
                    callGemini(query);
                }
            }
        });
    }
    private void callGemini(String query){
        String apiKey = geminiKey;
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash",apiKey);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content =
                new Content.Builder().addText(query).build();

// For illustrative purposes only. You should use an executor that fits your needs.
        Executor executor = Executors.newSingleThreadExecutor();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //textView.setText(result.getText());
                                ChatModel replyMessage = new ChatModel(result.getText(),ChatModel.SENT_BY_BOT);
                                messageList.add(replyMessage);
                                chatAdapter.notifyDataSetChanged();
                                binding.mainRecycler.smoothScrollToPosition(messageList.size());

                            }
                        });
                    }


                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                },
                executor);
    }
}