package com.krash.devguruuastro.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.krash.devguruuastro.Adapters.AstrologerAdapter;
import com.krash.devguruuastro.Adapters.PhotoListAdapte;
import com.krash.devguruuastro.Adapters.ReviewAdapter;
import com.krash.devguruuastro.Models.AstOrder;
import com.krash.devguruuastro.Models.AstrologerModel;
import com.krash.devguruuastro.Models.FcmNotificationsSender;
import com.krash.devguruuastro.Models.ReviewModel;
import com.krash.devguruuastro.Models.SystemTools;
import com.krash.devguruuastro.R;
import com.krash.devguruuastro.databinding.CallLayoutBinding;
import com.krash.devguruuastro.databinding.LowBalanceDialogBinding;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class AstrologerDetailsActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Map<String, Object> statusMap;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    String uid, imageURL, phone;
    Dialog dialog1, dialog;
    AlertDialog.Builder builder;
    Dialog callDialog;
    Call callAst;
    LinearLayout btnLayout;
    SinchClient sinchClient;
    ValueEventListener valueEventListener, listener;
    int astPrice, astCallPrice, balance, flag, usedDuration, astBal;
    String username;
    long remainedSecs;
    LowBalanceDialogBinding binding;
    CallLayoutBinding callBinding;
    int duration;
    AstrologerModel model;
    RecyclerView recyclerView;
    ArrayList<ReviewModel> reviews;
    ReviewAdapter reviewAdapter;
    String userType;
    private TextView name, degree, exp, lang, chat, call, report, bio, rating, busy, astOff;
    private ImageView profileImage, pBtn;
    private Button chatBtn, callBtn;
    RecyclerView photo_count_recycler_view;
    PhotoListAdapte photoListAdapte;


    long lastCallMins = 0;
    long lastChatMins = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astrologer_details);

        photo_count_recycler_view = findViewById(R.id.photoscount);
        name = findViewById(R.id.detailsName);
        busy = findViewById(R.id.busy);
        degree = findViewById(R.id.detailsDegree);
        exp = findViewById(R.id.detailsExp);
        lang = findViewById(R.id.detailsLang);
        chat = findViewById(R.id.chatMins);
        call = findViewById(R.id.callMins);
        report = findViewById(R.id.reportTV);
        chatBtn = findViewById(R.id.chatBtnDetails);
        callBtn = findViewById(R.id.callBtnDetails);
        bio = findViewById(R.id.longBioDetails);
        rating = findViewById(R.id.rate_average_rating);
        profileImage = findViewById(R.id.detailsImage);
        pBtn = findViewById(R.id.pBtn);
        astOff = findViewById(R.id.astOffTV);
        btnLayout = findViewById(R.id.btnLayout);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        statusMap = new HashMap<>();

        Intent i = getIntent();
        uid = i.getStringExtra("uid");
        userType = i.getStringExtra("userType");

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        reviews = new ArrayList<>();

        if (userType.equals("ast")) {
            btnLayout.setVisibility(View.GONE);
            uid = FirebaseAuth.getInstance().getUid();
            reviewAdapter = new ReviewAdapter(this, reviews, "ast");
        } else {
            checkBalance();
            requestAudioPermissions();
            reviewAdapter = new ReviewAdapter(this, reviews, "user");
        }

        recyclerView = findViewById(R.id.reviewRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();

        firebaseDatabase.getReference().child("Astrologers").child(uid).child("Reviews")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reviews.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            ReviewModel reviewModel = snapshot1.getValue(ReviewModel.class);
                            reviews.add(reviewModel);
                        }
                        reviewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });



        sinchClient = Sinch.getSinchClientBuilder()
                .context(AstrologerDetailsActivity.this)
                .userId(FirebaseAuth.getInstance().getUid())
                .applicationKey("6f2b7acf-c28f-4d61-bd44-ec5abd2c95a8")
                .applicationSecret("8h4s41t9XkuxfCB65CuD3A==")
                .environmentHost("clientapi.sinch.com")
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        binding = LowBalanceDialogBinding.inflate(getLayoutInflater());
        dialog1 = new Dialog(AstrologerDetailsActivity.this);
        dialog1.setContentView(binding.getRoot());
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.setCancelable(true);
        dialog1.getWindow().setBackgroundDrawable(getDrawable(R.drawable.border_back));

        builder = new AlertDialog.Builder(AstrologerDetailsActivity.this);
        builder.setTitle("Devguruuastro");
        builder.setCancelable(true);

        callBinding = CallLayoutBinding.inflate(getLayoutInflater());
        callDialog = new Dialog(AstrologerDetailsActivity.this);
        callDialog.setContentView(callBinding.getRoot());
        callDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        callDialog.setCancelable(false);

        databaseReference = firebaseDatabase.getReference().child("Astrologers").child(uid);

        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    model = snapshot.getValue(AstrologerModel.class);
                    Glide.with(AstrologerDetailsActivity.this).load(model.getProfileURL()).into(profileImage);
                    name.setText(model.getName());
                    if (model.getIsBusy().equals("true")) {
                        busy.setText("Busy");
                        busy.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    } else if (model.getCallOnline().equals("Online") || model.getChatOnline().equals("Online")) {
                        busy.setText("Free to interact");
                        busy.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    } else {
                        busy.setText("Offline");
                        busy.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                    }

                    if (model.getChatOnline().equals("Online")) {
                        astOff.setVisibility(View.GONE);
                        chatBtn.setVisibility(View.VISIBLE);
                        chatBtn.setBackgroundColor(Color.rgb(0,153,51));

                    } else if (model.getChatOnline().equals("Offline")) {
                        astOff.setVisibility(View.GONE);
                        chatBtn.setVisibility(View.VISIBLE);
                        chatBtn.setBackgroundColor(Color.rgb(194,194,194));
                    }

                    if (model.getCallOnline().equals("Online")) {
                        astOff.setVisibility(View.GONE);
                        callBtn.setVisibility(View.VISIBLE);
                        callBtn.setBackgroundColor(Color.rgb(0,153,51));
                    } else if (model.getCallOnline().equals("Offline")) {
                        astOff.setVisibility(View.GONE);
                        callBtn.setVisibility(View.VISIBLE);
                        callBtn.setBackgroundColor(Color.rgb(194,194,194));
                    }

                    if (model.getIsBusy().equalsIgnoreCase("true"))
                    {
                        astOff.setVisibility(View.GONE);
                        callBtn.setBackgroundColor(Color.rgb(255,161,34));
                        chatBtn.setBackgroundColor(Color.rgb(255,161,34));
                    }

                    if (model.getChatOnline().equals("Offline") && model.getCallOnline().equals("Offline")) {
                        astOff.setVisibility(View.GONE);
                    }

                    degree.setText(model.getBachD() + ", " + model.getMastD());
                    lang.setText(model.getLanguage());
                    exp.setText(model.getExperience() + " years");
                    chat.setText(model.getChatMins() + " mins");
                    call.setText(model.getCallMins() + " mins");
                    report.setText(model.getReports() + " report");
                    bio.setText(model.getLongB());
                    imageURL = model.getProfileURL();
                    chatBtn.setText("Chat (₹" + model.getChatPrice() + "/min)");
                    callBtn.setText("Call (₹" + model.getCallPrice() + "/min)");
                    astPrice = Integer.parseInt(model.getChatPrice());
                    astCallPrice = Integer.parseInt(model.getCallPrice());
                    phone = model.getMobile();
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AstrologerDetailsActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(model.getChatOnline().equalsIgnoreCase("Online") && model.getIsBusy().equalsIgnoreCase("false")) {
                    if (isAstrologerOnline() == 1) {
                        if (balance < (astPrice * 5)) {
                            binding.currentBalance.setText("Current Balance: ₹" + balance);
                            binding.textView36.setText("Minimum Wallet Balance required to talk is ₹" + (astPrice * 5) + ", Please recharge your wallet.");
                            binding.button4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(AstrologerDetailsActivity.this, WalletActivity.class);
                                    dialog1.dismiss();
                                    startActivity(i);
                                    finish();
                                }
                            });
                            dialog1.show();
                        } else {
                            Intent i = new Intent(AstrologerDetailsActivity.this, PersonalInfoActivity.class);
                            i.putExtra("name", name.getText().toString());
                            i.putExtra("uid", uid);
                            i.putExtra("type", ""+userType);
                            i.putExtra("astPrice", astPrice + "");
                            i.putExtra("imageURL", imageURL);
                            startActivity(i);
                            finish();
                        }
                        ref.removeEventListener(valueEventListener);
                    }
                }
                else
                {
                    Toast.makeText(AstrologerDetailsActivity.this, "he is offline", Toast.LENGTH_SHORT).show();
                }
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getCallOnline().equalsIgnoreCase("online") && model.getIsBusy().equalsIgnoreCase("false")) {
                    if ((balance >= 0) && (balance < (astCallPrice * 5))) {
                        binding.currentBalance.setText("Current Balance: ₹" + balance);
                        binding.textView36.setText("Minimum Wallet Balance required to talk is ₹" + (astCallPrice * 5) + ", Please recharge your wallet.");
                        binding.button4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(AstrologerDetailsActivity.this, WalletActivity.class);
                                dialog1.dismiss();
                                startActivity(i);
                                finish();
                            }
                        });
                        dialog1.show();
                    } else {
//                        final Dialog dialog = new Dialog(AstrologerDetailsActivity.this);
//                        dialog.setContentView(R.layout.call_details);
//                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        dialog.setCancelable(true);
//                        final TextView qty = dialog.findViewById(R.id.calldetails);
//                        qty.setText("We will take your " + Integer.parseInt(String.valueOf(balance / Integer.parseInt(model.getCallPrice()))) + " min");
//                        final Button submitBtn = dialog.findViewById(R.id.button);
                        ref = firebaseDatabase.getReference().child("Astrologers").child(uid);
                        listener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if ((snapshot.child("callOnline").getValue().equals("Online")) && (!snapshot.child("isBusy").getValue().toString().equals("true"))) {
                                    callAstrologer();
                                } else {
                                    Toast.makeText(AstrologerDetailsActivity.this, "Astrologer is Busy or Offline! Try Again later!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        };

                        if (Integer.parseInt(String.valueOf(balance / Integer.parseInt(model.getCallPrice()))) > 4) {
                           // dialog.dismiss();
                            duration = Integer.parseInt(String.valueOf(balance / Integer.parseInt(model.getCallPrice())));
                            ref.removeEventListener(listener);
                            ref.addValueEventListener(listener);
                        } else {
                            Toast.makeText(AstrologerDetailsActivity.this, "Duration should be atleast 5 mins!", Toast.LENGTH_LONG).show();
                        }
//
//                        submitBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (Integer.parseInt(String.valueOf(balance / Integer.parseInt(model.getCallPrice()))) > 4) {
//                                    dialog.dismiss();
//                                    duration = Integer.parseInt(String.valueOf(balance / Integer.parseInt(model.getCallPrice())));
//                                    ref.removeEventListener(listener);
//                                    ref.addValueEventListener(listener);
//                                } else {
//                                    Toast.makeText(AstrologerDetailsActivity.this, "Duration should be atleast 5 mins!", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });
//                        dialog.show();
                    }
                }
                else {
                    if (model.getIsBusy().equalsIgnoreCase("true"))
                    {
                        Toast.makeText(AstrologerDetailsActivity.this, "He is busy", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(AstrologerDetailsActivity.this, "He is offline", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        getListOfPhotos();
        getCallAndChatDuration();
    }

    private void callAstrologer() {

        final int[] isEstablish = {0};

        final Dialog call_dialog = new Dialog(this);
        call_dialog.setContentView(R.layout.call_dialog);
        call_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        call_dialog.setCancelable(true);

        call_dialog.show();



        FcmNotificationsSender fns = new FcmNotificationsSender(model.getToken(),"Call","You have Call from "+username,getApplicationContext(),AstrologerDetailsActivity.this);
        fns.SendNotifications();

        ref.removeEventListener(listener);
        callAst = sinchClient.getCallClient().callUser(uid);
        callBinding.astCallEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.dismiss();
                callAst.hangup();
            }
        });

        callBinding.astCallName.setText(name.getText().toString());
        Glide.with(AstrologerDetailsActivity.this).load(imageURL).into(callBinding.astCallImg);

        callAst.addCallListener(new CallListener() {
            @Override
            public void onCallProgressing(Call call) {

                Toast.makeText(AstrologerDetailsActivity.this, "Ringing...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCallEstablished(Call call) {
                isEstablish[0] = 1;
                call_dialog.dismiss();
                Toast.makeText(AstrologerDetailsActivity.this, "Call Established!", Toast.LENGTH_SHORT).show();
                callDialog.show();
                duration = duration * 60000;
                startTimer(duration, 1000);
            }

            @Override
            public void onCallEnded(Call endedCall) {
                Toast.makeText(AstrologerDetailsActivity.this, "Call Ended!", Toast.LENGTH_SHORT).show();
                call = null;
                callDialog.dismiss();
                endedCall.hangup();

                call_dialog.dismiss();
                if(isEstablish[0] == 1)
                {
                    callEnded();
                }
            }

            @Override
            public void onShouldSendPushNotification(Call call, List<PushPair> list) {
          //      ..

//                Toast.makeText(AstrologerDetailsActivity.this, "noti", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private int isAstrologerOnline() {
        ref = firebaseDatabase.getReference().child("Astrologers").child(uid);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String isBusy = snapshot.child("isBusy").getValue().toString();
                String isOnline = snapshot.child("chatOnline").getValue().toString();
                if (isOnline.equals("Offline")) {
                    builder.setMessage("Astrologer is Offline! Try Again Later!");
                    dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.border_back));
                    if (!AstrologerDetailsActivity.this.isFinishing()) {
                        dialog.show();
                    }
                    flag = 0;
                } else if (isBusy.equals("true")) {
                    builder.setMessage("Astrologer is Busy! Please Wait!");
                    dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.border_back));
                    if (!AstrologerDetailsActivity.this.isFinishing()) {
                        dialog.show();
                    }
                    flag = 0;
                } else {
                    flag = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        ref.addValueEventListener(valueEventListener);
        return flag;
    }

    private void checkBalance() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        balance = Integer.parseInt(snapshot.child("balance").getValue().toString());
                        username = snapshot.child("name").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userType == "user") {
            checkBalance();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ref != null && valueEventListener != null) {
            ref.removeEventListener(valueEventListener);
        }
    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void startTimer(final long finish, long tick) {
        CountDownTimer t;
        t = new CountDownTimer(finish, tick) {
            public void onTick(long millisUntilFinished) {
                remainedSecs = millisUntilFinished / 1000;
                callBinding.astCallDuration.setText("Time Left: " + (remainedSecs / 60) + ":" + (remainedSecs % 60));
            }

            public void onFinish() {
                callEnded();
            }
        }.start();
    }

    private void callEnded() {
        usedDuration = duration / 60000;
        try {
            usedDuration = (int) ((duration / 60000) - (remainedSecs / 60));
        } catch (Exception e) {
        }

        statusMap.put("balance", (balance - (usedDuration * astCallPrice)) + "");
        firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(statusMap);

        Map<String, Object> userMap = new HashMap<>();
        Map<String, Object> astMap = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String orderID = UUID.randomUUID().toString().substring(0, 21);
        userMap.put("AstUID", uid);
        userMap.put("orderID", orderID);
        userMap.put("name", name.getText().toString());
        userMap.put("orderType", "Call");
        userMap.put("orderDate", SystemTools.getCurrent_date());
        userMap.put("orderTime", SystemTools.getCurrent_time());
        userMap.put("duration", ((duration / 60000) - (remainedSecs / 60)) + "");
        userMap.put("expense", ((usedDuration * astCallPrice)) + "");
        firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Orders").child(orderID).setValue(userMap);

        astMap.put("UserUID", FirebaseAuth.getInstance().getUid());
        astMap.put("orderID", orderID);
        astMap.put("orderType", "Call");
        astMap.put("name", name.getText().toString());
        astMap.put("orderDate", SystemTools.getCurrent_date());
        astMap.put("orderTime", SystemTools.getCurrent_time());
        astMap.put("duration", ((duration / 60000) - (remainedSecs / 60)) + "");
        astMap.put("earning", ((usedDuration * astCallPrice) / 2) + "");

        double total = ((usedDuration * astCallPrice) / 2);
        double gateway = (double) ((1.25/100)*total);
        astMap.put("geteway",""+gateway);
        double tds = (double) ((7.5/100)*total);
        astMap.put("tds",""+tds);
        double apayment = total - tds - gateway;
        astMap.put("apayment",""+apayment);


        firebaseDatabase.getReference().child("Astrologers").child(uid).child("Orders").child(orderID).setValue(astMap);
        firebaseDatabase.getReference().child("Astrologers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                astBal = Integer.parseInt(snapshot.child("balance").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        firebaseDatabase.getReference().child("Astrologers").child(uid).child("balance").setValue((astBal + ((usedDuration * astCallPrice) / 2)) + "");

      //  Intent i = new Intent(AstrologerDetailsActivity.this, MainActivity.class);
        //startActivity(i);
        finish();
    }

    List<String> photolList;
    List<String> keyList;
    void getListOfPhotos()
    {
        photolList = new ArrayList<>();
        keyList = new ArrayList<>();
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Astrologers").child(FirebaseAuth.getInstance().getUid()).child("Photos");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                photolList.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
//                    Toast.makeText(AstrologerDetailsActivity.this, "", Toast.LENGTH_SHORT).show();
                    String pl = ds.getValue(String.class);
                    photolList.add(pl);
                    keyList.add(ds.getKey());
                    //Toast.makeText(AstrologerDetailsActivity.this, pl, Toast.LENGTH_SHORT).show();
                }


                photo_count_recycler_view.setLayoutManager(new GridLayoutManager(AstrologerDetailsActivity.this, 3));

                if(userType.equalsIgnoreCase("ast"))
                {
                    photoListAdapte = new PhotoListAdapte(getApplicationContext(), photolList,keyList, uid,"ast");
                }
                else
                {
                    photoListAdapte = new PhotoListAdapte(getApplicationContext(), photolList,keyList, uid,"usr");
                }
                photo_count_recycler_view.setAdapter(photoListAdapte);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getCallAndChatDuration()
    {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Astrologers").child(uid).child("Orders");

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    try{
                        AstOrder ao = ds.getValue(AstOrder.class);
                        if(ao.getOrderType().equalsIgnoreCase("call")) {
                            lastCallMins = lastCallMins + Long.parseLong(ao.getDuration());
                        }
                        else {
                            lastChatMins = lastChatMins + Long.parseLong(ao.getDuration());
                        }
                     //   Toast.makeText(AstrologerDetailsActivity.this, ""+lastCallMins, Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception ex)
                    {
                      //  Toast.makeText(AstrologerDetailsActivity.this, "oredes are not placed proper.", Toast.LENGTH_SHORT).show();
                    }
                }

                chat.setText(String.valueOf(lastChatMins) + " mins");
                call.setText(String .valueOf(lastCallMins) + " mins");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
