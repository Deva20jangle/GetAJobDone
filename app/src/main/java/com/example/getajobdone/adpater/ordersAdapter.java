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

import com.example.getajobdone.OrderDetails;
import com.example.getajobdone.R;
import com.example.getajobdone.model.orderModel;
import com.example.getajobdone.model.serviceModel;

import java.util.ArrayList;
import java.util.List;

public class ordersAdapter extends RecyclerView.Adapter<ordersAdapter.MyViewHolder> implements Filterable {
    private final List<orderModel> orderModelList;
    private List<orderModel> backupList;
    private final Context context;

    public ordersAdapter(List<orderModel> orderModelList, Context context) {
        this.orderModelList = orderModelList;
        this.context = context;
        backupList = new ArrayList<>(orderModelList);
    }

    @NonNull
    @Override
    public ordersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ordersAdapter.MyViewHolder holder, int position) {
        orderModel orderModel = orderModelList.get(position);
        holder.txtCustomerNameOrderRow.setText(orderModel.getCustomerName());
        holder.txtOrderDate.setText(orderModel.getDate());
        holder.txtOrderServiceType.setText(orderModel.getServiceType());
        holder.txtOrderStatus.setText(orderModel.getOrderStatus());

        holder.itemView.setOnClickListener(view -> {
            holder.itemView.getContext().startActivity(new Intent(context, OrderDetails.class)
                    .putExtra("orderId", orderModel.getOrderId())
                    .putExtra("customerName", orderModel.getCustomerName())
                    .putExtra("customerContact", orderModel.getCustomerContact())
                    .putExtra("customerUid", orderModel.getCustomerUid())
                    .putExtra("serviceId", orderModel.getServiceId())
                    .putExtra("address", orderModel.getAddress())
                    .putExtra("serviceType", orderModel.getServiceType())
                    .putExtra("businessName", orderModel.getBusinessName())
                    .putExtra("spUid", orderModel.getSpUid())
                    .putExtra("servicePrice", orderModel.getServicePrice())
                    .putExtra("serviceDesc", orderModel.getServiceDesc())
                    .putExtra("SPContactNo", orderModel.getSPContactNo())
                    .putExtra("spName", orderModel.getSpName())
                    .putExtra("orderStatus", orderModel.getOrderStatus())
                    .putExtra("orderDesc", orderModel.getOrderDesc())
                    .putExtra("orderStatusDesc", orderModel.getOrderStatusDesc())
                    .putExtra("date", orderModel.getDate()));
        });
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<orderModel> filteredData = new ArrayList<>();
            if (keyword.toString().equals("Select Status")) {
                filteredData.addAll(backupList);
            } else {
                for (orderModel model : backupList) {
                    if (model.getOrderStatus().toString().toLowerCase().contains(keyword.toString().toLowerCase())) {
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
            orderModelList.clear();
            orderModelList.addAll((ArrayList<orderModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtCustomerNameOrderRow, txtOrderDate, txtOrderServiceType, txtOrderStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCustomerNameOrderRow = itemView.findViewById(R.id.txtCustomerNameOrderRow);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderServiceType = itemView.findViewById(R.id.txtOrderServiceType);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
        }
    }
}