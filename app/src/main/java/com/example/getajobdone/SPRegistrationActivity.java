package com.example.getajobdone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.getajobdone.databinding.ActivitySpregistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SPRegistrationActivity extends AppCompatActivity {

    ActivitySpregistrationBinding binding;
    String emailPattern = "[a-zA-Z0-9]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;

    DatabaseReference ref1 =FirebaseDatabase.getInstance().getReference();
    List<String> serviceProviderIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpregistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SPRegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        binding.txtHaveAcc.setOnClickListener(v -> {
            Intent intent = new Intent(SPRegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        String id = binding.edID.getText().toString();
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot users : snapshot.child("Customers").getChildren()){
                    String dbSpId = users.child("spID").getValue(String.class);
                    serviceProviderIds.add(dbSpId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            if (serviceProviderIds.contains(binding.edID.getText().toString())){
                binding.edID.setError("This service provider id is already in use");
            } else {
                performAuth();
            }
        });

    }
    private void performAuth() {

        String name = binding.edName.getText().toString();
        String email = binding.edEmail.getText().toString();
        String contact = binding.edContactNo.getText().toString();
        String address = binding.edAddress.getText().toString();
        String password = binding.edPassword.getText().toString();
        String cPassword = binding.edConfirmPassword.getText().toString();
        String businessName = binding.edBusinessName.getText().toString();
        String spID = binding.edID.getText().toString();
        String aadhaarNo = binding.edAadhaar.getText().toString();

        if (!email.matches(emailPattern) || email.isEmpty()){
            binding.edEmail.setError("Enter correct email");
        } else if(name.isEmpty()){
            binding.edName.setError("Enter your name");
        } else if(contact.isEmpty()){
            binding.edContactNo.setError("Enter your contact number");
        } else if(address.isEmpty()){
            binding.edAddress.setError("Enter your address");
        } else  if(businessName.isEmpty()){
            binding.edAddress.setError("Enter your business name");
        } else if(spID.isEmpty()){
            binding.edAddress.setError("Enter your ID");
        } else if (password.isEmpty() || password.length() < 8){
            binding.edPassword.setError("Enter the proper password. Password should be more than 8 characters");
        } else if (!password.equals(cPassword)){
            binding.edConfirmPassword.setError("Password doesn't match");
        } else if (aadhaarNo.isEmpty()){
            binding.edAadhaar.setError("PLease enter your aadhaar number");
        } else {
            progressDialog.setTitle("Registration...");
            progressDialog.setMessage("Please wait while your registration is getting done.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    progressDialog.dismiss();

                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put("userId", auth.getUid());
                    hashmap.put("userType", "ServiceProvider");
                    hashmap.put("name", name);
                    hashmap.put("email", email);
                    hashmap.put("contactNo", contact);
                    hashmap.put("address", address);
                    hashmap.put("password", password);
                    hashmap.put("businessName", businessName);
                    hashmap.put("spID", spID);
                    hashmap.put("active", "ACTIVE");

                    DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Customers");
                    ref.child(auth.getUid()).setValue(hashmap).addOnCompleteListener(task1 -> {
                        Intent intent = new Intent(SPRegistrationActivity.this, SPDashboard.class);
                        startActivity(intent);
                        finishAffinity();
                        Toast.makeText(SPRegistrationActivity.this, "Customer Registered Successfully.", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Log.d("TAG", "onFailure: "+e.getMessage());
                        Toast.makeText(SPRegistrationActivity.this, "Registration Failed."+e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SPRegistrationActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Log.d("TAG", "onFailure: "+e.getMessage());
            });
        }
    }
}