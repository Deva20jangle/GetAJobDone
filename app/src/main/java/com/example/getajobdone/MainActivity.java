package com.example.getajobdone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getajobdone.adpater.complaintAdapter;
import com.example.getajobdone.adpater.serviceAdapter;
import com.example.getajobdone.databinding.ActivityMainBinding;
import com.example.getajobdone.model.complaintModel;
import com.example.getajobdone.model.serviceModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ActionBarDrawerToggle toggle;
    serviceAdapter adapter;

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final List<serviceModel> serviceModelList = new ArrayList<>();

    List<String> activeSP = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        toggle=new ActionBarDrawerToggle(MainActivity.this,binding.drawer,binding.toolbar,R.string.open,R.string.close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();

        binding.customerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.ivShowFilter.setOnClickListener(view -> {
            binding.filterCard.setVisibility(View.VISIBLE);
            binding.ivShowFilter.setVisibility(View.GONE);
            binding.ivHideFilter.setVisibility(View.VISIBLE);
        });

        binding.ivHideFilter.setOnClickListener(view -> {
            binding.filterCard.setVisibility(View.GONE);
            binding.ivShowFilter.setVisibility(View.VISIBLE);
            binding.ivHideFilter.setVisibility(View.GONE);
        });

        binding.txtCostLH.setOnClickListener(view -> {
            binding.filterCard.setVisibility(View.GONE);
            Collections.sort(serviceModelList, (s1, s2) -> s1.getServicePrice().compareToIgnoreCase(s2.getServicePrice()));
            adapter.notifyDataSetChanged();
        });

        binding.txtCostHL.setOnClickListener(view -> {
            binding.filterCard.setVisibility(View.GONE);
            Collections.sort(serviceModelList, (s1, s2) -> s1.getServicePrice().compareToIgnoreCase(s2.getServicePrice()));
            Collections.reverse(serviceModelList);
            adapter.notifyDataSetChanged();
        });

        binding.txtRatingLH.setOnClickListener(view -> {
            binding.filterCard.setVisibility(View.GONE);
            Collections.sort(serviceModelList, (s1, s2) -> s1.getServiceRating().compareToIgnoreCase(s2.getServiceRating()));
            adapter.notifyDataSetChanged();
        });

        binding.txtReset.setOnClickListener(view -> {
            binding.filterCard.setVisibility(View.GONE);
            Collections.sort(serviceModelList, (s1, s2) -> s1.getServiceRating().compareToIgnoreCase(s2.getServiceRating()));
            Collections.reverse(serviceModelList);
            adapter.notifyDataSetChanged();
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot users : snapshot.child("Customers").getChildren()){
                    String userID = users.child("userId").getValue(String.class);
                    String isActive = users.child("active").getValue(String.class);
                    if (isActive.equals("ACTIVE")){
                        activeSP.add(userID);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceModelList.clear();
                for(DataSnapshot services : snapshot.child("Services").getChildren()){
                    final String serviceID = services.child("serviceId").getValue(String.class);
                    final String spUid = services.child("spUid").getValue(String.class);
                    final String spName = services.child("spName").getValue(String.class);
                    final String businessAddress = services.child("address").getValue(String.class);
                    final String businessContactNo = services.child("contactNo").getValue(String.class);
                    final String businessName = services.child("businessName").getValue(String.class);
                    final String serviceType = services.child("serviceType").getValue(String.class);
                    final String servicePrice = services.child("servicePrice").getValue(String.class);
                    final String serviceDescription = services.child("serviceDesc").getValue(String.class);
                    final String serviceRating = services.child("serviceRating").getValue(String.class);
                    final String serviceRatingSum = services.child("serviceRatingSum").getValue(String.class);
                    final String serviceRatingCount = services.child("serviceRatingCount").getValue(String.class);
                    final String bankAccountHolderName = services.child("bankAccountHolderName").getValue(String.class);
                    final String bankAccountNumber = services.child("bankAccountNumber").getValue(String.class);
                    final String bankIFSCCode = services.child("bankIFSCCode").getValue(String.class);
                    final String bankUPI = services.child("bankUPI").getValue(String.class);

                    if (activeSP.contains(spUid)){
                        serviceModel list = new serviceModel(serviceID, spUid, spName, businessAddress, businessContactNo, businessName, serviceType, servicePrice, serviceDescription, serviceRating, serviceRatingSum, serviceRatingCount, bankAccountHolderName, bankAccountNumber, bankIFSCCode, bankUPI);
                        serviceModelList.add(list);
                        Collections.sort(serviceModelList, (s1, s2) -> s1.getServiceRating().compareToIgnoreCase(s2.getServiceRating()));
                        Collections.reverse(serviceModelList);
                    }
                }
                adapter = new serviceAdapter(serviceModelList, getBaseContext());
                binding.customerRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.svSearchServices.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        binding.navMenu.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.orderedServices){
                Toast.makeText(MainActivity.this, "Ordered Services", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, MyOrders.class);
                startActivity(i);
                binding.drawer.closeDrawer(GravityCompat.START);
            } else if(itemId == R.id.fileComplaint){
                Intent i = new Intent(MainActivity.this, SubmitComplaint.class);
                startActivity(i);
                binding.drawer.closeDrawer(GravityCompat.START);
            } else if(itemId == R.id.settingsCustomer){
                Intent i = new Intent(MainActivity.this, UpdateCustomer.class);
                startActivity(i);
                binding.drawer.closeDrawer(GravityCompat.START);
            } else if(itemId == R.id.logoutCustomer){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Do you want to logout?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        dialog.cancel();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
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