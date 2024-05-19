package com.example.getajobdone.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getajobdone.R;
import com.example.getajobdone.model.subscribeModel;

import java.util.List;

public class subscribeAdapter extends RecyclerView.Adapter<subscribeAdapter.MyViewHolder> {

    private final List<subscribeModel> subscribeModelList;
    private final Context context;

    public subscribeAdapter(List<subscribeModel> subscribeModelList, Context context) {
        this.subscribeModelList = subscribeModelList;
        this.context = context;
    }


    @NonNull
    @Override
    public subscribeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new subscribeAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, null));
    }

    @Override
    public void onBindViewHolder(@NonNull subscribeAdapter.MyViewHolder holder, int position) {
        subscribeModel subscribeModel = subscribeModelList.get(position);
        holder.txtCustomerNameOrderRow.setText(subscribeModel.getBusinessName());
        holder.txtOrderDate.setText(subscribeModel.getEmail());
        holder.txtOrderServiceType.setText(subscribeModel.getSpUid());
        holder.txtOrderStatus.setText(subscribeModel.getActive());
    }

    @Override
    public int getItemCount() {
        return subscribeModelList.size();
    }

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
