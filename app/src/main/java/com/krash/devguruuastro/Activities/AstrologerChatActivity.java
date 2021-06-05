package com.krash.devguruuastro.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.krash.devguruuastro.Adapters.MessagesAdapter;
import com.krash.devguruuastro.Adapters.UserMessageAdapter;
import com.krash.devguruuastro.Models.Message;
import com.krash.devguruuastro.Models.Messages;
import com.krash.devguruuastro.Models.SystemTools;
import com.krash.devguruuastro.Models.User;
import com.krash.devguruuastro.R;
import com.krash.devguruuastro.databinding.ActivityChatBinding;
import com.krash.devguruuastro.databinding.UserInfoLayoutBinding;
import com.sinch.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class AstrologerChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    UserInfoLayoutBinding userInfoLayoutBinding;
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
    CountDownTimer t;
    private TextView textTimer;

    EditText msgBox;

    User user;


    RecyclerView chatShowRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astrologer_chat);


        msgBox = findViewById(R.id.msgBox);


        builder = new AlertDialog.Builder(this);
        textTimer = findViewById(R.id.duration);

        progressDialog = new ProgressDialog(AstrologerChatActivity.this);

        firebaseStorage = FirebaseStorage.getInstance();

        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        Gson gson = new Gson();


        user = gson.fromJson(getIntent().getStringExtra("myjson"),User.class);

        //Toast.makeText(this, ""+user.getRc().getStatus()  , Toast.LENGTH_SHORT).show();

        long dur = Long.parseLong(user.getRc().getDuration());

        dur = dur * 60000;

        startTimer(dur,1000);

        messages = new ArrayList<>();
        chatShowRV = findViewById(R.id.chatShowRV);

        adapter = new UserMessageAdapter(this, messages,"ast");
        chatShowRV.setLayoutManager(new LinearLayoutManager(this));
        chatShowRV.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference("RequestQueue").child(user.getRc().getAstrologerid()).child(user.getRc().getRequestid()).child("status").setValue("accept");

        getChatMessage();


        findViewById(R.id.attachBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);
            }
        });

        findViewById(R.id.endBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Astrologers").child(user.getRc().getAstrologerid()).child("isBusy").setValue("true");

        checkRequestStatus();
    }

    String isFinish = "no";

    @Override
    public void onBackPressed() {
        if(isFinish.equalsIgnoreCase("yes"))
        {
            endChat();
        }
        else {
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


    public void startTimer(final long finish, long tick) {
        t = new CountDownTimer(finish, tick) {
            public void onTick(long millisUntilFinished) {
                remainedSecs = millisUntilFinished / 1000;
                textTimer.setText("" + (remainedSecs / 60) + ":" + (remainedSecs % 60));
            }

            public void onFinish() {
                isFinish = "yes";
                endChat();

                cancel();
            }
        }.start();
    }

    void sendMessage()
    {
        if(!msgBox.getText().toString().equalsIgnoreCase("")) {
            DatabaseReference sendref = FirebaseDatabase.getInstance().getReference().child("Chat").child(user.getRc().getSessionid()).child("message");

            String messageid = sendref.push().getKey();
            Messages ms = new Messages(messageid, user.getRc().getAstrologerid(), user.getRc().getUserid(),"ast", "msg", msgBox.getText().toString(), "url", SystemTools.getCurrent_date(), SystemTools.getCurrent_time(), "true");

            sendref.child(messageid).setValue(ms);
            msgBox.setText("");
        }
        else
        {
            Toast.makeText(this, "Enter something", Toast.LENGTH_SHORT).show();
        }


    }


    void getChatMessage()
    {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Chat").child(user.getRc().getSessionid()).child("message");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messages.clear();
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    Messages message = ds.getValue(Messages.class);
                    messages.add(message);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

                    final DatabaseReference sendref = FirebaseDatabase.getInstance().getReference().child("Chat").child(user.getRc().getSessionid()).child("message");

                    final String messageid = sendref.push().getKey();
                    final StorageReference reference = firebaseStorage.getReference().child("Chat").child(user.getRc().getSessionid()).child(messageid);
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


                                        Messages ms = new Messages(messageid, user.getRc().getAstrologerid(), user.getRc().getUserid(),"ast", "image", msgBox.getText().toString(), filePath, SystemTools.getCurrent_date(), SystemTools.getCurrent_time(), "true");

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


    void endChat()
    {
        Toast.makeText(this, "finish chat", Toast.LENGTH_SHORT).show();

        FirebaseDatabase.getInstance().getReference("RequestQueue").child(user.getRc().getAstrologerid()).child(user.getRc().getRequestid()).child("status").setValue("done");
        finishAffinity();
        startActivity(new Intent(getApplicationContext(),AstrologerMainActivity.class));


    }


    void checkRequestStatus()
    {
        FirebaseDatabase.getInstance().getReference("RequestQueue").child(user.getRc().getAstrologerid()).child(user.getRc().getRequestid()).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue(String.class).equalsIgnoreCase("userdone"))
                {
                    finishAffinity();
                    startActivity(new Intent(getApplicationContext(),AstrologerMainActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}