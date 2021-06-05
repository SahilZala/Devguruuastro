package com.krash.devguruuastro.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.krash.devguruuastro.Adapters.AstrologerAdapter;
import com.krash.devguruuastro.Adapters.SliderAdapter;
import com.krash.devguruuastro.MatMainActivity;
import com.krash.devguruuastro.Models.AstrologerModel;
import com.krash.devguruuastro.Models.FcmNotificationsSender;
import com.krash.devguruuastro.Models.FirebaseMessagingService;
import com.krash.devguruuastro.Models.SliderItem;
import com.krash.devguruuastro.Models.SystemTools;
import com.krash.devguruuastro.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private final Handler sliderHandler = new Handler();
    private final List<AstrologerModel> astrologerModelListOnline = new ArrayList<>();
    private final List<AstrologerModel> astrologerModelListOffline = new ArrayList<>();
    private final List<AstrologerModel> joinedList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    AlertDialog.Builder builder;
    String number;
    FirebaseAuth firebaseAuth;
    private LinearLayout matMain, chatMain, callMain;
    private ImageView userWallet, userSettings, userAccount, userOrders, userCS;
    private RecyclerView astrologerRV;
    private AstrologerAdapter astrologerAdapter;
    private ViewPager2 viewPager2;
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            //setData();
        }
    };

    int starPos;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);

        if(getIntent().getStringExtra("comesfrom").equalsIgnoreCase("userchat"))
        {
            final Dialog rvDialog = new Dialog(MainActivity.this);
            rvDialog.setContentView(R.layout.user_review_layout);
            rvDialog.setCancelable(true);
            rvDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rvDialog.show();
            starPos = 0;
            final EditText rvMsg = rvDialog.findViewById(R.id.rvMsg);
            final LinearLayout rvContainer = rvDialog.findViewById(R.id.rv_container);
            for (int x = 0; x < rvContainer.getChildCount(); x++) {
                starPos = x;
                final int finalStarPos = starPos;
                rvContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRating(finalStarPos, rvContainer);
                    }
                });
            }
            final String reviewID = UUID.randomUUID().toString().substring(0, 21);
            final Button rvSubBtn = rvDialog.findViewById(R.id.rvSubBtn);

            rvSubBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(rvMsg.getText().toString()) || (starPos != 0)) {
                        progressDialog.setMessage("Saving Review!");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Map<String, Object> reviewMap = new HashMap<>();
                        reviewMap.put("username", getIntent().getStringExtra("name"));
                        reviewMap.put("reviewID", reviewID);
                        reviewMap.put("rating", starPos + "");
                        reviewMap.put("message", rvMsg.getText().toString());
                        reviewMap.put("timestamp", SystemTools.getCurrent_date()+" ~ "+SystemTools.getCurrent_time());
                        firebaseDatabase.getReference().child("Astrologers").child(getIntent().getStringExtra("astid")).child("Reviews").child(reviewID).setValue(reviewMap);
                        //firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Reviews").child(reviewID).setValue(reviewMap);
                        progressDialog.dismiss();
                        rvDialog.dismiss();
                        //Intent i = new Intent(MainActivity.this, MainActivity.class);
                      //  startActivity(i);
                        //finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Please, Provide a review to serve you better!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


        viewPager2 = findViewById(R.id.viewPager22);
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.image1));
        sliderItems.add(new SliderItem(R.drawable.image2));
        sliderItems.add(new SliderItem(R.drawable.image4));
        sliderItems.add(new SliderItem(R.drawable.image6));
        sliderItems.add(new SliderItem(R.drawable.image7));
        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        firebaseAuth = FirebaseAuth.getInstance();



        FirebaseMessaging.getInstance().subscribeToTopic("all");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Astrologers");
        astrologerRV = findViewById(R.id.astrologers_rv);

        setData();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please, Sign Up First!");
        builder.setTitle("Devguruuastro");
        builder.setCancelable(true);
        builder.setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(i);
                finish();
            }
        });

        userWallet = findViewById(R.id.userWallet);
        userWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getUid() == null) {
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Intent i = new Intent(MainActivity.this, WalletActivity.class);
                    startActivity(i);
                }
            }
        });

        userSettings = findViewById(R.id.userSettings);
        userSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getUid() == null) {
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                    i.putExtra("user", "customer");
                    startActivity(i);
                }
            }
        });

        userOrders = findViewById(R.id.userOrders);
        userOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyOrdersActivity.class);
                i.putExtra("userType", "user");
                startActivity(i);
            }
        });

        userCS = findViewById(R.id.userCS);
        userCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:9324226748"));
                    startActivity(intent);
                }
            }
        });

        matMain = findViewById(R.id.matMain);
        matMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, MatMainActivity.class).putExtra("userid",firebaseAuth.getUid());
                startActivity(i);
            }
        });

        chatMain = findViewById(R.id.chatMain);
        chatMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ChatMainActivity.class);
                startActivity(i);
            }
        });

        callMain = findViewById(R.id.callMain);
        callMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                FcmNotificationsSender fns = new FcmNotificationsSender("/topics/new","title","sahil hii",getApplicationContext(),MainActivity.this);
