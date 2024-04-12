package com.example.getajobdone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getajobdone.adpater.ordersAdapter;
import com.example.getajobdone.databinding.ActivityMyOrdersBinding;
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

public class MyOrders extends AppCompatActivity {

    ActivityMyOrdersBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final List<orderModel> orderModelList = new ArrayList<>();
    ordersAdapter adapter;
    String serviceId, serviceType, servicePrice, serviceDesc, businessName, SPContactNo, spUid, address, spName, orderId, customerUid, customerName, customerContact, orderStatus, orderDesc, date, orderStatusDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        binding.btnBack.setOnClickListener(view -> {
           finish();
        });

        binding.RvMyOrders.setLayoutManager(new LinearLayoutManager(this));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderModelList.clear();
                for(DataSnapshot orders : snapshot.child("Orders").getChildren()){

                    serviceId = orders.child("serviceId").getValue(String.class);
                    serviceType = orders.child("serviceType").getValue(String.class);
                    servicePrice = orders.child("servicePrice").getValue(String.class);
                    serviceDesc = orders.child("serviceDesc").getValue(String.class);
                    businessName = orders.child("businessName").getValue(String.class);
                    SPContactNo = orders.child("SPContactNo").getValue(String.class);
                    spUid = orders.child("spUid").getValue(String.class);
                    address = orders.child("address").getValue(String.class);
                    spName = orders.child("spName").getValue(String.class);
                    orderId = orders.child("orderId").getValue(String.class);
                    customerUid = orders.child("customerUid").getValue(String.class);
                    customerName = orders.child("customerName").getValue(String.class);
                    customerContact = orders.child("customerContact").getValue(String.class);
                    orderStatus = orders.child("orderStatus").getValue(String.class);
                    orderDesc = orders.child("orderDesc").getValue(String.class);
                    date = orders.child("date").getValue(String.class);
                    orderStatusDesc = orders.child("orderStatusDesc").getValue(String.class);

                    Log.d("TAG", "onBindViewHolder: MyOrders auth.getUID:: " + auth.getUid());
                    Log.d("TAG", "onBindViewHolder: MyOrders customerUid:: " + customerUid);

                    orderModel list = new orderModel();
                    if (auth.getUid().equals(customerUid)){
                        list.setServiceId(serviceId);
                        list.setServiceType(serviceType);
                        list.setServicePrice(servicePrice);
                        list.setServiceDesc(serviceDesc);
                        list.setBusinessName(businessName);
                        list.setSPContactNo(SPContactNo);
                        list.setSpUid(spUid);
                        list.setAddress(address);
                        list.setSpName(spName);
                        list.setOrderId(orderId);
                        list.setCustomerUid(customerUid);
                        list.setCustomerName(customerName);
                        list.setCustomerContact(customerContact);
                        list.setOrderStatus(orderStatus);
                        list.setOrderDesc(orderDesc);
                        list.setDate(date);
                        list.setOrderStatusDesc(orderStatusDesc);
                    }
                    Log.d("TAG", "onBindViewHolder: list:: " + list);
                    if (list.getCustomerUid() != null) {
                        orderModelList.add(list);
                    }
                }
                adapter = new ordersAdapter(orderModelList, getBaseContext());
                binding.RvMyOrders.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}