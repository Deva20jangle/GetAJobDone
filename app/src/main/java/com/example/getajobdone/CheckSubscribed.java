package com.example.getajobdone;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getajobdone.adpater.complaintAdapter;
import com.example.getajobdone.adpater.subscribeAdapter;
import com.example.getajobdone.databinding.ActivityCheckSubscribedBinding;
import com.example.getajobdone.model.complaintModel;
import com.example.getajobdone.model.subscribeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CheckSubscribed extends AppCompatActivity {

    ActivityCheckSubscribedBinding binding;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final List<subscribeModel> subscribeModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckSubscribedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(CheckSubscribed.this, AdminDashboard.class);
            startActivity(intent);
            finishAffinity();
        });

        binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subscribeModelList.clear();
                for(DataSnapshot complaints : snapshot.child("Subscriptions").getChildren()){
                    final String spUid = complaints.child("userId").getValue().toString();
                    final String businessName = complaints.child("businessName").getValue().toString();
                    final String active = complaints.child("active").getValue().toString();
                    final String email = complaints.child("email").getValue().toString();

                    subscribeModel list = new subscribeModel(spUid, businessName, active, email);
                    subscribeModelList.add(list);
                }
                binding.rvUsers.setAdapter(new subscribeAdapter(subscribeModelList, CheckSubscribed.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}