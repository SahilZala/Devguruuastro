package com.krash.devguruuastros.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.krash.devguruuastros.Activities.AstrologerCallActivity;
import com.krash.devguruuastros.Activities.AstrologerChatActivity;
import com.krash.devguruuastros.Models.User;
import com.krash.devguruuastros.R;
import com.krash.devguruuastros.databinding.UserItemBinding;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    Context context;
    ArrayList<User> users;
    FirebaseDatabase firebaseDatabase;

    public UsersAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersViewHolder holder, final int position) {
        final User user = users.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (users.get(position).getRc().getReqtype().equalsIgnoreCase("chat")) {
                    Intent i = new Intent(v.getContext(), AstrologerChatActivity.class);
//                i.putExtra("name", user.getName());
//                i.putExtra("uid", user.getUid());
//                i.putExtra("imageURL", user.getProfileImage());
//                i.putExtra("gender", user.getGender());
//                i.putExtra("dob", user.getDOB());
//                i.putExtra("tob", user.getTOB());
//                i.putExtra("pob", user.getPOB());
//                i.putExtra("occupation", user.getOccupation());
//                i.putExtra("MaritalStatus", user.getMaritalStatus());
//                i.putExtra("type", "user");
//                i.putExtra("duration", user.getDuration());
//                i.putExtra("sessionid", user.getRc().getSessionid());
//                i.putExtra("requsestid",user.getRc().getRequestid());


                    Gson gson = new Gson();
                    String myjson = gson.toJson(user);
                    //   Toast.makeText(context, ""+myjson, Toast.LENGTH_SHORT).show();
                    Log.d("userobject", myjson);
                    i.putExtra("myjson", myjson);
//                Map<String, Object> updateStatus = new HashMap<>();
//                updateStatus.put("status", "completed");
//                firebaseDatabase.getReference().child("Users").child(user.getUid()).updateChildren(updateStatus);
                    context.startActivity(i);
                }
                else
                {

                    Intent i = new Intent(v.getContext(), AstrologerCallActivity.class);
                    Gson gson = new Gson();
                    String myjson = gson.toJson(user);
                    //   Toast.makeText(context, ""+myjson, Toast.LENGTH_SHORT).show();
                    Log.d("userobject", myjson);
                    i.putExtra("myjson", myjson);
//                Map<String, Object> updateStatus = new HashMap<>();
//                updateStatus.put("status", "completed");
//                firebaseDatabase.getReference().child("Users").child(user.getUid()).updateChildren(updateStatus);
                    context.startActivity(i);

                }
            }

        });

        holder.binding.username.setText(user.getName());
        holder.binding.genderTV.setText("Gender: " + user.getGender());
        holder.binding.dobTV.setText("Date of Birth: " + user.getDOB());
        holder.binding.tobTV.setText("Time of Birth: " + user.getTOB());
        holder.binding.pobTV.setText("Place of Birth: " + user.getPOB());
        holder.binding.occupationTV.setText("Occupation: " + user.getOccupation());
        holder.binding.durationTV.setText("Duration: " + user.getDuration() + " min(s)");
        holder.binding.reqtype.setText("Reqtype: "+ user.getRc().getReqtype());
        Glide.with(holder.itemView).load(user.getProfileImage()).into(holder.binding.profileImage);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {
        UserItemBinding binding;

        public UsersViewHolder(@NonNull final View itemView) {
            super(itemView);
            binding = UserItemBinding.bind(itemView);
        }
    }
}
