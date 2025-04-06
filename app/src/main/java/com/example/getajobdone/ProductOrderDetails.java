package com.example.getajobdone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.getajobdone.databinding.ActivityProductOrderDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProductOrderDetails extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ActivityProductOrderDetailsBinding binding;
    String orderId, productId, address, productName, spUid, productPrice, orderStatus, productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ProductOrders");

        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        productId = intent.getStringExtra("productId");
        address = intent.getStringExtra("address");
        productName = intent.getStringExtra("productName");
        spUid = intent.getStringExtra("spUid");
        productPrice = intent.getStringExtra("productPrice");
        orderStatus = intent.getStringExtra("orderStatus");
        productImage = intent.getStringExtra("productImage");

        binding.txtOrderId.setText(orderId);
        binding.txtProductId.setText(productId);
        binding.txtProductName.setText(productName);
        binding.txtProductPrice.setText(productPrice);
        binding.txtCustomerAddress.setText(address);
        Glide.with(this).load(productImage).into(binding.imgProduct);

        binding.btnAccept.setOnClickListener(view -> {
            if (orderStatus != null && orderStatus.equals("Accepted")) {
                Toast.makeText(ProductOrderDetails.this, "Product already accepted.", Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("orderStatus", "Accepted");

            ref.child(orderId).child("orderStatus").setValue("Accepted").addOnSuccessListener(unused -> {
                Toast.makeText(ProductOrderDetails.this, "Product accepted.", Toast.LENGTH_SHORT).show();
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(ProductOrderDetails.this, "Failed to accept.", Toast.LENGTH_SHORT).show();
            });
        });

        binding.btnReject.setOnClickListener(view -> {
            String reason = binding.edReason.getText().toString();
            if (reason.isEmpty()) {
                binding.edReason.setError("Please enter reason for rejection");
            } else if(orderStatus != null && orderStatus.equals("Rejected")){
                Toast.makeText(ProductOrderDetails.this, "Product already accepted.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("orderStatus", "Rejected");

                ref.child(orderId).child("orderStatus").setValue("Rejected").addOnSuccessListener(unused -> {
                    Toast.makeText(ProductOrderDetails.this, "Product rejected.", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(ProductOrderDetails.this, "Failed to reject.", Toast.LENGTH_SHORT).show();
                });
            }
        });

    }
}