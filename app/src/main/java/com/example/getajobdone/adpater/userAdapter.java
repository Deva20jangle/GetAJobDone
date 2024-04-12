package com.example.getajobdone.adpater;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getajobdone.R;
import com.example.getajobdone.model.userModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class userAdapter extends RecyclerView.Adapter<userAdapter.MyViewHolder> {

    private final List<userModel> userModelList;
    private final Context context;
    public userAdapter(List<userModel> userModelList, Context context) {
        this.userModelList = userModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public userAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new userAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, null));
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter.MyViewHolder holder, int position) {
        userModel userModel = userModelList.get(position);
        holder.txtUserNameRow.setText(userModel.getName());
        holder.txtUserEmailRow.setText(userModel.getEmail());

        Log.d("TAG", "onBindViewHolder: user Status active:: " + userModel.getActive());
        String isActive = userModel.getActive();

        if (isActive.equals("NOT ACTIVE")){
            holder.btnActivateUserRow.setVisibility(View.VISIBLE);
            holder.btnDeactivateUserRow.setVisibility(View.GONE);
        } else if(isActive.equals("ACTIVE")){
            holder.btnActivateUserRow.setVisibility(View.GONE);
            holder.btnDeactivateUserRow.setVisibility(View.VISIBLE);
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customers");

        holder.btnActivateUserRow.setOnClickListener(view -> {
            if (userModel.getUserType().equals("Customer")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure to activate the user");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();

                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put("userId", userModel.getuID());
                    hashmap.put("userType", "Customer");
                    hashmap.put("name", userModel.getName());
                    hashmap.put("email", userModel.getEmail());
                    hashmap.put("contactNo", userModel.getContactNo());
                    hashmap.put("address", userModel.getAddress());
                    hashmap.put("password", userModel.getPassword());
                    hashmap.put("businessName", "");
                    hashmap.put("spID", "");
                    hashmap.put("active", "ACTIVE");

                    ref.child(userModel.getuID()).setValue(hashmap).addOnSuccessListener(unused -> {
                        Toast.makeText(builder.getContext(), "User has been activated", Toast.LENGTH_SHORT).show();
                        holder.btnActivateUserRow.setVisibility(View.GONE);
                        holder.btnDeactivateUserRow.setVisibility(View.VISIBLE);
                    }).addOnFailureListener(e -> {
                        Toast.makeText(builder.getContext(), "Failed to deactivate user. Please try again later", Toast.LENGTH_SHORT).show();
                    });
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if(userModel.getUserType().equals("ServiceProvider")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure to deactivate the user");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();
                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put("userId", userModel.getuID());
                    hashmap.put("userType", "ServiceProvider");
                    hashmap.put("name", userModel.getName());
                    hashmap.put("email", userModel.getEmail());
                    hashmap.put("contactNo", userModel.getContactNo());
                    hashmap.put("address", userModel.getAddress());
                    hashmap.put("password", userModel.getPassword());
                    hashmap.put("businessName", userModel.getBusinessName());
                    hashmap.put("spID", userModel.getSpID());
                    hashmap.put("active", "ACTIVE");

                    Log.d("TAG", "onClick: userAdpater: " + userModel.getuID());
                    Log.d("TAG", "onClick: userAdpater: " + hashmap);

                    ref.child(userModel.getuID()).setValue(hashmap).addOnSuccessListener(unused -> {
                        Toast.makeText(builder.getContext(), "User has been activated", Toast.LENGTH_SHORT).show();
                        holder.btnActivateUserRow.setVisibility(View.GONE);
                        holder.btnDeactivateUserRow.setVisibility(View.VISIBLE);
                    }).addOnFailureListener(e -> {
                        Toast.makeText(builder.getContext(), "Failed to deactivate user. Please try again later", Toast.LENGTH_SHORT).show();
                    });
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        holder.btnDeactivateUserRow.setOnClickListener(view -> {
            if (userModel.getUserType().equals("Customer")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure to deactivate the user");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();

                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put("userId", userModel.getuID());
                    hashmap.put("userType", "Customer");
                    hashmap.put("name", userModel.getName());
                    hashmap.put("email", userModel.getEmail());
                    hashmap.put("contactNo", userModel.getContactNo());
                    hashmap.put("address", userModel.getAddress());
                    hashmap.put("password", userModel.getPassword());
                    hashmap.put("businessName", "");
                    hashmap.put("spID", "");
                    hashmap.put("active", "NOT ACTIVE");

                    ref.child(userModel.getuID()).setValue(hashmap).addOnSuccessListener(unused -> {
                        Toast.makeText(builder.getContext(), "User has been deactivated", Toast.LENGTH_SHORT).show();
                        holder.btnActivateUserRow.setVisibility(View.VISIBLE);
                        holder.btnDeactivateUserRow.setVisibility(View.GONE);
                    }).addOnFailureListener(e -> {
                        Toast.makeText(builder.getContext(), "Failed to deactivate user. Please try again later", Toast.LENGTH_SHORT).show();
                    });
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if(userModel.getUserType().equals("ServiceProvider")){
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure to deactivate the user");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.cancel();
                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put("userId", userModel.getuID());
                    hashmap.put("userType", "ServiceProvider");
                    hashmap.put("name", userModel.getName());
                    hashmap.put("email", userModel.getEmail());
                    hashmap.put("contactNo", userModel.getContactNo());
                    hashmap.put("address", userModel.getAddress());
                    hashmap.put("password", userModel.getPassword());
                    hashmap.put("businessName", userModel.getBusinessName());
                    hashmap.put("spID", userModel.getSpID());
                    hashmap.put("active", "NOT ACTIVE");

                    Log.d("TAG", "onClick: userAdpater: " + userModel.getuID());
                    Log.d("TAG", "onClick: userAdpater: " + hashmap);

                    ref.child(userModel.getuID()).setValue(hashmap).addOnSuccessListener(unused -> {
                        Toast.makeText(builder.getContext(), "User has been deactivated", Toast.LENGTH_SHORT).show();
                        holder.btnActivateUserRow.setVisibility(View.VISIBLE);
                        holder.btnDeactivateUserRow.setVisibility(View.GONE);
                    }).addOnFailureListener(e -> {
                        Toast.makeText(builder.getContext(), "Failed to deactivate user. Please try again later", Toast.LENGTH_SHORT).show();
                    });
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img1;
        TextView txtUserNameRow, txtUserEmailRow;
        Button btnDeactivateUserRow, btnActivateUserRow;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.img1);
            txtUserNameRow = itemView.findViewById(R.id.txtUserNameRow);
            txtUserEmailRow = itemView.findViewById(R.id.txtUserEmailRow);
            btnDeactivateUserRow = itemView.findViewById(R.id.btnDeactivateUserRow);
            btnActivateUserRow = itemView.findViewById(R.id.btnActivateUserRow);
        }
    }
}
