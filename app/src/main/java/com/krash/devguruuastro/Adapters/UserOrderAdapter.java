package com.krash.devguruuastro.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.krash.devguruuastro.Models.UserOrder;
import com.krash.devguruuastro.R;
import com.krash.devguruuastro.databinding.OrderLayoutBinding;

import java.util.ArrayList;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.UserOrderViewHolder> {
    Context context;
    ArrayList<UserOrder> orders;
    FirebaseDatabase firebaseDatabase;

    public UserOrderAdapter(Context context, ArrayList<UserOrder> orders) {
        this.context = context;
        this.orders = orders;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false);
        return new UserOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserOrderViewHolder holder, int position) {
        final UserOrder order = orders.get(position);
        holder.binding.orderID.setText("Order ID: " + order.getOrderID());
        holder.binding.name.setText("Name: " + order.getName());
        holder.binding.duration.setText("Duration: " + order.getDuration() + " min(s)");
        holder.binding.orderTime.setText("Date ~ Time: " + order.getOrderDate()+" ~ "+ order.getOrderTime());
        holder.binding.earning.setText("₹" + order.getExpense());
        holder.binding.tds.setVisibility(View.GONE);
        holder.binding.gateway.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class UserOrderViewHolder extends RecyclerView.ViewHolder {
        OrderLayoutBinding binding;

        public UserOrderViewHolder(@NonNull final View itemView) {
            super(itemView);
            binding = OrderLayoutBinding.bind(itemView);
        }
    }
}
