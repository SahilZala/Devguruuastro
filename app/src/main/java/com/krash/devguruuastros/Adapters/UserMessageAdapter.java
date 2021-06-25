package com.krash.devguruuastros.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krash.devguruuastros.Models.Messages;
import com.krash.devguruuastros.R;
import com.krash.devguruuastros.databinding.ChatItemLeftBinding;
import com.krash.devguruuastros.databinding.ChatItemRightBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserMessageAdapter extends RecyclerView.Adapter {

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;
    Context context;
    ArrayList<Messages> messages;
    String usertype;


    public UserMessageAdapter(Context context, ArrayList<Messages> Message,String usertype) {
        this.context = context;
        this.messages = Message;
        this.usertype = usertype;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new UserMessageAdapter.SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new UserMessageAdapter.ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Messages message = messages.get(position);

        if (message.getSender().equalsIgnoreCase(usertype)) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages message = messages.get(position);

        if(message.getType().equalsIgnoreCase("msg"))
        {
            if(usertype.equalsIgnoreCase(message.getSender()))
            {
                    UserMessageAdapter.SentViewHolder viewHolder = (UserMessageAdapter.SentViewHolder) holder;
                    viewHolder.binding.showMessage.setText(message.getMessage());


            }
            else {
                    UserMessageAdapter.ReceiverViewHolder viewHolder = (UserMessageAdapter.ReceiverViewHolder) holder;
                    viewHolder.binding.message.setText(message.getMessage());
            }
        }
        else if(message.getType().equalsIgnoreCase("image"))
        {
            if(usertype.equalsIgnoreCase(message.getSender()))
            {
                UserMessageAdapter.SentViewHolder viewHolder = (UserMessageAdapter.SentViewHolder) holder;
                viewHolder.binding.showMessage.setText(message.getMessage());
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                Picasso.get().load(message.getUrl()).into(viewHolder.binding.image);


            }
            else {
                UserMessageAdapter.ReceiverViewHolder viewHolder = (UserMessageAdapter.ReceiverViewHolder) holder;

                viewHolder.binding.message.setText(message.getMessage());
                viewHolder.binding.imageView11.setVisibility(View.VISIBLE);
                Picasso.get().load(message.getUrl()).into(viewHolder.binding.imageView11);
            }
        }

//        if (holder.getClass() == UserMessageAdapter.SentViewHolder.class) {
//            UserMessageAdapter.SentViewHolder viewHolder = (UserMessageAdapter.SentViewHolder) holder;
//            if (message.getMessage().equals("Photo")) {
//                viewHolder.binding.image.setVisibility(View.VISIBLE);
//                viewHolder.binding.showMessage.setVisibility(View.GONE);
////                Glide.with(context).load(message.getImageURL())
////                        .placeholder(R.drawable.placeholder)
////                        .into(viewHolder.binding.image);
//            }
//            viewHolder.binding.showMessage.setText(message.getMessage());
//        } else {
//            UserMessageAdapter.ReceiverViewHolder viewHolder = (UserMessageAdapter.ReceiverViewHolder) holder;
//            if (message.getMessage().equals("Photo")) {
//                viewHolder.binding.imageView11.setVisibility(View.VISIBLE);
//                viewHolder.binding.message.setVisibility(View.GONE);
//                Glide.with(context).load(message.getImageURL())
//                        .placeholder(R.drawable.placeholder)
//                        .into(viewHolder.binding.imageView11);
//            }
//            viewHolder.binding.message.setText(message.getMessage());
    //    }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        ChatItemRightBinding binding;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChatItemRightBinding.bind(itemView);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ChatItemLeftBinding binding;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChatItemLeftBinding.bind(itemView);
        }
    }    
}
