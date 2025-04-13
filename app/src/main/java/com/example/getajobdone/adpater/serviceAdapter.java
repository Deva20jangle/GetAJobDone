package com.example.getajobdone.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getajobdone.R;
import com.example.getajobdone.model.serviceModel;
import com.example.getajobdone.ServiceDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class serviceAdapter extends RecyclerView.Adapter<serviceAdapter.MyViewHolder> implements Filterable {
    private final List<serviceModel> serviceModelList;
    private List<serviceModel> backupList;
    private final Context context;

    public serviceAdapter(List<serviceModel> serviceModelList, Context context) {
        this.serviceModelList = serviceModelList;
        this.context = context;
        backupList = new ArrayList<>(serviceModelList);
    }


    @NonNull
    @Override
    public serviceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_row, null));
    }

    @Override
    public void onBindViewHolder(@NonNull serviceAdapter.MyViewHolder holder, int position) {
        serviceModel serviceModel = serviceModelList.get(position);
        holder.txtBusinessNameRow.setText(serviceModel.getBusinessName());
        holder.txtServicePriceRow.setText(serviceModel.getServicePrice());
        holder.txtServiceTypeRow.setText(serviceModel.getServiceType());
        holder.txtRating.setText(serviceModel.getServiceRating());

        holder.itemView.setOnClickListener(view -> {
            holder.itemView.getContext().startActivity(new Intent(context, ServiceDetails.class)
                    .putExtra("serviceId", serviceModel.getServiceID())
                    .putExtra("serviceType", serviceModel.getServiceType())
                    .putExtra("servicePrice", serviceModel.getServicePrice())
                    .putExtra("serviceDesc", serviceModel.getServiceDescription())
                    .putExtra("businessName", serviceModel.getBusinessName())
                    .putExtra("contactNo", serviceModel.getBusinessContactNo())
                    .putExtra("spUid", serviceModel.getSpUid())
                    .putExtra("address", serviceModel.getBusinessAddress())
                    .putExtra("spName", serviceModel.getSpName()));
        });

        holder.btnAddToTodo.setOnClickListener(view -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UsersTodoList")
                    .child(Objects.requireNonNull(auth.getUid())).child("TodoList");

            ref.orderByChild("serviceId").equalTo(serviceModel.getServiceID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(context, "Service already in ToDo list", Toast.LENGTH_SHORT).show();
                    } else {
                        HashMap<String, String> todoItem = new HashMap<>();
                        todoItem.put("serviceId", serviceModel.getServiceID());
                        todoItem.put("serviceType", serviceModel.getServiceType());
                        todoItem.put("servicePrice", serviceModel.getServicePrice());
                        todoItem.put("serviceDesc", serviceModel.getServiceDescription());
                        todoItem.put("businessName", serviceModel.getBusinessName());
                        todoItem.put("contactNo", serviceModel.getBusinessContactNo());
                        todoItem.put("spUid", serviceModel.getSpUid());
                        todoItem.put("address", serviceModel.getBusinessAddress());
                        todoItem.put("spName", serviceModel.getSpName());

                        ref.push().setValue(todoItem).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Service added to ToDo list", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed to add service to ToDo list", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<serviceModel> filteredData = new ArrayList<>();

            if (keyword.toString().isEmpty()) {
                filteredData.addAll(backupList);
            } else {
                String[] searchKeywords = keyword.toString().toLowerCase().split("\\s+");

                for (serviceModel model : backupList) {
                    String serviceType = model.getServiceType().toLowerCase();
                    String businessAddress = model.getBusinessAddress().toLowerCase();

                    boolean matchesAll = true;
                    for (String key : searchKeywords) {
                        if (!(serviceType.contains(key) || businessAddress.contains(key))) {
                            matchesAll = false;
                            break;
                        }
                    }

                    if (matchesAll) {
                        filteredData.add(model);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            serviceModelList.clear();
            serviceModelList.addAll((ArrayList<serviceModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };

//    Filter filter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence keyword) {
//            ArrayList<serviceModel> filteredData = new ArrayList<>();
//            if (keyword.toString().isEmpty())
//                filteredData.addAll(backupList);
//            else{
//                for (serviceModel model : backupList){
//                    if (model.getBusinessAddress().toString().toLowerCase().contains(keyword.toString().toLowerCase())){
//                        filteredData.add(model);
////                        if (model.getServiceType().toString().toLowerCase().contains(keyword.toString().toLowerCase())){
////                            filteredData.add(model);
//
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredData;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            serviceModelList.clear();
//            serviceModelList.addAll((ArrayList<serviceModel>)filterResults.values);
//            notifyDataSetChanged();
//        }
//    };

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtBusinessNameRow, txtServiceTypeRow, txtServicePriceRow, txtRating;
        private final Button btnAddToTodo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBusinessNameRow = itemView.findViewById(R.id.txtBusinessNameRow);
            txtServiceTypeRow = itemView.findViewById(R.id.txtServiceTypeRow);
            txtServicePriceRow = itemView.findViewById(R.id.txtServicePriceRow);
            txtRating = itemView.findViewById(R.id.txtRating);
            btnAddToTodo = itemView.findViewById(R.id.btnAddToTodo);
        }
    }
}
