package com.krash.devguruuastro.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.krash.devguruuastro.Models.AstOrder;
import com.krash.devguruuastro.Models.FcmNotificationsSender;
import com.krash.devguruuastro.Models.SystemTools;
import com.krash.devguruuastro.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AstrologerMainActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    long earning = 0;
    long todayEarning = 0;
    FirebaseAuth firebaseAuth;
    Map<String, Object> updateStatus;
    String uid;
    ToggleButton chatTB, callTB;
    private ConstraintLayout chatCL, callCL;
    private TextView earningTV, todayEarnTV;
    private ImageView setting, account, history, CS, viewAs;
    String username = "";
    LinearLayout linearLayout4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astrologer_main);


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic(firebaseAuth.getUid());
        updateStatus = new HashMap<>();
        uid = firebaseAuth.getUid();

        linearLayout4 = findViewById(R.id.linearLayout4);

        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDialog();
            }
        });

        final DatabaseReference tokendata = FirebaseDatabase.getInstance().getReference("Astrologers").child(firebaseAuth.getUid());

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful())
                {
                    String usertoken = Objects.requireNonNull(task.getResult()).getToken();

                    tokendata.child("token").setValue(usertoken);
                 //   Toast.makeText(AstrologerMainActivity.this, "jjjj  "+usertoken, Toast.LENGTH_SHORT).show();
                }
            }
        });

        getAstrologerData();

        earningTV = findViewById(R.id.totalEarn);
        todayEarnTV = findViewById(R.id.earnToday);

        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AstrologerMainActivity.this, SettingsActivity.class);
                i.putExtra("user", "astrologer");
                startActivity(i);
            }
        });

        CS = findViewById(R.id.astCS);
        CS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AstrologerMainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AstrologerMainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:9324226748"));
                    startActivity(intent);
                }
            }
        });

        account = findViewById(R.id.account);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AstrologerMainActivity.this, AstrologerAccountActivity.class);
                startActivity(i);
            }
        });

        history = findViewById(R.id.userHistory);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AstrologerMainActivity.this, MyOrdersActivity.class);
                i.putExtra("userType", "ast");
                startActivity(i);
            }
        });

        viewAs = findViewById(R.id.viewas);
        viewAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AstrologerMainActivity.this, AstrologerDetailsActivity.class);
                i.putExtra("userType", "ast");
                startActivity(i);
            }
        });

        chatCL = findViewById(R.id.chatAstCL);
        chatCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus.clear();
                //updateStatus.put("callOnline", "Offline");
                updateStatus.put("chatOnline", "Online");
                firebaseDatabase.getReference().child("Astrologers").child(firebaseAuth.getUid()).updateChildren(updateStatus);
                Intent i = new Intent(AstrologerMainActivity.this, UsersActivity.class);
                startActivity(i);
                finish();
            }
        });

        callCL = findViewById(R.id.callAstCL);
        callCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus.clear();
                updateStatus.put("callOnline", "Online");
//                updateStatus.put("chatOnline", "Offline");
                firebaseDatabase.getReference().child("Astrologers").child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(updateStatus);
                Intent i = new Intent(AstrologerMainActivity.this, AstrologerCallActivity.class);
                startActivity(i);
            }
        });

        chatTB = findViewById(R.id.chatTB);
        chatTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    updateStatus.clear();
                    if (isChecked) {
//                        FcmNotificationsSender fns = new FcmNotificationsSender("/topics/all",username,"Astrologer comes online for chat",getApplicationContext(),AstrologerMainActivity.this);
//
//                        fns.SendNotifications();
//                        updateStatus.put("chatOnline", "Online");
//                        updateStatus(updateStatus);
                        firebaseDatabase.getReference().child("Astrologers").child(Objects.requireNonNull(firebaseAuth.getUid())).child("chatOnline").setValue("Online");
                        chatTB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple)));
                    } else {
                        firebaseDatabase.getReference().child("Astrologers").child(Objects.requireNonNull(firebaseAuth.getUid())).child("chatOnline").setValue("Offline");
//                        updateStatus.put("chatOnline", "Offline");
//                        updateStatus(updateStatus);
                        chatTB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                    }
                } catch (Exception e) {
                    Toast.makeText(AstrologerMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        callTB = findViewById(R.id.callTB);
        callTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateStatus.clear();
                if (callTB.getText().toString().equals("Online")) {
//                    FcmNotificationsSender fns = new FcmNotificationsSender("/topics/all",username,"Astrologer comes online for voice call",getApplicationContext(),AstrologerMainActivity.this);
//
//                    fns.SendNotifications();
//                    updateStatus.put("callOnline", "Online");



                    firebaseDatabase.getReference().child("Astrologers").child(Objects.requireNonNull(firebaseAuth.getUid())).child("callOnline").setValue("Online");
                    callTB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple)));
                } else {
    //                updateStatus.put("callOnline", "Offline");
                    firebaseDatabase.getReference().child("Astrologers").child(Objects.requireNonNull(firebaseAuth.getUid())).child("callOnline").setValue("Offline");
                    callTB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                }
                firebaseDatabase.getReference().child("Astrologers").child(firebaseAuth.getUid()).updateChildren(updateStatus);
            }
        });
