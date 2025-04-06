package com.example.getajobdone;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getajobdone.adpater.ProductOrderAdapter;
import com.example.getajobdone.databinding.ActivityMyProductOrdersBinding;
import com.example.getajobdone.databinding.ActivityProductOrdersBinding;
import com.example.getajobdone.model.ProductOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyProductOrders extends AppCompatActivity {

    ActivityMyProductOrdersBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final List<ProductOrder> orderModelList = new ArrayList<>();
    ProductOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProductOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        binding.btnBack.setOnClickListener(view -> finish());

        binding.RvMyProductOrders.setLayoutManager(new LinearLayoutManager(this));

        reference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderModelList.clear();
                for (DataSnapshot orders : snapshot.getChildren()) {
                    ProductOrder order = orders.getValue(ProductOrder.class);
                    if (order != null && order.getSpId().equals(auth.getUid())) {
                        orderModelList.add(order);
                    }
                }
                adapter = new ProductOrderAdapter(orderModelList, MyProductOrders.this);
                binding.RvMyProductOrders.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SPOrdersActivity", "Database error: " + error.getMessage());
            }
        });
    }
}