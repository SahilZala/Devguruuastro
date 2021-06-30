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

public class AstrologerAdapter extends RecyclerView.Adapter<AstrologerAdapter.ViewHolder> {
    private final List<AstrologerModel> astrologerModelList;

    public AstrologerAdapter(List<AstrologerModel> astrologerModelList) {
        this.astrologerModelList = astrologerModelList;
    }

    @NonNull
    @Override
    public AstrologerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.astrologer_details_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AstrologerAdapter.ViewHolder holder, final int position) {
        String res = astrologerModelList.get(position).getProfileURL();
        String name = astrologerModelList.get(position).getName();
        String degree = astrologerModelList.get(position).getBachD() + ", " + astrologerModelList.get(position).getMastD();
        String exp = astrologerModelList.get(position).getExperience();
        String lang = astrologerModelList.get(position).getLanguage();
        String chatPrice = astrologerModelList.get(position).getChatPrice();
        if (astrologerModelList.get(position).getChatOnline().equals("Online") || astrologerModelList.get(position).getCallOnline().equals("Online")) {
            holder.setIsOnline("Online");
        } else {
            holder.setIsOnline("Offline");
        }
        holder.setAstDegree(degree);
        holder.setAstExp(exp);
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
        ImageView astImg, tick;
        TextView astDegree, astName, astLanguage, astExp, astPrice, isOnline;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            astImg = itemView.findViewById(R.id.astImage);
            astName = itemView.findViewById(R.id.astName);
            astDegree = itemView.findViewById(R.id.astDegree);
            astLanguage = itemView.findViewById(R.id.astLanguages);
            astExp = itemView.findViewById(R.id.astExp);
            astPrice = itemView.findViewById(R.id.astPrice);
            isOnline = itemView.findViewById(R.id.isOnline);
            tick = itemView.findViewById(R.id.tick);
        }

        private void setAstImage(String res) {
            Glide.with(itemView.getContext()).load(res).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_person_24)).into(astImg);
        }

        private void setAstName(String name) {
            astName.setText(name);
        }

        private void setAstDegree(String degree) {
            astDegree.setText(degree);
        }

        private void setAstLanguage(String language) {
            astLanguage.setText(language);
        }

        private void setAstExp(String exp) {
            astExp.setText(exp + " years");
        }

        public void setAstPrice(String price) {
            astPrice.setText(price + "/min");
        }

        public void setIsOnline(String online) {
            isOnline.setText(online);
            if (online.equals("Online")) {
                isOnline.setBackgroundTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.purple)));
                tick.setImageTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.purple)));
            } else {
                isOnline.setBackgroundTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.grey)));
                tick.setImageTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.grey)));
            }
        }
    }
}
