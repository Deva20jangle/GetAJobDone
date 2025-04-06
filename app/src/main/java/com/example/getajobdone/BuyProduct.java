package com.example.getajobdone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.getajobdone.databinding.ActivityBuyProductBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BuyProduct extends AppCompatActivity {

    ActivityBuyProductBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String productId, productName, productPrice, productImage, spId, spName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuyProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        productName = intent.getStringExtra("productName");
        productPrice = intent.getStringExtra("productPrice");
        productImage = intent.getStringExtra("productImage");
        spId = intent.getStringExtra("spId");
        spName = intent.getStringExtra("spName");

        binding.txtProductName.setText(productName);
        binding.txtProductPrice.setText(productPrice);
        Glide.with(this).load(productImage).into(binding.imgProduct);

        binding.btnOrderProduct.setOnClickListener(view -> {
            String customerAddress = binding.edCustomerAddress.getText().toString();
            if (customerAddress.isEmpty()) {
                Toast.makeText(BuyProduct.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                return;
            }

            String timestamp = "" + System.currentTimeMillis();
            DatabaseReference ref = database.getReference("ProductOrders");

            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("orderId", timestamp);
            hashmap.put("customerUid", auth.getUid());
            hashmap.put("productId", productId);
            hashmap.put("address", customerAddress);
            hashmap.put("productName", productName);
            hashmap.put("productPrice", productPrice);
            hashmap.put("productImage", productImage);
            hashmap.put("spId", spId);
            hashmap.put("spName", spName);
            hashmap.put("orderStatus", "New");

            ref.child(timestamp).setValue(hashmap).addOnCompleteListener(task -> {
                Toast.makeText(BuyProduct.this, "Product ordered successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(BuyProduct.this, "Order failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }
}