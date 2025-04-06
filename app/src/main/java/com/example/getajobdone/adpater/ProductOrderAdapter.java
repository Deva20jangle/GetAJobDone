package com.example.getajobdone.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.getajobdone.OrderDetails;
import com.example.getajobdone.ProductOrderDetails;
import com.example.getajobdone.R;
import com.example.getajobdone.model.ProductOrder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ProductOrderViewHolder> {

    private final List<ProductOrder> orderModelList;
    private final Context context;

    public ProductOrderAdapter(List<ProductOrder> orderModelList, Context context) {
        this.orderModelList = orderModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_order_row, parent, false);
        return new ProductOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderViewHolder holder, int position) {
        ProductOrder order = orderModelList.get(position);
        holder.txtOrderStatus.setText(order.getOrderStatus());
        holder.txtProductName.setText(order.getProductName());
        holder.txtProductPrice.setText(order.getProductPrice());
        holder.txtCustomerAddress.setText(order.getAddress());
        Glide.with(context).load(order.getProductImage()).into(holder.imgProduct);

        holder.itemView.setOnClickListener(view -> {
            if(order.getSpId().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                context.startActivity(new Intent(context, ProductOrderDetails.class)
                        .putExtra("orderId", order.getOrderId())
                        .putExtra("productId", order.getProductId())
                        .putExtra("address", order.getAddress())
                        .putExtra("productName", order.getProductName())
                        .putExtra("spUid", order.getSpId())
                        .putExtra("productPrice", order.getProductPrice())
                        .putExtra("orderStatus", order.getOrderStatus())
                        .putExtra("productImage", order.getProductImage()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public static class ProductOrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderStatus, txtProductName, txtProductPrice, txtCustomerAddress;
        ImageView imgProduct;

        public ProductOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtProductName = itemView.findViewById(R.id.txtProductNameOrderRow);
            txtProductPrice = itemView.findViewById(R.id.txtProductPriceOrderRow);
            txtCustomerAddress = itemView.findViewById(R.id.txtCustomerAddressOrderRow);
            imgProduct = itemView.findViewById(R.id.imgProductOrderRow);
        }
    }
}