//
//                fns.SendNotifications();
//

                Intent i = new Intent(MainActivity.this, CallMainActivity.class);
                startActivity(i);
            }
        });

        final Dialog updateDialog = new Dialog(MainActivity.this);
        updateDialog.setContentView(R.layout.edit_phone_number);
        updateDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        updateDialog.setCancelable(true);

        final EditText username = updateDialog.findViewById(R.id.username);
        final EditText gender = updateDialog.findViewById(R.id.gender);
        final EditText dateOfBirth = updateDialog.findViewById(R.id.dateOfBirth);
        final EditText timeOfBirth = updateDialog.findViewById(R.id.timeOfBirth);
        final EditText placeOfBirth = updateDialog.findViewById(R.id.placeOfBirth);
        final EditText marriedStatus = updateDialog.findViewById(R.id.marriedStatus);
        final EditText occupation = updateDialog.findViewById(R.id.occupation);

        final EditText phone = updateDialog.findViewById(R.id.updatePhoneET);
        final EditText email = updateDialog.findViewById(R.id.updateEmailET);
        final Button updateBtn = updateDialog.findViewById(R.id.updatePhoneBtn);
        final Map<String, Object> updatePhone = new HashMap<>();

        userAccount = findViewById(R.id.userAccount);
        userAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.show();
                firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                number = snapshot.child("phoneNo").getValue().toString();

                                if(snapshot.child("MaritalStatus").getValue() != null) {
                                    phone.setText(number);
                                    email.setText(snapshot.child("email").getValue().toString());
                                    username.setText(snapshot.child("name").getValue().toString());
                                    gender.setText(snapshot.child("Gender").getValue().toString());
                                    dateOfBirth.setText(snapshot.child("DOB").getValue().toString());
                                    timeOfBirth.setText(snapshot.child("TOB").getValue().toString());
                                    placeOfBirth.setText(snapshot.child("POB").getValue().toString());
                                    marriedStatus.setText(snapshot.child("MaritalStatus").getValue().toString());
                                    occupation.setText(snapshot.child("Occupation").getValue().toString());

                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "you havent done any activity at" , Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (phone.getText().toString().equals(number)) {
                            Toast.makeText(MainActivity.this, "Please, Edit the previous phone number!", Toast.LENGTH_SHORT).show();
                        } else {
                            updatePhone.put("phoneNo", phone.getText().toString());
                            firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).updateChildren(updatePhone);
                            updateDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Phone Number Updated Successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void setData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                astrologerModelListOffline.clear();
                astrologerModelListOnline.clear();
                joinedList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("chatOnline").equals("Offline")) {
                        AstrologerModel model = snapshot1.getValue(AstrologerModel.class);
                        astrologerModelListOffline.add(model);
                    } else {
                        AstrologerModel model = snapshot1.getValue(AstrologerModel.class);
                        astrologerModelListOnline.add(model);
                    }
                }


                joinedList.addAll(astrologerModelListOnline);
                joinedList.addAll(astrologerModelListOffline);

                astrologerRV.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                Collections.shuffle(joinedList);
                astrologerAdapter = new AstrologerAdapter(joinedList);
                astrologerRV.setAdapter(astrologerAdapter);
                astrologerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setRating(final int starPos, LinearLayout rate_now_container) {
        for (int x = 0; x < rate_now_container.getChildCount(); x++) {
            ImageView starView = (ImageView) rate_now_container.getChildAt(x);
            starView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPos) {
                starView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFBB00")));
            }
        }
    }




}