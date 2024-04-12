package com.example.getajobdone;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.getajobdone.databinding.ActivityServiceDetailsBinding;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class ServiceDetails extends AppCompatActivity {

    ActivityServiceDetailsBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    String serviceId, serviceType, servicePrice, serviceDesc, businessName, SPContactNo, spUid, businessAddress, spName, customerName, customerContact, customerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        serviceId = intent.getStringExtra("serviceId");
        serviceType = intent.getStringExtra("serviceType");
        servicePrice = intent.getStringExtra("servicePrice");
        serviceDesc = intent.getStringExtra("serviceDesc");
        businessName = intent.getStringExtra("businessName");
        SPContactNo = intent.getStringExtra("contactNo");
        spUid = intent.getStringExtra("spUid");
        businessAddress = intent.getStringExtra("address");
        spName = intent.getStringExtra("spName");

        binding.txtBusinessNameServiceDetails.setText(businessName);
        binding.tctServiceProviderNameServiceDetails.setText(spName);
        binding.txtSPContactNoServiceDetails.setText(SPContactNo);
        binding.txtAddressServiceDetails.setText(businessAddress);
        binding.txtServiceTypeServiceDetails.setText(serviceType);
        binding.txtServicePriceServiceDetails.setText(servicePrice);
        binding.txtServiceDescServiceDetails.setText(serviceDesc);

        binding.btnBack.setOnClickListener(view -> finish());

        String timestamp = "" + System.currentTimeMillis();
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Customers").child(Objects.requireNonNull(auth.getUid()));
        DatabaseReference ref1 =FirebaseDatabase.getInstance().getReference("Orders");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customerName = snapshot.child("name").getValue(String.class);
                customerContact = snapshot.child("contactNo").getValue(String.class);
                customerAddress = snapshot.child("address").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d("TAG", "onCreate: ServiceDetails:: " + customerName);
        Log.d("TAG", "onCreate: ServiceDetails:: " + customerContact);

        binding.btnHireService.setOnClickListener(view -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ServiceDetails.this);
            builder.setTitle("Are you Sure");
            builder.setMessage("Press Yes if you want to hire the service");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dialog.cancel();

                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("orderId", timestamp);
                hashmap.put("customerName", customerName);
                hashmap.put("customerContact", customerContact);
                hashmap.put("customerUid", auth.getUid());
                hashmap.put("serviceId", serviceId);
                hashmap.put("address", customerAddress);
                hashmap.put("serviceType", serviceType);
                hashmap.put("businessName", businessName);
                hashmap.put("spUid", spUid);
                hashmap.put("servicePrice", servicePrice);
                hashmap.put("serviceDesc", serviceDesc);
                hashmap.put("SPContactNo", SPContactNo);
                hashmap.put("spName", spName);
                hashmap.put("orderStatus", "New");
                hashmap.put("orderDesc", "" + binding.edOrderDescServiceDetails.getText().toString());
                hashmap.put("orderStatusDesc", "");
                hashmap.put("date", getCurrentDate());

                ref1.child(timestamp).setValue(hashmap).addOnCompleteListener(task1 -> {
                    Intent in = new Intent(ServiceDetails.this, MainActivity.class);
                    startActivity(in);
                    finish();
                    Toast.makeText(ServiceDetails.this, "Service ordered successfully.", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.d("TAG", "onFailure: "+e.getMessage());
                    Toast.makeText(ServiceDetails.this, "Service ordered failed."+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
                finishAffinity();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

    }

    public String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String currentTime = df.format(currentDateTime.getTime());

        return today + ", " + currentTime;
    }
}