package com.krash.devguruuastros.Adapters;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.krash.devguruuastros.Activities.AstrologerDetailsActivity;
import com.krash.devguruuastros.Models.AstrologerModel;
import com.krash.devguruuastros.R;

import java.util.List;

public class ChatMainAdapter extends RecyclerView.Adapter<ChatMainAdapter.ViewHolder> {
    private final List<AstrologerModel> astrologerModelList;

    public ChatMainAdapter(List<AstrologerModel> astrologerModelList) {
        this.astrologerModelList = astrologerModelList;
    }

    @NonNull
    @Override
    public ChatMainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ast_chat_layout, parent, false);
        return new ChatMainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatMainAdapter.ViewHolder holder, final int position) {
        String res = astrologerModelList.get(position).getProfileURL();
        String name = astrologerModelList.get(position).getName();
        String lang = astrologerModelList.get(position).getLanguage();
        String chatPrice = astrologerModelList.get(position).getChatPrice();
        holder.setIsOnline("Online");
        holder.setAstImage(res);
        holder.setAstLanguage(lang);
        holder.setAstName(name);
        holder.setAstPrice(chatPrice);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AstrologerDetailsActivity.class);
                i.putExtra("uid", astrologerModelList.get(position).getUid());
                i.putExtra("name", astrologerModelList.get(position).getName());
                i.putExtra("userType", "user");
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return astrologerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView astImg;
        TextView astDegree, astName, astLanguage, astExp, astPrice, isOnline;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            astImg = itemView.findViewById(R.id.astChatImg);
            astName = itemView.findViewById(R.id.astChatName);
            astLanguage = itemView.findViewById(R.id.astChatLang);
            astPrice = itemView.findViewById(R.id.astChatPrice);
            isOnline = itemView.findViewById(R.id.astChatStatus);
        }

        private void setAstImage(String res) {
            Glide.with(itemView.getContext()).load(res).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_person_24)).into(astImg);
        }

        private void setAstName(String name) {
            astName.setText(name);
        }

        private void setAstLanguage(String language) {
            astLanguage.setText(language);
        }

        public void setAstPrice(String price) {
            astPrice.setText("₹" + price + "/min");
        }

        public void setIsOnline(String online) {
            isOnline.setText(online);
            isOnline.setBackgroundTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.green)));
        }
    }
}
