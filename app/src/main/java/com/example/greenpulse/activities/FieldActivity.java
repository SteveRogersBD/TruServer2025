package com.example.greenpulse.activities;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.greenpulse.R;
import com.example.greenpulse.adapters.FieldAdapter;
import com.example.greenpulse.databinding.ActivityFieldBinding;
import com.example.greenpulse.models.Field;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FieldActivity extends AppCompatActivity {

    ActivityFieldBinding binding;
    FieldAdapter adapter;
    ArrayList<Field> fieldList;
    private DatabaseReference fieldDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFieldBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpBar();
        // Handle window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the field list and adapter
        fieldList = new ArrayList<>();
        adapter = new FieldAdapter(this, fieldList);

        // Set up the RecyclerView
        binding.fieldRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.fieldRecycler.setAdapter(adapter);

        // Initialize Firebase reference
        fieldDb = FirebaseDatabase.getInstance().getReference().child("fields");

        // Retrieve fields from Firebase
        retrieveFields();
    }

    private void retrieveFields() {
        fieldDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fieldList.clear(); // Clear existing list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Field field = dataSnapshot.getValue(Field.class);
                    if (field != null) {
                        fieldList.add(field);
                    }
                }
                // Update the adapter with the new list
                adapter.updateFields(fieldList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to retrieve fields: " + error.getMessage());
                Toast.makeText(FieldActivity.this,
                        "Failed to retrieve fields", Toast.LENGTH_SHORT).show();
            }
        });
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