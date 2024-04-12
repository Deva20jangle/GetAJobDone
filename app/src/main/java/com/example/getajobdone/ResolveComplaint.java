package com.example.getajobdone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.getajobdone.databinding.ActivityResolveComplaintBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class ResolveComplaint extends AppCompatActivity {

    ActivityResolveComplaintBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    String complaintId, customerName, customerPhone, SPName, SPPhone, complaint, resolvedDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResolveComplaintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        complaintId = intent.getStringExtra("complaintId");
        customerName = intent.getStringExtra("customerName");
        customerPhone = intent.getStringExtra("customerPhone");
        SPName = intent.getStringExtra("SPName");
        SPPhone = intent.getStringExtra("SPPhone");
        complaint = intent.getStringExtra("Complaint");
        resolvedDesc = intent.getStringExtra("resolvedDesc");

        binding.txtComplaintID.setText(complaintId);
        binding.txtComplaintCustmerName.setText(customerName);
        binding.txtComplaintCustomerContactNo.setText(customerPhone);
        binding.txtComplaintSPName.setText(SPName);
        binding.txtComplaintSPContactNo.setText(SPPhone);
        binding.txtComplaintDesc.setText(complaint);

        binding.btnBack.setOnClickListener(view -> finish());

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Complaints");

        binding.btnMarkResolvedComplaint.setOnClickListener(view -> {

            final AlertDialog.Builder builder = new AlertDialog.Builder(ResolveComplaint.this);
            builder.setMessage("Resolve complaint");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dialog.cancel();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("complaintId", "" + complaintId);
                hashMap.put("customerName", "" + customerName);
                hashMap.put("customerPhone", "" + customerPhone);
                hashMap.put("SPName", "" + SPName);
                hashMap.put("SPPhone", "" + SPPhone);
                hashMap.put("Complaint", "" + complaint);
                hashMap.put("resolvedDesc", "" + binding.edComplaintResolvedDesc.getText().toString());
                hashMap.put("Resolved", "YES");

                ref.child(complaintId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().setValue(hashMap);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        });
    }
}