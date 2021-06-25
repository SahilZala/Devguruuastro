package com.krash.devguruuastros.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.krash.devguruuastros.Models.AstOrder;
import com.krash.devguruuastros.R;
import com.krash.devguruuastros.databinding.OrderLayoutBinding;

import java.util.ArrayList;

public class AstOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<AstOrder> orders;
    FirebaseDatabase firebaseDatabase;

    public AstOrderAdapter(Context context, ArrayList<AstOrder> orders) {
        this.context = context;
        this.orders = orders;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView orderid,date,time,duration,user_paid,total_earning,order_type;

        public OriginalViewHolder(View v) {
            super(v);
            orderid = v.findViewById(R.id.paymentid);
            date = v.findViewById(R.id.orderdate);
            time = v.findViewById(R.id.ordertime);
            duration = v.findViewById(R.id.duration);
            user_paid = v.findViewById(R.id.userpaid);
            total_earning = v.findViewById(R.id.totalearning);
            order_type = v.findViewById(R.id.ordertype);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ast_orders_list_item, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.orderid.setText(orders.get(position).getOrderID());
            view.date.setText(orders.get(position).getOrderDate());
            view.time.setText(orders.get(position).getOrderTime());
            view.duration.setText(orders.get(position).getDuration());
            //view.user_paid.setText(String.valueOf((Integer.parseInt(orders.get(position).getEarning()) * 2)));
            view.user_paid.setText(orders.get(position).getUserpaid());
            view.total_earning.setText(orders.get(position).getEarning());
            view.order_type.setText(orders.get(position).getOrderType());
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }




//    @NonNull
//    @Override
//    public AstOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.ast_orders_list_item, parent, false);
//        return new AstOrderViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final AstOrderViewHolder holder, int position) {
//        final AstOrder order = orders.get(position);
//
//
//
//
////        holder.binding.orderID.setText("Order ID: " + order.getOrderID());
////        holder.binding.name.setText("Name: " + order.getName());
////        holder.binding.duration.setText("Duration: " + order.getDuration() + " min(s)");
////        holder.binding.orderTime.setText("Order Time: " + order.getOrderDate());
////        holder.binding.earning.setText("₹" + order.getEarning());
////        holder.binding.tds.setText("₹" + order.getTds());
////        holder.binding.gateway.setText("₹" + order.getGeteway());
//    }
//
//    @Override
//    public int getItemCount() {
//        return orders.size();
//    }
//
//    public class AstOrderViewHolder extends RecyclerView.ViewHolder {
//        //OrderLayoutBinding binding;
//
//        public AstOrderViewHolder(@NonNull final View itemView) {
//            super(itemView);
//          //  binding = OrderLayoutBinding.bind(itemView);
//        }
//    }
}
