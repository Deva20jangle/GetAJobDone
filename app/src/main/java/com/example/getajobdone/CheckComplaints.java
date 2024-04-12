package com.example.getajobdone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getajobdone.adpater.complaintAdapter;
import com.example.getajobdone.adpater.serviceAdapter;
import com.example.getajobdone.databinding.ActivityCheckComplaintsBinding;
import com.example.getajobdone.model.complaintModel;
import com.example.getajobdone.model.serviceModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CheckComplaints extends AppCompatActivity {

    ActivityCheckComplaintsBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    complaintAdapter complaintAdapter;
    String bName;

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final List<complaintModel> complaintModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckComplaintsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        binding.btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(CheckComplaints.this, AdminDashboard.class);
            startActivity(intent);
            finishAffinity();
        });

        binding.checkComplaintsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complaintModelList.clear();
                for(DataSnapshot complaints : snapshot.child("Complaints").getChildren()){
                    final String complaintId = complaints.child("complaintId").getValue().toString();
                    final String customerName = complaints.child("customerName").getValue().toString();
                    final String customerPhone = complaints.child("customerPhone").getValue().toString();
                    final String SPName = complaints.child("SPName").getValue().toString();
                    final String SPPhone = complaints.child("SPPhone").getValue().toString();
                    final String Complaint = complaints.child("Complaint").getValue().toString();
                    final String resolved = complaints.child("Resolved").getValue().toString();
                    final String resolvedDesc = null;
//                    resolvedDesc = complaints.child("resolvedDesc").getValue().toString();

                    complaintModel list = new complaintModel(complaintId, customerName, customerPhone, SPName, SPPhone, Complaint, resolved, resolvedDesc);
                    if (resolved.equals("NO")){
                        complaintModelList.add(list);
                    }
                }
                binding.checkComplaintsRecyclerView.setAdapter(new complaintAdapter(complaintModelList, CheckComplaints.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}