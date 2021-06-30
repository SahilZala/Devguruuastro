package com.krash.devguruuastros.Activities;

import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.common.collect.Comparators;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krash.devguruuastros.Adapters.AstOrderAdapter;
import com.krash.devguruuastros.Adapters.UserOrderAdapter;
import com.krash.devguruuastros.Models.AstOrder;
import com.krash.devguruuastros.Models.UserOrder;
import com.krash.devguruuastros.databinding.ActivityMyOrdersBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MyOrdersActivity extends AppCompatActivity {
    ActivityMyOrdersBinding binding;
    String user;
    AstOrderAdapter astOrderAdapter;
    UserOrderAdapter userOrderAdapter;
    FirebaseDatabase firebaseDatabase;
    ArrayList<AstOrder> astOrderList;
    ArrayList<UserOrder> userOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();
        user = getIntent().getStringExtra("userType");

        binding.historyBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (user.equals("ast")) {
            astOrderList = new ArrayList<>();
            firebaseDatabase.getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid()).child("Orders")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                AstOrder model = dataSnapshot.getValue(AstOrder.class);
                                astOrderList.add(model);

                                Comparator<AstOrder> comparator = new Comparator<AstOrder>() {
                                    @Override
                                    public int compare(AstOrder o1, AstOrder o2) {

                                        SimpleDateFormat smf = new SimpleDateFormat("dd-MM-yyyy");
                                        SimpleDateFormat smf1 = new SimpleDateFormat("HH:mm:ss");

                                        try {
                                            Date d1 = smf.parse(o1.getOrderDate());
                                            Date d2 = smf.parse(o2.getOrderDate());

                                            Date d3 = smf1.parse(o1.getOrderTime());
                                            Date d4 = smf1.parse(o2.getOrderTime());




                                            if(d1.compareTo(d2) == -1)
                                            {
                                                return 1;
                                            }
                                            else if(d1.compareTo(d2) == 0){
                                                if((d3.getTime() - d4.getTime()) < 0) {
                                                    return 1;
                                                }
                                                else {
                                                    return -1;
                                                }
                                            }
                                            else{
                                                return -1;
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }


                                        return 0;

                                    }
                                };


                                Collections.sort(astOrderList,comparator);

                                // Collections.reverse(astOrderList);



                                binding.orderRV.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this, LinearLayoutManager.VERTICAL, false));
                                astOrderAdapter = new AstOrderAdapter(getApplicationContext(), astOrderList);
                                binding.orderRV.setAdapter(astOrderAdapter);
                                astOrderAdapter.notifyDataSetChanged();
                            }
                            if (astOrderList.size() == 0) {

                            } else {

//                                int minindex = 0;
//                                SimpleDateFormat smf = new SimpleDateFormat("dd-MM-yyyy");
//
//
//                                for(int k = 0;k<astOrderList.size()-1;k++)
//                                {
//                                    minindex = k;
//
//                                    for(int j = k+1;j<astOrderList.size();j++)
//                                    {
//                                        try {
//                                            Date d1 = smf.parse(astOrderList.get(minindex).getOrderDate());
//                                            Date d2 = smf.parse(astOrderList.get(j).getOrderDate());
//
//                                            if(d1.compareTo(d2) == 1)
//                                            {
//                                                minindex = j;
//                                            }
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//
//
//                                    astOrderList.set(minindex,astOrderList.get(k));
//                                    astOrderList.set(k,astOrderList.get(minindex));
//
//                                }
//


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        } else {
            userOrderList = new ArrayList<>();
            firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Orders")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userOrderList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                UserOrder model = dataSnapshot.getValue(UserOrder.class);
                                userOrderList.add(model);


                                Comparator<UserOrder> comparator = new Comparator<UserOrder>() {
                                    @Override
                                    public int compare(UserOrder o1, UserOrder o2) {

                                        SimpleDateFormat smf = new SimpleDateFormat("dd-MM-yyyy");
                                        SimpleDateFormat smf1 = new SimpleDateFormat("HH:mm:ss");

                                        try {
                                            Date d1 = smf.parse(o1.getOrderDate());
                                            Date d2 = smf.parse(o2.getOrderDate());

                                            Date d3 = smf1.parse(o1.getOrderTime());
                                            Date d4 = smf1.parse(o2.getOrderTime());




                                            if(d1.compareTo(d2) == -1)
                                            {
                                                return 1;
                                            }
                                            else if(d1.compareTo(d2) == 0){
                                                if((d3.getTime() - d4.getTime()) < 0) {
                                                    return 1;
                                                }
                                                else {
                                                    return -1;
                                                }
                                            }
                                            else{
                                                return -1;
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }


                                        return 0;

                                    }
                                };


                                Collections.sort(userOrderList,comparator);




                                binding.orderRV.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this, LinearLayoutManager.VERTICAL, false));
                                userOrderAdapter = new UserOrderAdapter(getApplicationContext(), userOrderList);
                                binding.orderRV.setAdapter(userOrderAdapter);
                                userOrderAdapter.notifyDataSetChanged();
                            }

//                            if (userOrderList.size() == 0) {
//
//                            } else {
//
//                                int minindex = 0;
//                                SimpleDateFormat smf = new SimpleDateFormat("dd-MM-yyyy");
//                                for(int k = 0;k<userOrderList.size()-1;k++)
//                                {
//                                    minindex = k;
//
//                                    for(int j = k+1;j<userOrderList.size();j++)
//                                    {
//                                        try {
//                                            Date d1 = smf.parse(userOrderList.get(minindex).getOrderDate());
//                                            Date d2 = smf.parse(userOrderList.get(j).getOrderDate());
//
//                                            if(d1.compareTo(d2) == 1)
//                                            {
//                                                minindex = j;
//                                            }
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//
//
//                                    userOrderList.set(minindex,userOrderList.get(k));
//                                    userOrderList.set(k,userOrderList.get(minindex));
//
//                                }
//
//                                Collections.reverse(userOrderList);
//
//                                binding.orderRV.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this, LinearLayoutManager.VERTICAL, false));
//                                userOrderAdapter = new UserOrderAdapter(getApplicationContext(), userOrderList);
//                                binding.orderRV.setAdapter(userOrderAdapter);
//                                userOrderAdapter.notifyDataSetChanged();
//                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }


}