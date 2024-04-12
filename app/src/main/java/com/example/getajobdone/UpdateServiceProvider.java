package com.example.getajobdone;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.getajobdone.databinding.ActivityUpdateCustomerBinding;
import com.example.getajobdone.databinding.ActivityUpdateServiceProviderBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class UpdateServiceProvider extends AppCompatActivity {

    ActivityUpdateServiceProviderBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;

    String name, email, spID, businessName, contactNo, address, password;
    String updatedName, updatedEmail, updatedId, updatedBusinessName, updatedContact, updatedAddress, updatedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateServiceProviderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        if (auth.getCurrentUser() != null){

            DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Customers").child(Objects.requireNonNull(auth.getUid()));
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    name = snapshot.child("name").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                    spID = snapshot.child("spID").getValue(String.class);
                    businessName = snapshot.child("businessName").getValue(String.class);
                    contactNo = snapshot.child("contactNo").getValue(String.class);
                    address = snapshot.child("address").getValue(String.class);
                    password = snapshot.child("password").getValue(String.class);

                    binding.edName.setText(name);
                    binding.edEmail.setText(email);
                    binding.edContactNo.setText(contactNo);
                    binding.edAddress.setText(address);
                    binding.edPassword.setText(password);
                    binding.edID.setText(spID);
                    binding.edBusinessName.setText(businessName);

                    binding.btnUpdateSP.setOnClickListener(view -> {
                        updatedName = binding.edName.getText().toString();
                        updatedEmail = binding.edEmail.getText().toString();
                        updatedContact = binding.edContactNo.getText().toString();
                        updatedAddress = binding.edPassword.getText().toString();
                        updatedPassword = binding.edPassword.getText().toString();
                        updatedId = binding.edID.getText().toString();
                        updatedBusinessName = binding.edBusinessName.getText().toString();

                        HashMap<String, String> hashmap = new HashMap<>();
                        hashmap.put("userId", auth.getUid());
                        hashmap.put("userType", "ServiceProvider");
                        hashmap.put("name", updatedName);
                        hashmap.put("email", updatedEmail);
                        hashmap.put("contactNo", updatedContact);
                        hashmap.put("address", updatedAddress);
                        hashmap.put("password", updatedPassword);
                        hashmap.put("businessName", updatedBusinessName);
                        hashmap.put("spID", updatedId);

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().setValue(hashmap);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}