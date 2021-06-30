package com.krash.devguruuastros.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.krash.devguruuastros.Adapters.UserMessageAdapter;
import com.krash.devguruuastros.Models.FcmNotificationsSender;
import com.krash.devguruuastros.Models.Messages;
import com.krash.devguruuastros.Models.RequestClass;
import com.krash.devguruuastros.Models.SystemTools;
import com.krash.devguruuastros.R;
import com.sinch.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserChatActivity extends AppCompatActivity {

    RequestClass rc;

    com.krash.devguruuastros.databinding.ActivityChatBinding binding;
    com.krash.devguruuastros.databinding.UserInfoLayoutBinding userInfoLayoutBinding;
    UserMessageAdapter adapter;
    String senderUid, receiverUid, imageURL;
    ArrayList<Messages> messages;
    String senderRoom, receiverRoom;
    FirebaseDatabase firebaseDatabase;
    Dialog userDialog;
    long remainedSecs;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    Dialog dialog, waitingDialog;
    long dur;
    Date date;
    int astPrice, bal, usedDuration, astBal, starPos;
    String userType, status, name;
    InputMethodManager imm;
    Map<String, Object> busy, statusMap;
    CountDownTimer t, t1;
    private TextView textTimer;

    EditText msgBox;


    RecyclerView chatShowRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        Gson gson = new Gson();

        textTimer = findViewById(R.id.duration);

        builder = new AlertDialog.Builder(this);

        progressDialog = new ProgressDialog(UserChatActivity.this);

        progressDialog.setCancelable(false);
        msgBox = findViewById(R.id.msgBox);


        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        firebaseStorage = FirebaseStorage.getInstance();

        rc = gson.fromJson(getIntent().getStringExtra("myjson"), RequestClass.class);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Chat").child(rc.getSessionid()).child("message");

        String mee = "name - " + getIntent().getStringExtra("name");
        String messageid = ref.push().getKey();
        Messages ms = new Messages(messageid, rc.getAstrologerid(), rc.getUserid(), "user", "msg", mee, "url", SystemTools.getCurrent_date(), SystemTools.getCurrent_time(), "true");

        ref.child(messageid).setValue(ms);

//        Toast.makeText(this, ""+rc.getStatus()  , Toast.LENGTH_SHORT).show();

        checkRequestStatus();

        messages = new ArrayList<>();
        chatShowRV = findViewById(R.id.chatShowRV);

        adapter = new UserMessageAdapter(this, messages, "user");
        chatShowRV.setLayoutManager(new LinearLayoutManager(this));
        chatShowRV.setAdapter(adapter);



        findViewById(R.id.attachBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);
            }
        });


        getChatMessage();
        fetchDataAstroData();


        findViewById(R.id.endBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 25) {
            if (data != null) {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();

                    final DatabaseReference sendref = FirebaseDatabase.getInstance().getReference().child("Chat").child(rc.getSessionid()).child("message");

                    final String messageid = sendref.push().getKey();
                    final StorageReference reference = firebaseStorage.getReference().child("Chat").child(rc.getSessionid()).child(messageid);
                    progressDialog.show();
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath = uri.toString();

                                        //                                      String messageTxt = binding.msgBox.getText().toString();
//                                        Date date = new Date();


                                        Messages ms = new Messages(messageid, rc.getAstrologerid(), rc.getUserid(), "user", "image", msgBox.getText().toString(), filePath, SystemTools.getCurrent_date(), SystemTools.getCurrent_time(), "true");

                                        sendref.child(messageid).setValue(ms);

                                        msgBox.setText("");
//                                        final Messages message = new Message(messageTxt, senderUid, receiverUid, date.getTime());
//                                        message.setMessage("Photo");
//                                        message.setImageURL(filePath);
//                                        binding.msgBox.setText("");

                                        sendref.child(messageid).setValue(ms).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {


//                                                firebaseDatabase.getReference().child("Chats")
//                                                        .child(receiverRoom)
//                                                        .child("Messages")
//                                                        .child(randomKey)
//                                                        .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void aVoid) {
//
//                                                    }
//                                                });
                                            }
                                        });

                                    }
                                });
                            } else {

                            }
                        }
                    });
                }
            }
        }
    }


    public void startTimer(final long finish, long tick) {
        t = new CountDownTimer(finish, tick) {
            public void onTick(long millisUntilFinished) {
                remainedSecs = millisUntilFinished / 1000;
                textTimer.setText("" + (remainedSecs / 60) + ":" + (remainedSecs % 60));
            }

            public void onFinish() {

                isFinish = "yes";
                onBackPressed();
                cancel();
            }
        }.start();
    }


    void checkRequestStatus() {


        progressDialog.setTitle("Astrologer havent accept request at");
        progressDialog.show();

        count2Min(60000,1000);

        FirebaseDatabase.getInstance().getReference("RequestQueue").child(rc.getAstrologerid()).child(rc.getRequestid()).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.getValue(String.class).equalsIgnoreCase("accept")) {
                    status = snapshot.getValue(String.class);
                    progressDialog.cancel();


                    long dur = Long.parseLong(rc.getDuration());

                    dur = dur * 60000;

                    startTimer(dur, 1000);
                    t1.cancel();

                } else if (snapshot.getValue(String.class).equalsIgnoreCase("cancel") || snapshot.getValue(String.class).equalsIgnoreCase("done")) {
                    endChat();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void sendMessage() {
        if (!msgBox.getText().toString().equalsIgnoreCase("")) {
            DatabaseReference sendref = FirebaseDatabase.getInstance().getReference().child("Chat").child(rc.getSessionid()).child("message");

            String messageid = sendref.push().getKey();
            Messages ms = new Messages(messageid, rc.getAstrologerid(), rc.getUserid(), "user", "msg", msgBox.getText().toString(), "url", SystemTools.getCurrent_date(), SystemTools.getCurrent_time(), "true");

            sendref.child(messageid).setValue(ms);

            msgBox.setText("");
        } else {
            Toast.makeText(this, "Enter something", Toast.LENGTH_SHORT).show();
        }


    }


    void getChatMessage() {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Chat").child(rc.getSessionid()).child("message");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messages.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Messages message = ds.getValue(Messages.class);
                    messages.add(message);


                    chatShowRV.scrollToPosition(messages.size()-1);

                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void endChat() {
        if (isFinish.equalsIgnoreCase("no")) {
            FirebaseDatabase.getInstance().getReference("RequestQueue").child(rc.getAstrologerid()).child(rc.getRequestid()).child("status").setValue("userdone");
        }
        Toast.makeText(this, "finish chat", Toast.LENGTH_SHORT).show();
        int duration = Integer.parseInt(rc.getDuration());

        int astPrice = Integer.parseInt(getIntent().getStringExtra("astPrice"));

        int balance = Integer.parseInt(getIntent().getStringExtra("balance"));


        balance = (int) ((balance - ((duration - (int) (remainedSecs / 60)) * astPrice)));

        //Toast.makeText(this, ""+balance, Toast.LENGTH_LONG).show();

        FirebaseDatabase.getInstance().getReference().child("Users").child(rc.getUserid()).child("balance").setValue("" + balance);
        FirebaseDatabase.getInstance().getReference().child("Astrologers").child(rc.getAstrologerid()).child("isBusy").setValue("false");

        Map<String, Object> astMap = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();


        userMap.put("AstUID", rc.getAstrologerid());
        userMap.put("orderID", rc.getSessionid());
        userMap.put("name", getIntent().getStringExtra("name"));
        astMap.put("username",getIntent().getStringExtra("username"));
        userMap.put("orderDate", SystemTools.getCurrent_date());
        userMap.put("orderTime", SystemTools.getCurrent_time());
        userMap.put("duration", "" + (duration - (int) (remainedSecs / 60)));
        userMap.put("expense", "" + ((duration - (int) (remainedSecs / 60)) * astPrice));
        userMap.put("orderType", "Chat");

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Orders").child(rc.getSessionid()).setValue(userMap);

        astMap.put("UserUID", FirebaseAuth.getInstance().getUid());
        astMap.put("orderID", rc.getSessionid());
        astMap.put("name", getIntent().getStringExtra("name"));
        astMap.put("username",getIntent().getStringExtra("username"));
        astMap.put("orderDate", SystemTools.getCurrent_date());
        astMap.put("orderTime", SystemTools.getCurrent_time());
        astMap.put("duration", "" + (duration - (int) (remainedSecs / 60)));
        astMap.put("earning", "" + ((double)((duration - (int) (remainedSecs / 60)) * astPrice) / 2));
        astMap.put("userpaid", "" + ((duration - (int) (remainedSecs / 60)) * astPrice));
        astMap.put("orderType", "Chat");

        double total = ((double)((duration - (int) (remainedSecs / 60)) * astPrice) / 2);
        double gateway = (double) ((1.50 / 100) * total);
        astMap.put("geteway", "" + gateway);
        //double tds = (double) ((7.5 / 100) * total);
        double tds = 0;
        astMap.put("tds", "" + tds);
        double apayment = total - tds - gateway;
        astMap.put("apayment", "" + apayment);


        FirebaseDatabase.getInstance().getReference().child("Astrologers").child(rc.getAstrologerid()).child("Orders").child(rc.getSessionid()).setValue(astMap);


        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("comesfrom","userchat").putExtra("name",rc.getUsername()).putExtra("astid",rc.getAstrologerid()));
        finishAffinity();


    }

    String isFinish = "no";

    @Override
    public void onBackPressed() {
        if (isFinish.equalsIgnoreCase("yes")) {
            endChat();
        } else {
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                FirebaseDatabase.getInstance().getReference("RequestQueue").child(user.getRc().getAstrologerid()).child(user.getRc().getRequestid()).child("status").setValue("done").addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(AstrologerChatActivity.this, "Chat done succesfully", Toast.LENGTH_SHORT).show();
//                    }
//                });
                    endChat();
                }
            });
            builder.setMessage("Are you sure you want to end the chat?");
            dialog = builder.create();
            dialog.show();

        }
    }

    void fetchDataAstroData() {

        DatabaseReference astrodata = FirebaseDatabase.getInstance().getReference("Astrologers").child(rc.getAstrologerid()).child("token");
        astrodata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("astroid", snapshot.getValue().toString());
                FcmNotificationsSender fns = new FcmNotificationsSender(snapshot.getValue().toString(), "chat", "you have chat invitation","USERSACTIVITY", getApplicationContext(), UserChatActivity.this);

                fns.SendNotifications();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    String isTickFinish = "false";

    void count2Min(final long finish, long tick) {
        t1 = new CountDownTimer(finish, tick) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {



                FirebaseDatabase.getInstance().getReference("RequestQueue").child(rc.getAstrologerid()).child(rc.getRequestid()).child("status").setValue("expired");

                Toast.makeText(UserChatActivity.this, "Client is not connected", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("comesfrom","expired"));
                finishAffinity();

                cancel();
            }
        }.start();
    }
}