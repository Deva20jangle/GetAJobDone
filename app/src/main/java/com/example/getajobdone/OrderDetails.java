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

import com.example.getajobdone.databinding.ActivityOrderDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderDetails extends AppCompatActivity {

    ActivityOrderDetailsBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    String orderId, customerName, customerContact, customerUid, serviceId, address, serviceType, businessName, spUid, servicePrice, serviceDesc, SPContactNo, spName, orderStatus, orderDesc, date, orderStatusDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        customerName = intent.getStringExtra("customerName");
        customerContact = intent.getStringExtra("customerContact");
        customerUid = intent.getStringExtra("customerUid");
        serviceId = intent.getStringExtra("serviceId");
        address = intent.getStringExtra("address");
        serviceType = intent.getStringExtra("serviceType");
        businessName = intent.getStringExtra("businessName");
        spUid = intent.getStringExtra("spUid");
        servicePrice = intent.getStringExtra("servicePrice");
        serviceDesc = intent.getStringExtra("serviceDesc");
        SPContactNo = intent.getStringExtra("SPContactNo");
        spName = intent.getStringExtra("spName");
        orderStatus = intent.getStringExtra("orderStatus");
        orderDesc = intent.getStringExtra("orderDesc");
        date = intent.getStringExtra("date");
        orderStatusDesc = intent.getStringExtra("orderStatusDesc");

        binding.txtOrderIDOrderDetails.setText(orderId);
        binding.txtCustomerNameOrderDetails.setText(customerName);
        binding.txtOrderDateOrderDetails.setText(date);
        binding.txtCustomerContactNoOrderDetails.setText(customerContact);
        binding.txtCustomerAddressOrderDetails.setText(address);
        binding.txtServiceTypeOrderDetails.setText(serviceType);
        binding.txtOrderStatusOrderDetails.setText(orderStatus);
        binding.txtOrderDescOrderDetails.setText(orderDesc);
        binding.txtSPNameOrderDetails.setText(spName);
        binding.txtBusinessNameOrderDetails.setText(businessName);

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Orders");
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Services");

        if (orderStatus.equals("New") && !customerUid.equals(auth.getUid())){
            binding.edOrderStatusDesc.setVisibility(View.VISIBLE);
            binding.lnAcceptReject.setVisibility(View.VISIBLE);
            binding.lnComplete.setVisibility(View.GONE);
        } else if (orderStatus.equals("In Progress") && !customerUid.equals(auth.getUid())){
            binding.edOrderStatusDesc.setVisibility(View.GONE);
            binding.lnAcceptReject.setVisibility(View.GONE);
            binding.lnComplete.setVisibility(View.VISIBLE);
        } else if((orderStatus.equals("Rejected")  && !customerUid.equals(auth.getUid()))
                || (orderStatus.equals("Completed") && !customerUid.equals(auth.getUid()))){
            binding.edOrderStatusDesc.setVisibility(View.GONE);
            binding.lnAcceptReject.setVisibility(View.GONE);
            binding.lnComplete.setVisibility(View.GONE);
        } else if(orderStatus.equals("Completed") && customerUid.equals(auth.getUid())){
            binding.edOrderStatusDesc.setVisibility(View.GONE);
            binding.lnAcceptReject.setVisibility(View.GONE);
            binding.lnComplete.setVisibility(View.GONE);
            binding.btnRateUs.setVisibility(View.VISIBLE);
            binding.lnBankDetails.setVisibility(View.VISIBLE);

            ref1.child(serviceId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String bankAccountHolderName = snapshot.child("bankAccountHolderName").getValue(String.class);
                    String bankAccountNumber = snapshot.child("bankAccountNumber").getValue(String.class);
                    String bankIFSCCode = snapshot.child("bankIFSCCode").getValue(String.class);
                    String bankUPI = snapshot.child("bankUPI").getValue(String.class);

                    binding.txtBankAccountHolderName.setText(bankAccountHolderName);
                    binding.txtBankAccountNumber.setText(bankAccountNumber);
                    binding.txtBankIFSCCode.setText(bankIFSCCode);
                    binding.txtBankUPI.setText(bankUPI);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } else if((orderStatus.equals("Rejected")  && customerUid.equals(auth.getUid()))
                || (orderStatus.equals("Completed") && customerUid.equals(auth.getUid()))){
            binding.edOrderStatusDesc.setVisibility(View.GONE);
            binding.lnAcceptReject.setVisibility(View.GONE);
            binding.lnComplete.setVisibility(View.GONE);
            binding.btnRateUs.setVisibility(View.VISIBLE);
        }

        binding.btnRateUs.setOnClickListener(view -> {
            Intent in = new Intent(OrderDetails.this, RateUs.class);
            in.putExtra("orderId", orderId);
            in.putExtra("serviceId", serviceId);
            startActivity(in);
        });

        binding.btnAcceptOrder.setOnClickListener(view -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetails.this);
            builder.setTitle("Accept Order");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dialog.cancel();

                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("orderId", orderId);
                hashmap.put("customerName", customerName);
                hashmap.put("customerContact", customerContact);
                hashmap.put("customerUid", customerUid);
                hashmap.put("serviceId", serviceId);
                hashmap.put("address", address);
                hashmap.put("serviceType", serviceType);
                hashmap.put("businessName", businessName);
                hashmap.put("spUid", spUid);
                hashmap.put("servicePrice", servicePrice);
                hashmap.put("serviceDesc", serviceDesc);
                hashmap.put("SPContactNo", SPContactNo);
                hashmap.put("spName", spName);
                hashmap.put("orderStatus", "In Progress");
                hashmap.put("orderDesc", orderDesc);
                hashmap.put("orderStatusDesc", "");
                hashmap.put("date", date);

                ref.child(orderId).setValue(hashmap).addOnCompleteListener(task1 -> {
                    Intent in = new Intent(OrderDetails.this, SPDashboard.class);
                    startActivity(in);
                    finishAffinity();
                    Toast.makeText(OrderDetails.this, "Order Accepted", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.d("TAG", "onFailure: "+e.getMessage());
                    Toast.makeText(OrderDetails.this, "Failed to accept order. Please try again later", Toast.LENGTH_SHORT).show();
                });
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        binding.btnRejectOrder.setOnClickListener(view -> {
            if (binding.edOrderStatusDesc.getText().toString().isEmpty()){
                binding.edOrderStatusDesc.setError("PLease provide the reason to reject the order");
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetails.this);
                builder.setTitle("Reject Order");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();

                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put("orderId", orderId);
                    hashmap.put("customerName", customerName);
                    hashmap.put("customerContact", customerContact);
                    hashmap.put("customerUid", customerUid);
                    hashmap.put("serviceId", serviceId);
                    hashmap.put("address", address);
                    hashmap.put("serviceType", serviceType);
                    hashmap.put("businessName", businessName);
                    hashmap.put("spUid", spUid);
                    hashmap.put("servicePrice", servicePrice);
                    hashmap.put("serviceDesc", serviceDesc);
                    hashmap.put("SPContactNo", SPContactNo);
                    hashmap.put("spName", spName);
                    hashmap.put("orderStatus", "Rejected");
                    hashmap.put("orderDesc", orderDesc);
                    hashmap.put("orderStatusDesc", binding.edOrderStatusDesc.getText().toString());
                    hashmap.put("date", date);

                    ref.child(orderId).setValue(hashmap).addOnCompleteListener(task1 -> {
                        Intent in = new Intent(OrderDetails.this, SPDashboard.class);
                        startActivity(in);
                        finishAffinity();
                        Toast.makeText(OrderDetails.this, "Order Rejected", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Log.d("TAG", "onFailure: "+e.getMessage());
                        Toast.makeText(OrderDetails.this, "Failed to reject order. Please try again later", Toast.LENGTH_SHORT).show();
                    });
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        binding.btnCompleteOrder.setOnClickListener(view -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetails.this);
            builder.setTitle("Reject Order");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dialog.cancel();

                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("orderId", orderId);
                hashmap.put("customerName", customerName);
                hashmap.put("customerContact", customerContact);
                hashmap.put("customerUid", customerUid);
                hashmap.put("serviceId", serviceId);
                hashmap.put("address", address);
                hashmap.put("serviceType", serviceType);
                hashmap.put("businessName", businessName);
                hashmap.put("spUid", spUid);
                hashmap.put("servicePrice", servicePrice);
                hashmap.put("serviceDesc", serviceDesc);
                hashmap.put("SPContactNo", SPContactNo);
                hashmap.put("spName", spName);
                hashmap.put("orderStatus", "Completed");
                hashmap.put("orderDesc", orderDesc);
                hashmap.put("orderStatusDesc", binding.edOrderStatusDesc.getText().toString());
                hashmap.put("date", date);

                ref.child(orderId).setValue(hashmap).addOnCompleteListener(task1 -> {
                    Intent in = new Intent(OrderDetails.this, SPDashboard.class);
                    startActivity(in);
                    finishAffinity();
                    Toast.makeText(OrderDetails.this, "Order Rejected", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.d("TAG", "onFailure: "+e.getMessage());
                    Toast.makeText(OrderDetails.this, "Failed to reject order. Please try again later", Toast.LENGTH_SHORT).show();
                });
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

    }
}