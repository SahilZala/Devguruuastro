package com.krash.devguruuastros.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.krash.devguruuastros.Models.ReviewModel;
import com.krash.devguruuastros.R;
import com.krash.devguruuastros.databinding.ReviewLayoutBinding;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<com.krash.devguruuastros.Adapters.ReviewAdapter.ReviewViewHolder> {

    Context context;
    ArrayList<ReviewModel> reviews;
    FirebaseDatabase firebaseDatabase;
    String userType;

    public ReviewAdapter(Context context, ArrayList<ReviewModel> reviews, String userType) {
        this.context = context;
        this.reviews = reviews;
        firebaseDatabase = FirebaseDatabase.getInstance();
        this.userType = userType;
    }

    @NonNull
    @Override
    public com.krash.devguruuastros.Adapters.ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_layout, parent, false);
        return new com.krash.devguruuastros.Adapters.ReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final com.krash.devguruuastros.Adapters.ReviewAdapter.ReviewViewHolder holder, int position) {
        final ReviewModel review = reviews.get(position);
        holder.binding.userRVMsg.setText(review.getMessage());
        holder.binding.userTimeStampRV.setText(review.getTimestamp());
        holder.binding.usernameRV.setText(review.getUsername());
        int rating = Integer.parseInt(review.getRating());
        for (int i = 0; i < rating; i++) {
            ImageView starView = (ImageView) holder.binding.rvContainerReviewLayout.getChildAt(i);
            starView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFBB00")));
        }
        if (userType.equals("ast")) {
//            holder.binding.deleteReviewBtn.setVisibility(View.VISIBLE);
//            holder.binding.deleteReviewBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    firebaseDatabase.getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid()).child("Reviews").child(review.getReviewID()).setValue(null);
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ReviewLayoutBinding binding;

        public ReviewViewHolder(@NonNull final View itemView) {
            super(itemView);
            binding = ReviewLayoutBinding.bind(itemView);
        }
    }
}