//
//        videoTB = findViewById(R.id.videoTB);
//        videoTB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (videoTB.getText().toString().equals("Online")) {
//
//
////                    FcmNotificationsSender fns = new FcmNotificationsSender("/topics/all",username,"Astrologer comes online for video call",getApplicationContext(),AstrologerMainActivity.this);
////
////                    fns.SendNotifications();
//
//
//                    videoTB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple)));
//                } else {
//
//                    videoTB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
//                }
//            }
//        });

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        final Date date = new Date();
        try {
            firebaseDatabase.getReference().child("Astrologers").child(firebaseAuth.getUid())
                    .child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        earning = earning + Long.parseLong(dataSnapshot.child("earning").getValue().toString());
                        if (dateFormat.format(date).equals(dataSnapshot.child("orderDate").getValue())) {
                            todayEarning = todayEarning + Long.parseLong(dataSnapshot.child("earning").getValue().toString());
                        }
                    }
                    earningTV.setText(earning + "");
                    todayEarnTV.setText(todayEarning + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } catch (Exception e) {
        }

        getOnlineOfflineData();
    }

    void updateStatus(Map<String, Object> map) {
        firebaseDatabase.getReference().child("Astrologers").child(uid).updateChildren(map);
    }


    void getAstrologerData()
    {
        DatabaseReference astrodata = FirebaseDatabase.getInstance().getReference("Astrologers").child(firebaseAuth.getUid()).child("name");
        astrodata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Toast.makeText(AstrologerMainActivity.this, ""+snapshot.getValue(), Toast.LENGTH_SHORT).show();
                username = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void showOrderDialog()
    {

        final Dialog total_orders_dialog = new Dialog(AstrologerMainActivity.this);
        total_orders_dialog.setContentView(R.layout.total_orders_dialog_item);
        total_orders_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        total_orders_dialog.setCancelable(true);

        total_orders_dialog.show();

        TextView actualpayment,actualtotal,taxtotal,tds,paymentgateway,timeperiod;
        actualpayment = total_orders_dialog.findViewById(R.id.actualpayment);
        actualtotal = total_orders_dialog.findViewById(R.id.actualtotal);
        taxtotal = total_orders_dialog.findViewById(R.id.taxtotal);
        tds = total_orders_dialog.findViewById(R.id.tds);
        paymentgateway = total_orders_dialog.findViewById(R.id.paymentgateway);
        timeperiod = total_orders_dialog.findViewById(R.id.timeperiod);

        getAstOrdersData(actualpayment,actualtotal,taxtotal,tds,paymentgateway,timeperiod);

        ImageButton cancel = total_orders_dialog.findViewById(R.id.cut_dialog);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_orders_dialog.cancel();
            }
        });


    }

    double actualPayment = 0,actual_total = 0,tax_total = 0,tds_total = 0,payment_gateway = 0;

    void getAstOrdersData(final TextView ap, final TextView at, final TextView tt, final TextView td, final TextView pg, final TextView tp)
    {
        final String[] smallDate = {SystemTools.getCurrent_date()};
        final String[] largeDate = {SystemTools.getCurrent_date()};
        DatabaseReference dref =  FirebaseDatabase.getInstance().getReference("Astrologers").child(firebaseAuth.getUid()).child("Orders");

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                actualPayment = 0;
                actual_total = 0;
                tax_total = 0;
                tds_total = 0;
                payment_gateway = 0;

                for(DataSnapshot ds: snapshot.getChildren())
                {
                    AstOrder ao = ds.getValue(AstOrder.class);

                    actualPayment = actualPayment + Double.parseDouble(ao.getApayment());
                    actual_total = actual_total + Double.parseDouble(ao.getEarning());
                    tax_total = tax_total + Double.parseDouble(ao.getTds()) + Double.parseDouble(ao.getGeteway());

                    tds_total = tds_total + Double.parseDouble(ao.getTds());
                    payment_gateway = payment_gateway + Double.parseDouble(ao.getGeteway());


                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                    try {
                        Date date1 = format.parse(ao.getOrderDate());
                        Date date2 = format.parse(smallDate[0]);
                       // Toast.makeText(AstrologerMainActivity.this, ""+date1.compareTo(date2), Toast.LENGTH_SHORT).show();

                        if(date1.compareTo(date2) == -1)
                        {
                            smallDate[0] = ao.getOrderDate();
                        }
                        else
                        {
                            largeDate[0] = ao.getOrderDate();
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(AstrologerMainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }


                ap.setText("+ "+String.valueOf(actualPayment));
                at.setText("+ "+String.valueOf(actual_total));
                tt.setText("- "+String.valueOf(tax_total));
                td.setText("- "+String.valueOf(tds_total));
                pg.setText("- "+String.valueOf(payment_gateway));
                tp.setText(smallDate[0]+" ~ "+largeDate[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getOnlineOfflineData()
    {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Astrologers").child(uid);

        dref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("snapsost",snapshot.child("c").getValue().toString());
                if(snapshot.child("chatOnline").getValue().toString().equalsIgnoreCase("online"))
                {
                    chatTB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple)));
                    chatTB.setChecked(true);
                }
                else
                {
                    chatTB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                    chatTB.setChecked(false);
                }

                if(snapshot.child("callOnline").getValue().toString().equalsIgnoreCase("online"))
                {
                    callTB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple)));
                    callTB.setChecked(true);
                }
                else
                {
                    callTB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                    callTB.setChecked(false);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}