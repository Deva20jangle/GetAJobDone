package com.example.getajobdone.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getajobdone.R;
import com.example.getajobdone.model.serviceModel;
import com.example.getajobdone.ServiceDetails;

import java.util.ArrayList;
import java.util.List;

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
            if (keyword.toString().isEmpty())
                filteredData.addAll(backupList);
            else{
                for (serviceModel model : backupList){
                        if (model.getServiceType().toString().toLowerCase().contains(keyword.toString().toLowerCase())){
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
            serviceModelList.addAll((ArrayList<serviceModel>)filterResults.values);
            notifyDataSetChanged();
        }
    };

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtBusinessNameRow, txtServiceTypeRow, txtServicePriceRow, txtRating;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBusinessNameRow = itemView.findViewById(R.id.txtBusinessNameRow);
            txtServiceTypeRow = itemView.findViewById(R.id.txtServiceTypeRow);
            txtServicePriceRow = itemView.findViewById(R.id.txtServicePriceRow);
            txtRating = itemView.findViewById(R.id.txtRating);
        }
    }
}
