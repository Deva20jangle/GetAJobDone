package com.example.getajobdone.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getajobdone.R;
import com.example.getajobdone.model.serviceModel;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.MyViewHolder> {

    private final List<serviceModel> todoList;
    private final Context context;

    public TodoListAdapter(List<serviceModel> todoList, Context context) {
        this.todoList = todoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        serviceModel service = todoList.get(position);
        holder.txtBusinessNameRow.setText(service.getBusinessName());
        holder.txtServiceTypeRow.setText(service.getServiceType());
        holder.txtServicePriceRow.setText(service.getServicePrice());
        holder.txtRating.setText(service.getServiceRating());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
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