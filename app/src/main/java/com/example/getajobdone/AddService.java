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

import com.example.getajobdone.databinding.ActivityAddServiceBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class AddService extends AppCompatActivity {

    ActivityAddServiceBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    String businessName, contactNo, address, spName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        String timestamp = "" + System.currentTimeMillis();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Services");
        DatabaseReference ref1 =FirebaseDatabase.getInstance().getReference("Customers").child(Objects.requireNonNull(auth.getUid()));

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        binding.btnAddService.setOnClickListener(view -> {
            if (binding.edServiceType.getText().toString().isEmpty()){
                binding.edServiceType.setError("Please enter service type");
            } else if(binding.edServicePrice.getText().toString().isEmpty()){
                binding.edServicePrice.setError("Please enter service price");
            } else if(binding.edDescription.getText().toString().isEmpty()){
                binding.edDescription.setError("Please enter service description");
            } else if(binding.edBankAccountHolderName.getText().toString().isEmpty()){
                binding.edBankAccountHolderName.setError("Please enter account holder name");
            } else if(binding.edBankAccountNumber.getText().toString().isEmpty()){
                binding.edBankAccountNumber.setError("Please enter bank account number");
            } else if(binding.edIFSCCode.getText().toString().isEmpty()){
                binding.edIFSCCode.setError("Please enter bank IFSC code");
            } else if(binding.edBankUPI.getText().toString().isEmpty()){
                binding.edBankUPI.setError("Please enter bank UPI ID");
            } else {
                progressDialog.show();
                ref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        businessName = snapshot.child("businessName").getValue(String.class);
                        contactNo = snapshot.child("contactNo").getValue(String.class);
                        spName = snapshot.child("name").getValue(String.class);
                        address = snapshot.child("address").getValue(String.class);

                        Log.d("TAG", "onCreate: data from db for add service:: businessName: " + businessName);
                        Log.d("TAG", "onCreate: data from db for add service:: contactNo: " + contactNo);
                        Log.d("TAG", "onCreate: data from db for add service:: spName: " + spName);
                        Log.d("TAG", "onCreate: data from db for add service:: address: " + address);

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("serviceId", "" + timestamp);
                        hashMap.put("serviceType", "" + binding.edServiceType.getText().toString());
                        hashMap.put("servicePrice", "" + binding.edServicePrice.getText().toString());
                        hashMap.put("serviceDesc", "" + binding.edDescription.getText().toString());
                        hashMap.put("businessName", businessName);
                        hashMap.put("contactNo", contactNo);
                        hashMap.put("spName", spName);
                        hashMap.put("spUid", auth.getUid());
                        hashMap.put("address", address);
                        hashMap.put("serviceRating", "0");
                        hashMap.put("serviceRatingSum", "0");
                        hashMap.put("serviceRatingCount", "0");
                        hashMap.put("bankAccountHolderName", "" + binding.edBankAccountHolderName.getText().toString());
                        hashMap.put("bankAccountNumber", "" + binding.edBankAccountNumber.getText().toString());
                        hashMap.put("bankIFSCCode", "" + binding.edIFSCCode.getText().toString());
                        hashMap.put("bankUPI", "" + binding.edBankUPI.getText().toString());

                        ref.child(timestamp).setValue(hashMap).addOnSuccessListener(unused -> {
                            Toast.makeText(AddService.this, "Service added Successfully.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(AddService.this, SPDashboard.class);
                            startActivity(intent);
                            finishAffinity();
                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(AddService.this, "Failed to add a service. Please contact to admin to register your complaint..", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}