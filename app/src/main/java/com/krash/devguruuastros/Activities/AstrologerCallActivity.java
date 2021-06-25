package com.krash.devguruuastros.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krash.devguruuastros.Models.User;
import com.krash.devguruuastros.R;
import com.krash.devguruuastros.databinding.ActivityAstrologerCallBinding;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;
import com.sinch.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AstrologerCallActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    SinchClient sinchClient;
    Call call;
    FirebaseDatabase firebaseDatabase;
    Map<String, Object> status;
    ActivityAstrologerCallBinding binding;

    User user;
    AlertDialog.Builder alertDialog;

    ImageView callBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityAstrologerCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        callBackBtn = findViewById(R.id.callBackBtn);


        alertDialog = new AlertDialog.Builder(AstrologerCallActivity.this);

        Gson gson = new Gson();


        user = gson.fromJson(getIntent().getStringExtra("myjson"), User.class);

        responceToRequest();


        firebaseDatabase = FirebaseDatabase.getInstance();
        status = new HashMap<>();

        sinchClient = Sinch.getSinchClientBuilder()
                .context(AstrologerCallActivity.this)
                .userId(FirebaseAuth.getInstance().getUid())
                .applicationKey("6f2b7acf-c28f-4d61-bd44-ec5abd2c95a8")
                .applicationSecret("8h4s41t9XkuxfCB65CuD3A==")
                .environmentHost("clientapi.sinch.com")
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();

        requestAudioPermissions();

        sinchClient.getCallClient().addCallClientListener(new CallClientListener() {
            @Override
            public void onIncomingCall(final CallClient callClient, final Call incomingCall) {

                alertDialog.create();
                alertDialog.setTitle("Calling!");
                alertDialog.setCancelable(false);
                call = incomingCall;
                Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                final Ringtone ringtoneSound = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
                if (ringtoneSound != null) {
                    ringtoneSound.play();
                }
                alertDialog.setNeutralButton("Reject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ringtoneSound.stop();
                        call.hangup();
                    }
                });

                alertDialog.setPositiveButton( "Pick", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ringtoneSound.stop();
                        call.answer();
                        binding.cl.setVisibility(View.VISIBLE);
                        call.addCallListener(new sinchCallListener());
                        status.clear();
                        status.put("isBusy", "true");
                        firebaseDatabase.getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid()).updateChildren(status);
                        firebaseDatabase.getReference().child("Users").child(incomingCall.getRemoteUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                binding.callerName.setText(snapshot.child("name").getValue().toString());
                                Glide.with(AstrologerCallActivity.this).load(snapshot.child("profileImage").getValue().toString()).into(binding.callerImg);
                            }

                            @Override

                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        binding.callerEndBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                call.hangup();
                                binding.cl.setVisibility(View.GONE);
                            }
                        });
                        Toast.makeText(AstrologerCallActivity.this, "Call is started!", Toast.LENGTH_SHORT).show();
                    }
                });

                try {
                    alertDialog.show();
                }
                catch(Exception ex)
                {
                    call.hangup();
//                    Toast.makeText(AstrologerCallActivity.this, "ex", Toast.LENGTH_SHORT).show();
//                    //ringtoneSound.stop();
//                    Log.d("bad token",ex.getMessage());
                }
            }
        });

        sinchClient.start();


        callBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    private class sinchCallListener implements CallListener {

        @Override
        public void onCallProgressing(Call call) {
            Toast.makeText(AstrologerCallActivity.this, "Ringing...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallEstablished(Call call) {
            Toast.makeText(AstrologerCallActivity.this, "Call Established!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallEnded(Call endedCall) {
            Toast.makeText(AstrologerCallActivity.this, "Call Ended!", Toast.LENGTH_SHORT).show();
            call = null;
            status.clear();
            status.put("isBusy", "false");
            firebaseDatabase.getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid()).updateChildren(status);
            endedCall.hangup();
            binding.cl.setVisibility(View.GONE);

            onBackPressed();

        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }

    @Override
    public void onBackPressed() {
        firebaseDatabase.getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid()).child("isBusy").setValue("false");

        sinchClient.stopListeningOnActiveConnection();
        sinchClient.terminate();

        finish();

    }

    void responceToRequest()
    {
        FirebaseDatabase.getInstance().getReference("RequestQueue").child(user.getRc().getAstrologerid()).child(user.getRc().getRequestid()).child("status").setValue("accept");
    }
}