package com.example.getajobdone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.getajobdone.databinding.ActivitySubmitComplaintBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SubmitComplaint extends AppCompatActivity {

    ActivitySubmitComplaintBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubmitComplaintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        String timestamp = "" + System.currentTimeMillis();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Complaints");

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        binding.btnSubmitComplaint.setOnClickListener(view -> {
            progressDialog.show();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("complaintId", "" + timestamp);
            hashMap.put("customerName", "" + binding.edCustomerName.getText().toString());
            hashMap.put("customerPhone", "" + binding.edCustomerPhone.getText().toString());
            hashMap.put("SPName", "" + binding.edSPName.getText().toString());
            hashMap.put("SPPhone", "" + binding.edSpPhone.getText().toString());
            hashMap.put("Complaint", "" + binding.edComplaint.getText().toString());
            hashMap.put("ResolvedDesc", "");
            hashMap.put("Resolved", "NO");

            ref.child(timestamp).setValue(hashMap).addOnSuccessListener(unused -> {
                Toast.makeText(SubmitComplaint.this, "Complaint Registered Successfully.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent intent = new Intent(SubmitComplaint.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(SubmitComplaint.this, "Failed to register a complaint. Please contact to admin to register your complaint..", Toast.LENGTH_SHORT).show();
            });

        });

    }
}