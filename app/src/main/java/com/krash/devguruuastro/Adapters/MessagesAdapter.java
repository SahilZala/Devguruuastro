package com.krash.devguruuastro.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.krash.devguruuastro.Models.Message;
import com.krash.devguruuastro.R;
import com.krash.devguruuastro.databinding.ChatItemLeftBinding;
import com.krash.devguruuastro.databinding.ChatItemRightBinding;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;
    Context context;
    ArrayList<Message> messages;

    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderID())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder.getClass() == SentViewHolder.class) {
            SentViewHolder viewHolder = (SentViewHolder) holder;
            if (message.getMessage().equals("Photo")) {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.showMessage.setVisibility(View.GONE);
                Glide.with(context).load(message.getImageURL())
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.binding.image);
            }
            viewHolder.binding.showMessage.setText(message.getMessage());
        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            if (message.getMessage().equals("Photo")) {
                viewHolder.binding.imageView11.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context).load(message.getImageURL())
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.binding.imageView11);
            }
            viewHolder.binding.message.setText(message.getMessage());
        }
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
