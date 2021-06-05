package com.krash.devguruuastro.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krash.devguruuastro.Activities.AstrologerDetailsActivity;
import com.krash.devguruuastro.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoListAdapte extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> items = new ArrayList<>();
    private List<String> keys = new ArrayList<>();

    private Context ctx;

    String uid;
    String type;

    public PhotoListAdapte(Context ctx,List<String> items, List<String> keys, String uid, String type) {
        this.items = items;
        this.keys = keys;
        this.ctx = ctx;
        this.uid = uid;
        this.type = type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView photo;
        public OriginalViewHolder(View v) {
            super(v);
            photo = (ImageView) v.findViewById(R.id.profile_image);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_recyclerview_item, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            String p = items.get(position);
            Picasso.get().load(p).into(view.photo);

            view.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type.equalsIgnoreCase("ast")) {
                        FirebaseDatabase.getInstance().getReference("Astrologers").child(uid).child("Photos").child(keys.get(position)).removeValue();

                        decreaseCount();
                    }
                }
            });
            //    Tools.displayImageRound(ctx, view.image, p.image);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    void decreaseCount()
    {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Astrologers").child(uid).child("photocount");
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String count  = snapshot.getValue(String.class);

                FirebaseDatabase.getInstance().getReference("Astrologers").child(uid).child("photocount").setValue(String.valueOf(Integer.parseInt(count) - 1));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}