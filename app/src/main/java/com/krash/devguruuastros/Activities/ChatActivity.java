package com.krash.devguruuastros.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
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
import com.krash.devguruuastros.Adapters.MessagesAdapter;
import com.krash.devguruuastros.Models.FcmNotificationsSender;
import com.krash.devguruuastros.Models.Message;
import com.krash.devguruuastros.Models.SystemTools;
import com.krash.devguruuastros.R;
import com.krash.devguruuastros.databinding.ActivityChatBinding;
import com.krash.devguruuastros.databinding.UserInfoLayoutBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    UserInfoLayoutBinding userInfoLayoutBinding;
    MessagesAdapter adapter;
    String senderUid, receiverUid, imageURL;
    ArrayList<Message> messages;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchDataAstroData(getIntent().getStringExtra("uid"));

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        userInfoLayoutBinding = UserInfoLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        date = new Date();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Devguruuastro");

        messages = new ArrayList<>();
        busy = new HashMap<>();
        statusMap = new HashMap<>();
        adapter = new MessagesAdapter(this, messages);
        binding.chatShowRV.setLayoutManager(new LinearLayoutManager(this));
        binding.chatShowRV.setAdapter(adapter);
        textTimer = findViewById(R.id.duration);

        progressDialog = new ProgressDialog(ChatActivity.this);
        userDialog = new Dialog(ChatActivity.this);
        userDialog.setContentView(userInfoLayoutBinding.getRoot());
        userDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        userDialog.setCancelable(true);
        waitingDialog = new Dialog(ChatActivity.this);
        waitingDialog.setContentView(R.layout.waiting_dialog);
        waitingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.border_back));
        waitingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        waitingDialog.setCancelable(false);

        name = getIntent().getStringExtra("name");
        receiverUid = getIntent().getStringExtra("uid");
        imageURL = getIntent().getStringExtra("imageURL");
        userType = getIntent().getStringExtra("type");
        status = getIntent().getStringExtra("status");
        String myUID = getIntent().getStringExtra("myUID");
        dur = Long.parseLong(getIntent().getStringExtra("duration"));

        Toast.makeText(this, ""+dur, Toast.LENGTH_SHORT).show();


        try {
            if (!userType.equals("user")) {
                firebaseDatabase.getReference().child("Users").child(myUID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("status").getValue().equals("Req")) {
                            waitingDialog.show();
                            String name = snapshot.child("name").getValue().toString();
                            String gender = snapshot.child("Gender").getValue().toString();
                            String dob = snapshot.child("DOB").getValue().toString();
                            String tob = snapshot.child("TOB").getValue().toString();
                            String pob = snapshot.child("POB").getValue().toString();
                            String maritalStatus = snapshot.child("MaritalStatus").getValue().toString();
                            String occupation = snapshot.child("Occupation").getValue().toString();
                            String problem = snapshot.child("problem").getValue().toString();
                            bal = Integer.parseInt(snapshot.child("balance").getValue().toString());
                            astPrice = Integer.parseInt(getIntent().getStringExtra("astPrice"));
                            final Message message = new Message("Name: " + name + "\nGender: " + gender + "\nDOB: " + dob + "\nTOB: " + tob + "\nPOB: " + pob + "\nMarital Status: " + maritalStatus + "\nOccupation: " + occupation + "\nProblem: " + problem, senderUid, receiverUid, date.getTime());
                            binding.msgBox.setText("");
                            final String randomKey = firebaseDatabase.getReference().push().getKey();
                            firebaseDatabase.getReference().child("Chats")
                                    .child(senderRoom)
                                    .child("Messages")
                                    .child(randomKey)
                                    .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    firebaseDatabase.getReference().child("Chats")
                                            .child(receiverRoom)
                                            .child("Messages")
                                            .child(randomKey)
                                            .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                        }
                                    });
                                }
                            });
                        } else {
                            waitingDialog.dismiss();
                            dur = dur * 60000;
                            startTimer(dur, 1000);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        } catch (Exception e) {
        }

        try {
            if (userType.equals("user")) {
                busy.put("isBusy", "true");
                binding.endBtn.setVisibility(View.GONE);
                firebaseDatabase.getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid()).updateChildren(busy);
                binding.userChatName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userInfoLayoutBinding.dName.setText("Name: " + getIntent().getStringExtra("name"));
                        userInfoLayoutBinding.dGen.setText("Gender: " + getIntent().getStringExtra("gender"));
                        userInfoLayoutBinding.dDOB.setText("Date of Birth: " + getIntent().getStringExtra("dob"));
                        userInfoLayoutBinding.dTOB.setText("Time of Birth: " + getIntent().getStringExtra("tob"));
                        userInfoLayoutBinding.dPOB.setText("Place of Birth: " + getIntent().getStringExtra("pob"));
                        userInfoLayoutBinding.dMarital.setText("Marital Status: " + getIntent().getStringExtra("MaritalStatus"));
                        userInfoLayoutBinding.dOccupation.setText("Occupation: " + getIntent().getStringExtra("occupation"));
                        userDialog.show();
                    }
                });
                dur = dur * 60000;
                startTimer(dur, 1000);

                firebaseDatabase.getReference().child("Users").child(receiverUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("status").getValue().equals("Nil")) {
                            if (!ChatActivity.this.isFinishing()) {
                                Toast.makeText(ChatActivity.this, "User Ended the Chat!", Toast.LENGTH_SHORT).show();
                            }
                            astrologerEnd();
                            Intent i = new Intent(ChatActivity.this, AstrologerMainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        } catch (Exception ignored) {
        }

        senderUid = FirebaseAuth.getInstance().getUid();
        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        firebaseDatabase.getReference().child("Chats")
                .child(senderRoom)
                .child("Messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);
                            messages.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                String messageTxt = binding.msgBox.getText().toString();
                final Message message = new Message(messageTxt, senderUid, receiverUid, date.getTime());
                binding.msgBox.setText("");
                final String randomKey = firebaseDatabase.getReference().push().getKey();
                firebaseDatabase.getReference().child("Chats")
                        .child(senderRoom)
                        .child("Messages")
                        .child(randomKey)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseDatabase.getReference().child("Chats")
                                .child(receiverRoom)
                                .child("Messages")
                                .child(randomKey)
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });
                    }
                });
            }
        });

        binding.userChatName.setText(name);
        Glide.with(getApplicationContext()).load(imageURL).into(binding.circleImageView);
        binding.endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);
            }
        });

        binding.endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Ending Chat, Please Wait!");
                        progressDialog.show();
                        userEnd();
                    }
                });
                builder.setMessage("Are you sure you want to end the chat?");
                dialog = builder.create();
                dialog.show();
            }
        });

      //  checkBusy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 25) {
            if (data != null) {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    Calendar calendar = Calendar.getInstance();
                    final StorageReference reference = firebaseStorage.getReference().child("Chats").child(calendar.getTimeInMillis() + "");
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

                                        String messageTxt = binding.msgBox.getText().toString();
                                        Date date = new Date();
                                        final Message message = new Message(messageTxt, senderUid, receiverUid, date.getTime());
                                        message.setMessage("Photo");
                                        message.setImageURL(filePath);
                                        binding.msgBox.setText("");

                                        final String randomKey = firebaseDatabase.getReference().push().getKey();

                                        firebaseDatabase.getReference().child("Chats")
                                                .child(senderRoom)
                                                .child("Messages")
                                                .child(randomKey)
                                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                firebaseDatabase.getReference().child("Chats")
                                                        .child(receiverRoom)
                                                        .child("Messages")
                                                        .child(randomKey)
                                                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                });
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

    @Override
    public void onBackPressed() {
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (userType.equals("user")) {
                    //bIntent i = new Intent(ChatActivity.this, AstrologerMainActivity.class);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("You are about to end the chat! You won't earn anything!");
                    progressDialog.show();
                    astrologerEnd();
                    //startActivity(i);
                } else {
                    // Intent i = new Intent(ChatActivity.this, MainActivity.class);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Ending Chat, Please Wait!");
                    progressDialog.show();
                    userEnd();
                    //startActivity(i);
                }
                finish();
            }
        });
        builder.setMessage("Are you sure you want to end the chat?");
        dialog = builder.create();
        dialog.show();
    }

    private void userEnd() {
        textTimer.setVisibility(View.GONE);
        usedDuration = (int) dur / 60000;
        try {
            usedDuration = (int) ((dur / 60000) - (remainedSecs / 60));
        } catch (Exception e) {
        }
        statusMap.put("status", "Nil");
        statusMap.put("duration", "");
        statusMap.put("balance", (bal - (usedDuration * astPrice)) + "");

        firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(statusMap);
        Map<String, Object> userMap = new HashMap<>();
        Map<String, Object> astMap = new HashMap<>();
        String orderID = UUID.randomUUID().toString().substring(0, 21);
        userMap.put("AstUID", receiverUid);
        userMap.put("orderID", orderID);
        userMap.put("name", name);
        userMap.put("orderDate", SystemTools.getCurrent_date());
        userMap.put("orderTime", SystemTools.getCurrent_time());
        userMap.put("duration", ((dur / 60000) - (remainedSecs / 60)) + "");
        userMap.put("expense", ((usedDuration * astPrice)) + "");
        userMap.put("orderType", "Chat");

        firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Orders").child(orderID).setValue(userMap);

        astMap.put("UserUID", FirebaseAuth.getInstance().getUid());
        astMap.put("orderID", orderID);
        astMap.put("name", name);
        astMap.put("orderDate", SystemTools.getCurrent_date());
        astMap.put("orderTime", SystemTools.getCurrent_time());
        astMap.put("duration", ((dur / 60000) - (remainedSecs / 60)) + "");
        astMap.put("earning", ((usedDuration * astPrice) / 2) + "");
        astMap.put("orderType", "Chat");

        double total = ((usedDuration * astPrice) / 2);
        double gateway = (double) ((1.25 / 100) * total);
        astMap.put("geteway", "" + gateway);
        double tds = (double) ((7.5 / 100) * total);
        astMap.put("tds", "" + tds);
        double apayment = total - tds - gateway;
        astMap.put("apayment", "" + apayment);


        firebaseDatabase.getReference().child("Astrologers").child(receiverUid).child("Orders").child(orderID).setValue(astMap);
        busy.clear();
        final DatabaseReference ref = firebaseDatabase.getReference().child("Astrologers").child(receiverUid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                astBal = Integer.parseInt(snapshot.child("balance").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ref.child("balance").setValue((astBal + ((usedDuration * astPrice) / 2)) + "");
        firebaseDatabase.getReference().child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.getKey().contains(FirebaseAuth.getInstance().getUid())) {
                        firebaseDatabase.getReference().child("Chats").child(snapshot1.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        t.cancel();
        progressDialog.dismiss();
//        Dialog rvDialog = new Dialog(ChatActivity.this);
//        rvDialog.setContentView(R.layout.user_review_layout);
//        rvDialog.setCancelable(true);
//        rvDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        rvDialog.show();
//        starPos = 0;
//        final EditText rvMsg = rvDialog.findViewById(R.id.rvMsg);
//        final LinearLayout rvContainer = rvDialog.findViewById(R.id.rv_container);
//        for (int x = 0; x < rvContainer.getChildCount(); x++) {
//            starPos = x;
//            final int finalStarPos = starPos;
//            rvContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setRating(finalStarPos, rvContainer);
//                }
//            });
//        }
//        final String reviewID = UUID.randomUUID().toString().substring(0, 21);
//        final Button rvSubBtn = rvDialog.findViewById(R.id.rvSubBtn);
//        final SimpleDateFormat rvFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        rvSubBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(rvMsg.getText().toString()) || (starPos != 0)) {
//                    progressDialog.setMessage("Saving Review!");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//                    Map<String, Object> reviewMap = new HashMap<>();
//                    reviewMap.put("username", name);
//                    reviewMap.put("reviewID", reviewID);
//                    reviewMap.put("rating", starPos + "");
//                    reviewMap.put("message", rvMsg.getText().toString());
//                    reviewMap.put("timestamp", rvFormat.format(date));
//                    firebaseDatabase.getReference().child("Astrologers").child(receiverUid).child("Reviews").child(reviewID).setValue(reviewMap);
//                    //firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Reviews").child(reviewID).setValue(reviewMap);
//                    progressDialog.dismiss();
//                    Intent i = new Intent(ChatActivity.this, MainActivity.class);
//                    startActivity(i);
//                    finish();
//                } else {
//                    Toast.makeText(ChatActivity.this, "Please, Provide a review to serve you better!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void astrologerEnd() {
        busy.clear();
        busy.put("isBusy", "false");
        firebaseDatabase.getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid()).updateChildren(busy);
        progressDialog.dismiss();
    }

    public void startTimer(final long finish, long tick) {
        t = new CountDownTimer(finish, tick) {
            public void onTick(long millisUntilFinished) {
                remainedSecs = millisUntilFinished / 1000;
                textTimer.setText("" + (remainedSecs / 60) + ":" + (remainedSecs % 60));
            }

            public void onFinish() {
                if (userType.equals("user")) {
                    Intent i = new Intent(ChatActivity.this, AstrologerMainActivity.class);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Ending Chat, Please Wait!");
                    progressDialog.show();
                    astrologerEnd();
                    startActivity(i);
                } else {
                    Intent i = new Intent(ChatActivity.this, MainActivity.class).putExtra("comesfrom","chat");
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Ending Chat, Please Wait!");
                    progressDialog.show();
                    userEnd();
                    startActivity(i);
                }
                cancel();
                finish();
            }
        }.start();
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

    void fetchDataAstroData(String uid) {
        if (getIntent().getStringExtra("comesfrom").equalsIgnoreCase("user")) {
            DatabaseReference astrodata = FirebaseDatabase.getInstance().getReference("Astrologers").child(uid).child("token");
            astrodata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("astroid", snapshot.getValue().toString());
                    FcmNotificationsSender fns = new FcmNotificationsSender(snapshot.getValue().toString(), "chat", "you have chat invitation","USERCHAT", getApplicationContext(), ChatActivity.this);

                    fns.SendNotifications();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    void checkBusy()
    {
        if(getIntent().getStringExtra("comesfrom").equalsIgnoreCase("user")) {
            DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Astrologers").child(receiverUid).child("isBusy");
            dref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Toast.makeText(ChatActivity.this, ""+snapshot.getValue(String.class), Toast.LENGTH_SHORT).show();

                    if(snapshot.getValue(String.class).equalsIgnoreCase("false"))
                    {
                        userEnd();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
