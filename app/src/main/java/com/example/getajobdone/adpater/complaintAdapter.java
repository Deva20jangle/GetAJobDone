package com.example.getajobdone.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getajobdone.R;
import com.example.getajobdone.ResolveComplaint;
import com.example.getajobdone.model.complaintModel;

import java.util.List;

public class complaintAdapter extends RecyclerView.Adapter<complaintAdapter.MyViewHolder> {

    private final List<complaintModel> complaintModelList;
    private final Context context;

    public complaintAdapter(List<complaintModel> complaintModelList, Context context) {
        this.complaintModelList = complaintModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public complaintAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_row, null));
    }

    @Override
    public void onBindViewHolder(@NonNull complaintAdapter.MyViewHolder holder, int position) {
        complaintModel complaintModel = complaintModelList.get(position);
        holder.txtComplaintCustomerNameRow.setText(complaintModel.getCustomerName());
        holder.txtComplaintAgainstSPNameRow.setText(complaintModel.getSPName());

        holder.itemView.setOnClickListener(view -> {
            context.startActivity(new Intent(context, ResolveComplaint.class)
                    .putExtra("complaintId", complaintModel.getComplaintId())
                    .putExtra("customerName", complaintModel.getCustomerName())
                    .putExtra("customerPhone", complaintModel.getCustomerContactNo())
                    .putExtra("SPName", complaintModel.getSPName())
                    .putExtra("SPPhone", complaintModel.getSPContactNo())
                    .putExtra("resolvedDesc", complaintModel.getResolvedDesc())
                    .putExtra("Complaint", complaintModel.getComplaintDesc()));
        });
    }

    @Override
    public int getItemCount() {
        return complaintModelList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtComplaintCustomerNameRow, txtComplaintAgainstSPNameRow;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtComplaintCustomerNameRow = itemView.findViewById(R.id.txtComplaintCustomerNameRow);
            txtComplaintAgainstSPNameRow = itemView.findViewById(R.id.txtComplaintAgainstSPNameRow);
        }
    }
}
