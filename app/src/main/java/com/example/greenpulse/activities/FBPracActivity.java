package com.example.greenpulse.activities;

import androidx.annotation.NonNull;

import android.os.Bundle;

import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.databinding.ActivityFbpracBinding;
import com.example.greenpulse.models.Field;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FBPracActivity extends OtherActivity {


    ActivityFbpracBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFbpracBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//        Task task = new Task("Irrigation","Finish it by evening",false, new Date());
//        List<Task>tasks= new ArrayList<>();
//        tasks.add(task);
//        ArrayList<MyLatLng> latLngList = new ArrayList<>();
//        latLngList.add(new MyLatLng(37.7749, -122.4194)); // San Francisco
//        latLngList.add(new MyLatLng(34.0522, -118.2437)); // Los Angeles
//        latLngList.add(new MyLatLng(40.7128, -74.0060));  // New York
//        Field field = new Field("A field","For tomato",latLngList,tasks);
//        db.child("fields").child(field.title).setValue(field).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(FBPracActivity.this, "Entry Done!!!", Toast.LENGTH_SHORT).show();
//            }
//        });

        List<String>names = new ArrayList<>();
        db.child("fields").child("A field").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Field field = dataSnapshot.getValue(Field.class);
                    names.add(field.title);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public interface FieldCallBack{
        public void onSuccess(List<String>list);
    }
}