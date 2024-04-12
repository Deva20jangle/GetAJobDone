package com.example.getajobdone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.getajobdone.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    String type, isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Thread thread = new Thread()
        {
            public void run(){
                try
                {
                    sleep(100);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally
                {
                    if(auth.getCurrentUser() != null){
                        auth.getCurrentUser().reload();
                    }
                    if (auth.getCurrentUser() != null){
                        if(Objects.equals(auth.getUid(), "8T8b82TXv3MLk4F7S8wJXMXaZHu1")){
                            Intent intent = new Intent(SplashScreenActivity.this , AdminDashboard.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customers").child(Objects.requireNonNull(auth.getUid()));
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    type = snapshot.child("userType").getValue(String.class);
                                    isActive = snapshot.child("active").getValue(String.class);
                                    Log.d("TAG", "onCreate: UserType: in event   " + type);

                                    if (isActive.equals("NOT ACTIVE")){
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
                                        builder.setTitle("Account Deactivated");
                                        builder.setMessage("Your account has been deactivated by the admin. \nPlease contact to admin to activate your account");
                                        builder.setPositiveButton("Yes", (dialog, which) -> {
                                            dialog.cancel();
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                                            finishAffinity();
                                        });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    } else if(isActive.equals("ACTIVE")){
                                        if(type.equals("ServiceProvider")){
                                            Intent intent = new Intent(SplashScreenActivity.this , SPDashboard.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        } else{
                                            Intent intent = new Intent(SplashScreenActivity.this , MainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this , StartActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
            }
        };thread.start();
    }
}