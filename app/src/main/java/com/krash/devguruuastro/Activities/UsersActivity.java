package com.krash.devguruuastro.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krash.devguruuastro.Adapters.UsersAdapter;
import com.krash.devguruuastro.Models.RequestClass;
import com.krash.devguruuastro.Models.User;
import com.krash.devguruuastro.R;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    ArrayList<User> users;
    ArrayList<String> userID;
    UsersAdapter usersAdapter;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
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
        Intent  i = new Intent(this,AstrologerMainActivity.class);
        startActivity(i);
        finish();
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

                    if(rc.getActivation().equalsIgnoreCase("true") && rc.getStatus().equalsIgnoreCase("request"))
                    {
                        requestClassList.add(rc);

                        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users").child(rc.getUserid());

                        userref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                User u = snapshot.getValue(User.class);
                                u.setRc(rc);

//                                Toast.makeText(UsersActivity.this, ""+u.getName(), Toast.LENGTH_SHORT).show();

                                users.add(u);

                                usersAdapter = new UsersAdapter(UsersActivity.this, users);
                                recyclerView.setAdapter(usersAdapter);
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
}