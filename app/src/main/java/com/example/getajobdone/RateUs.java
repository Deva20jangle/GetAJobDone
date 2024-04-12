package com.example.getajobdone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.getajobdone.databinding.ActivityRateUsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;

public class RateUs extends AppCompatActivity {

    ActivityRateUsBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    String serviceID, spUid, spName, businessAddress, businessContactNo, businessName, serviceType, servicePrice, serviceDescription, serviceRating, serviceRatingSum, serviceRatingCount, orderId, serviceDesc, contactNo, address;
    float intServiceRating, intServiceRatingSum;
    int intServiceRatingCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRateUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        serviceID = intent.getStringExtra("serviceId");

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Services");
        DatabaseReference ref1 =FirebaseDatabase.getInstance().getReference("Orders");

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        ref.child(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                spUid = snapshot.child("spUid").getValue(String.class);
                spName = snapshot.child("spName").getValue(String.class);
                businessAddress = snapshot.child("businessAddress").getValue(String.class);
                businessContactNo = snapshot.child("businessContactNo").getValue(String.class);
                businessName = snapshot.child("businessName").getValue(String.class);
                serviceType = snapshot.child("serviceType").getValue(String.class);
                servicePrice = snapshot.child("servicePrice").getValue(String.class);
                serviceDescription = snapshot.child("serviceDescription").getValue(String.class);
                serviceRating = snapshot.child("serviceRating").getValue(String.class);
                serviceRatingSum = snapshot.child("serviceRatingSum").getValue(String.class);
                serviceRatingCount = snapshot.child("serviceRatingCount").getValue(String.class);
                serviceDesc = snapshot.child("serviceDesc").getValue(String.class);;
                contactNo = snapshot.child("contactNo").getValue(String.class);;
                address = snapshot.child("address").getValue(String.class);;

                //convert string to float as rating number will in float type
                intServiceRating = Float.parseFloat(serviceRating);
                intServiceRatingSum = Float.parseFloat(serviceRatingSum);
                intServiceRatingCount = Integer.parseInt(serviceRatingCount);

                //again converting rating to string to feed in database
                serviceRating = String.valueOf(intServiceRating);
                serviceRatingSum = String.valueOf(intServiceRatingSum);
                serviceRatingCount = String.valueOf(intServiceRatingCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnSubmit.setOnClickListener(view -> {

            if (String.valueOf(binding.ratingBar.getRating()).equals("0.0")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(RateUs.this);
                builder.setMessage("PLease select rating first");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                //calculate the rating
                String rating = String.valueOf(binding.ratingBar.getRating());
                intServiceRatingSum = intServiceRatingSum + Float.parseFloat(rating);
                intServiceRatingCount = intServiceRatingCount + 1;
                intServiceRating = (intServiceRatingSum/intServiceRatingCount);

                Log.d("TAG", "onCreate: RATE US:: intServiceRating:: " + intServiceRating);

                DecimalFormat df = new DecimalFormat("#.#");
                intServiceRating = Float.parseFloat(df.format(intServiceRating));

                Log.d("TAG", "onCreate: RATE US:: intServiceRating:: " + intServiceRating);

                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("serviceID", serviceID);
                hashmap.put("spUid", spUid);
                hashmap.put("spName", spName);
                hashmap.put("businessAddress", businessAddress);
                hashmap.put("businessContactNo", businessContactNo);
                hashmap.put("businessName", businessName);
                hashmap.put("serviceType", serviceType);
                hashmap.put("servicePrice", servicePrice);
                hashmap.put("serviceDescription", serviceDescription);
                hashmap.put("serviceRating", String.valueOf(intServiceRating));
                hashmap.put("serviceRatingSum", String.valueOf(intServiceRatingSum));
                hashmap.put("serviceRatingCount", String.valueOf(intServiceRatingCount));
                hashmap.put("serviceDesc", serviceDesc);
                hashmap.put("contactNo", contactNo);
                hashmap.put("address", address);


                ref.child(serviceID).setValue(hashmap).addOnSuccessListener(unused -> {
                    ref1.child(orderId).child("orderFeedback").setValue(binding.edFeedback.getText().toString()).addOnSuccessListener(unused1 -> {
                        Toast.makeText(RateUs.this, "Thank you for your time", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(RateUs.this, "Failed to submit feedback. Please try again later.", Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    Toast.makeText(RateUs.this, "Failed to submit feedback. Please try again later.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}