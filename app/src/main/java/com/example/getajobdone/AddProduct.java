package com.example.getajobdone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getajobdone.databinding.ActivityAddProductBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class AddProduct extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    ActivityAddProductBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        binding.btnSelectImage.setOnClickListener(view -> openFileChooser());

        binding.btnAddProduct.setOnClickListener(view -> {
            if (binding.edProductName.getText().toString().isEmpty()) {
                binding.edProductName.setError("Please enter product name");
            } else if (binding.edProductPrice.getText().toString().isEmpty()) {
                binding.edProductPrice.setError("Please enter product price");
            } else if (imageUri == null) {
                Toast.makeText(AddProduct.this, "Please select an image", Toast.LENGTH_SHORT).show();
            } else {
                uploadProduct();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            binding.imgProduct.setImageURI(imageUri);
        }
    }

    private void uploadProduct() {
        progressDialog.setMessage("Adding Product...");
        progressDialog.show();

        String timestamp = "" + System.currentTimeMillis();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProductImages").child(timestamp);

        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            saveProductInfo(timestamp, imageUrl);
        })).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(AddProduct.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveProductInfo(String productId, String imageUrl) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Customers").child(Objects.requireNonNull(auth.getUid()));

        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String spName = snapshot.child("name").getValue(String.class);
                String spId = auth.getUid();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("productId", productId);
                hashMap.put("productName", binding.edProductName.getText().toString());
                hashMap.put("productPrice", binding.edProductPrice.getText().toString());
                hashMap.put("productImage", imageUrl);
                hashMap.put("spId", spId);
                hashMap.put("spName", spName);

                ref.child(productId).setValue(hashMap).addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(AddProduct.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(AddProduct.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }
}