package com.example.getajobdone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getajobdone.adpater.serviceAdapter;
import com.example.getajobdone.adpater.userAdapter;
import com.example.getajobdone.databinding.ActivityAdminDashboardBinding;
import com.example.getajobdone.model.serviceModel;
import com.example.getajobdone.model.userModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends AppCompatActivity {

    ActivityAdminDashboardBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ActionBarDrawerToggle toggle;

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final List<userModel> userModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        toggle=new ActionBarDrawerToggle(AdminDashboard.this,binding.drawer,binding.toolbar,R.string.open,R.string.close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();

        binding.adminRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModelList.clear();
                for(DataSnapshot services : snapshot.child("Customers").getChildren()){

                    final String uID = services.child("userId").getValue(String.class);
                    final String userType = services.child("userType").getValue(String.class);
                    final String name = services.child("name").getValue(String.class);
                    final String email = services.child("email").getValue(String.class);
                    final String contactNo = services.child("contactNo").getValue(String.class);
                    final String address = services.child("address").getValue(String.class);
                    final String password = services.child("password").getValue(String.class);
                    final String active = services.child("active").getValue(String.class);
                    final String businessName = services.child("businessName").getValue(String.class);

                    Log.d("TAG", "onDataChange: user Status active:: " + active);
                    final String spID = services.child("spID").getValue(String.class);

                    userModel list = new userModel(uID, userType, name, email, contactNo, address, password, active, businessName, spID);
                    userModelList.add(list);
                }
                binding.adminRecyclerView.setAdapter(new userAdapter(userModelList, AdminDashboard.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.navMenu.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.checkComplaints){
                Intent i = new Intent(AdminDashboard.this, CheckComplaints.class);
                startActivity(i);
                binding.drawer.closeDrawer(GravityCompat.START);
            } else if(itemId == R.id.logoutAdmin){
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this);
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(AdminDashboard.this, LoginActivity.class));
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

//    @Override
//    protected void onStart() {
//        super.onStart();
//        userAdapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        userAdapter.stopListening();
//    }

}