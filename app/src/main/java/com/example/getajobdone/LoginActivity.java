package com.example.getajobdone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getajobdone.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    String emailPattern = "[a-zA-Z0-9]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    String isActive, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        progressDialog = new ProgressDialog(this);

        binding.txtSignUpCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CustomerRegistrationActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        binding.txtSignUpSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SPRegistrationActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        binding.radioCustomer.setOnCheckedChangeListener((compoundButton, b) -> {
            boolean checked = binding.radioCustomer.isChecked();
            if (checked){
                binding.radioCustomer.setChecked(true);
                binding.radioSP.setChecked(false);

                binding.customerLogin.setVisibility(View.VISIBLE);
                binding.SPLogin.setVisibility(View.GONE);
                binding.adminLogin.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Customer Button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        binding.radioSP.setOnCheckedChangeListener((compoundButton, b) -> {
            boolean checked = binding.radioSP.isChecked();
            if (checked){
                binding.radioSP.setChecked(true);
                binding.radioCustomer.setChecked(false);

                binding.SPLogin.setVisibility(View.VISIBLE);
                binding.customerLogin.setVisibility(View.GONE);
                binding.adminLogin.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Service Provider Button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.radioSP.setChecked(false);
                binding.radioCustomer.setChecked(false);

                binding.SPLogin.setVisibility(View.GONE);
                binding.customerLogin.setVisibility(View.GONE);
                binding.adminLogin.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Admin Login", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSPLogin.setOnClickListener(v -> {

            String SPEmail = binding.edSPEmail.getText().toString();
            String SPPassword = binding.edSPPassword.getText().toString();
            String SP_ID = binding.edSPID.getText().toString();

            if (!SPEmail.matches(emailPattern) || SPEmail.isEmpty()) {
                binding.edSPEmail.setError("Enter correct email");
            } else if (SP_ID.isEmpty()) {
                binding.edSPID.setError("Enter your licence number");
            } else if (SPPassword.isEmpty() || SPPassword.length() < 8) {
                binding.edSPPassword.setError("Enter the proper password");
            } else {
                progressDialog.setTitle("Login...");
                progressDialog.setMessage("Please wait while your login is getting done.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                auth.signInWithEmailAndPassword(SPEmail, SPPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customers").child(Objects.requireNonNull(auth.getUid()));
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                isActive = snapshot.child("active").getValue(String.class);
                                userType = snapshot.child("userType").getValue(String.class);

                                if (isActive.equals("NOT ACTIVE")){
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Account Deactivated");
                                    builder.setMessage("Your account has been deactivated by the admin. \nPlease contact to admin to activate your account");
                                    builder.setPositiveButton("Yes", (dialog, which) -> {
                                        dialog.cancel();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(LoginActivity.this, SplashScreenActivity.class));
                                        finishAffinity();
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                } else if(isActive.equals("ACTIVE")){
                                    if (userType.equals("ServiceProvider")){
                                        Intent intent = new Intent(LoginActivity.this, SPDashboard.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        Toast.makeText(LoginActivity.this, "Service Provider Login Successful.", Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(LoginActivity.this, "This is not service provider account.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onComplete: Login Failed. " + task.getException());
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.d("TAG", "onFailure: " + e.getMessage());
                });
            }
        });

        binding.btnCustomerLogin.setOnClickListener(v -> {

            String customerEmail = binding.edEmail.getText().toString();
            String customerPassword = binding.edPassword.getText().toString();

            if (!customerEmail.matches(emailPattern) || customerEmail.isEmpty()) {
                binding.edEmail.setError("Enter correct email");
            } else if (customerPassword.isEmpty() || customerPassword.length() < 8) {
                binding.edPassword.setError("Enter the proper password");
            } else {
                progressDialog.setTitle("Login...");
                progressDialog.setMessage("Please wait while your login is getting done.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                auth.signInWithEmailAndPassword(customerEmail, customerPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customers").child(Objects.requireNonNull(auth.getUid()));
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                isActive = snapshot.child("active").getValue(String.class);
                                userType = snapshot.child("userType").getValue(String.class);

                                Log.d("TAG", "onDataChange: userType:: " + userType);
                                Log.d("TAG", "onDataChange: active:: " + isActive);

                                if (isActive.equals("NOT ACTIVE")){
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Account Deactivated");
                                    builder.setMessage("Your account has been deactivated by the admin. \nPlease contact to admin to activate your account");
                                    builder.setPositiveButton("Yes", (dialog, which) -> {
                                        dialog.cancel();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(LoginActivity.this, SplashScreenActivity.class));
                                        finishAffinity();
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                } else if(isActive.equals("ACTIVE")){
                                    Log.d("TAG", "onDataChange: userType:: " + userType);
                                    if (userType.equals("Customer")){
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        Toast.makeText(LoginActivity.this, "Customer Login Successful.", Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(LoginActivity.this, "This is not customer account.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.d("TAG", "onFailure: " + e.getMessage());
                });
            }
        });

        binding.btnAdminLogin.setOnClickListener(view -> {
            String adminEmail = binding.edAdminEmail.getText().toString();
            String adminPassword = binding.edAdminPassword.getText().toString();

            if (!adminEmail.matches(emailPattern) || adminEmail.isEmpty()) {
                binding.edEmail.setError("Enter correct email");
            } else if (adminPassword.isEmpty() || adminPassword.length() < 8) {
                binding.edPassword.setError("Enter the proper password");
            } else {
                progressDialog.setTitle("Login...");
                progressDialog.setMessage("Please wait while your login is getting done.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                auth.signInWithEmailAndPassword(adminEmail, adminPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, AdminDashboard.class);
                        startActivity(intent);
                        finishAffinity();
                        Toast.makeText(LoginActivity.this, "Admin Login Successful.", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.d("TAG", "onFailure: " + e.getMessage());
                });
            }
        });
    }
}