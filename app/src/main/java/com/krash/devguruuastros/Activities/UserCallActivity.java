package com.krash.devguruuastros.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krash.devguruuastros.Adapters.UsersAdapter;
import com.krash.devguruuastros.Models.RequestClass;
import com.krash.devguruuastros.Models.SahilRingtone;
import com.krash.devguruuastros.Models.User;
import com.krash.devguruuastros.R;

import java.util.ArrayList;
import java.util.List;

public class UserCallActivity extends AppCompatActivity {

    ArrayList<User> users;
    ArrayList<String> userID;
    UsersAdapter usersAdapter;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_call);

        checkIsRinging();
        firebaseDatabase = FirebaseDatabase.getInstance();

        users = new ArrayList<>();
        userID = new ArrayList<>();

        recyclerView = findViewById(R.id.userRV);
        recyclerView.setHasFixedSize(true);



        getAstrologerRequest();

//        firebaseDatabase.getReference().child("Chats").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    if (snapshot1.getKey().contains(FirebaseAuth.getInstance().getUid())) {
//                        String uid = snapshot1.getKey().substring(28);
//                        if (!uid.equals(FirebaseAuth.getInstance().getUid())) {
//                            userID.add(uid);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                users.clear();
//                for (String uid : userID) {
//                    User user = snapshot.child(uid).getValue(User.class);
//                    users.add(user);
//                }
//                usersAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,AstrologerMainActivity.class);
        startActivity(i);
        finishAffinity();
    }

    List<RequestClass> requestClassList;

    void getAstrologerRequest()
    {
        requestClassList = new ArrayList<>();

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("RequestQueue").child(FirebaseAuth.getInstance().getUid());

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                requestClassList.clear();
                users.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    final RequestClass rc = ds.getValue(RequestClass.class);

//                    Toast.makeText(UsersActivity.this, ""+rc.getSessionid(), Toast.LENGTH_SHORT).show();

                    if(rc.getActivation().equalsIgnoreCase("true") && rc.getStatus().equalsIgnoreCase("request") && rc.getReqtype().equalsIgnoreCase("call"))
                    {
                        requestClassList.add(rc);

                        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users").child(rc.getUserid());

                        userref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                User u = snapshot.getValue(User.class);
                                //  Toast.makeText(UsersActivity.this, ""+u.getName(), Toast.LENGTH_SHORT).show();
                                if(u != null) {
                                    u.setRc(rc);

//                                Toast.makeText(UsersActivity.this, ""+u.getName(), Toast.LENGTH_SHORT).show();

                                    users.add(u);

                                    usersAdapter = new UsersAdapter(UserCallActivity.this, users);
                                    recyclerView.setAdapter(usersAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    void checkIsRinging()
    {
        if(SahilRingtone.getRingtone() != null) {
            if (SahilRingtone.isPlaying()) {
                SahilRingtone.stop();
            }
        }
    }
}