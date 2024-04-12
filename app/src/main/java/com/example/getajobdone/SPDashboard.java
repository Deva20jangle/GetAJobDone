package com.example.getajobdone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getajobdone.adpater.ordersAdapter;
import com.example.getajobdone.adpater.serviceAdapter;
import com.example.getajobdone.databinding.ActivitySpdashboardBinding;
import com.example.getajobdone.model.orderModel;
import com.example.getajobdone.model.serviceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SPDashboard extends AppCompatActivity {

    ActivitySpdashboardBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ActionBarDrawerToggle toggle;

    ordersAdapter adapter;
    String serviceId, serviceType, servicePrice, serviceDesc, businessName, SPContactNo, spUid, address, spName, orderId, customerUid, customerName, customerContact, orderStatus, orderDesc, date, orderStatusDesc;
    String [] dropDown = {"Select Status", "New", "In Progress", "Rejected", "Completed"};
    ArrayAdapter<String> adapterItems;

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final List<orderModel> orderModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpdashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        toggle=new ActionBarDrawerToggle(SPDashboard.this,binding.drawer,binding.toolbar,R.string.open,R.string.close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();

        binding.spRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterItems = new ArrayAdapter<String>(this, R.layout.drop_down_list, dropDown);
        binding.searchDropDownSPDashboard.setAdapter(adapterItems);

        binding.searchDropDownSPDashboard.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            binding.svSortData.setQuery(item, false);
        });

        binding.svSortData.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("TAG", "performFiltering: keyword:: MainActivity " + s);
                adapter.getFilter().filter(s);
                return false;
            }
        });

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

                    Log.d("TAG", "onBindViewHolder: SPD auth.getUID:: " + auth.getUid());
                    Log.d("TAG", "onBindViewHolder: SPD spUid:: " + spUid);

                    orderModel list = new orderModel();
                    if (auth.getUid().equals(spUid)){
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
                    if (list.getSpName() != null) {
                        orderModelList.add(list);
                    }
                }
                adapter = new ordersAdapter(orderModelList, getBaseContext());
                binding.spRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.navMenu.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.addService){
                Intent i = new Intent(SPDashboard.this, AddService.class);
                startActivity(i);
                binding.drawer.closeDrawer(GravityCompat.START);
            } else if(itemId == R.id.settingsSP){
                Intent i = new Intent(SPDashboard.this, UpdateServiceProvider.class);
                startActivity(i);
                binding.drawer.closeDrawer(GravityCompat.START);
            } else if(itemId == R.id.logoutSP){
                final AlertDialog.Builder builder = new AlertDialog.Builder(SPDashboard.this);
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(SPDashboard.this, LoginActivity.class));
                    finishAffinity();
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                binding.drawer.closeDrawer(GravityCompat.START);
            }
            return true;
        });
    }
}