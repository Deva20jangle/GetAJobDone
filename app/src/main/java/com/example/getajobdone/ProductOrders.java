package com.example.getajobdone;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getajobdone.adpater.ProductOrderAdapter;
import com.example.getajobdone.adpater.ordersAdapter;
import com.example.getajobdone.databinding.ActivityProductOrdersBinding;
import com.example.getajobdone.model.ProductOrder;
import com.example.getajobdone.model.orderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductOrders extends AppCompatActivity {

    ActivityProductOrdersBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final List<ProductOrder> orderModelList = new ArrayList<>();
    ProductOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        binding.btnBack.setOnClickListener(view -> finish());

        binding.RvSpOrders.setLayoutManager(new LinearLayoutManager(this));

        reference.child("ProductOrders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderModelList.clear();
                for (DataSnapshot orders : snapshot.getChildren()) {
                    ProductOrder order = orders.getValue(ProductOrder.class);
                    if (order != null && order.getSpId().equals(auth.getUid())) {
                        orderModelList.add(order);
                    }
                }
                adapter = new ProductOrderAdapter(orderModelList, ProductOrders.this);
                binding.RvSpOrders.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SPOrdersActivity", "Database error: " + error.getMessage());
            }
        });
    }
}