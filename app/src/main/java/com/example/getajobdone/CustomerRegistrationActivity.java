package com.example.getajobdone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getajobdone.databinding.ActivityCustomerRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CustomerRegistrationActivity extends AppCompatActivity {

    ActivityCustomerRegistrationBinding binding;
    String emailPattern = "[a-zA-Z0-9]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerRegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        binding.txtHaveAcc.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerRegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        binding.btnRegister.setOnClickListener(v -> performAuth());
        
    }
    private void performAuth() {

        String name = binding.edName.getText().toString();
        String email = binding.edEmail.getText().toString();
        String contact = binding.edContactNo.getText().toString();
        String address = binding.edAddress.getText().toString();
        String password = binding.edPassword.getText().toString();
        String cPassword = binding.edConfirmPassword.getText().toString();


        if (!email.matches(emailPattern) || email.isEmpty()){
            binding.edEmail.setError("Enter correct email");
        } else if(name.isEmpty()){
            binding.edName.setError("Enter your name");
        } else if(contact.isEmpty()){
            binding.edContactNo.setError("Enter your contact number");
        } else if(address.isEmpty()){
            binding.edAddress.setError("Enter your address");
        } else if (password.isEmpty() || password.length() < 8){
            binding.edPassword.setError("Enter the proper password. Password should be more than 8 characters");
        } else if (!password.equals(cPassword)){
            binding.edConfirmPassword.setError("Password doesn't match");
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
                    hashmap.put("userType", "Customer");
                    hashmap.put("name", name);
                    hashmap.put("email", email);
                    hashmap.put("contactNo", contact);
                    hashmap.put("address", address);
                    hashmap.put("password", password);
                    hashmap.put("businessName", "");
                    hashmap.put("spID", "");
                    hashmap.put("active", "ACTIVE");

                    //String userEmail = email;

                    DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Customers");
                    ref.child(auth.getUid()).setValue(hashmap).addOnCompleteListener(task1 -> {
                        Intent intent = new Intent(CustomerRegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                        Toast.makeText(CustomerRegistrationActivity.this, "Customer Registered Successfully.", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Log.d("TAG", "onFailure: "+e.getMessage());
                        Toast.makeText(CustomerRegistrationActivity.this, "Registration Failed."+e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(CustomerRegistrationActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Log.d("TAG", "onFailure: "+e.getMessage());
                }
            });
        }
    }
}