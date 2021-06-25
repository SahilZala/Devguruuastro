package com.krash.devguruuastros.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.krash.devguruuastros.R;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    final Map<String, Object> chatStatus = new HashMap<>();
    AlertDialog.Builder builder;
    GoogleSignInClient googleSignInClient;
    private ConstraintLayout logoutCL, termsCL, privacyCL, ticketCL;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        builder = new AlertDialog.Builder(SettingsActivity.this);
        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        Intent i = getIntent();
        final String user = i.getStringExtra("user");

        backBtn = findViewById(R.id.settingBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ticketCL = findViewById(R.id.ticketCL);
        ticketCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, TicketActivity.class);
                startActivity(i);
            }
        });

        logoutCL = findViewById(R.id.logoutCL);
        logoutCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Are you sure you want to logout?");
                builder.setTitle("Devguruuastro");
                builder.setCancelable(true);

                assert user != null;
                if (user.equals("customer")) {
                    builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            googleSignInClient.signOut().addOnCompleteListener(SettingsActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SettingsActivity.this, "Signed Out Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SettingsActivity.this, SignInActivity.class);
                                    startActivity(i);
                                    finishAffinity();
                                }
                            });
                        }
                    });
                } else {
                    builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                            chatStatus.put("chatOnline", "Offline");
                            chatStatus.put("callOnline", "Offline");
                            FirebaseDatabase.getInstance().getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid()).updateChildren(chatStatus);
                            FirebaseAuth.getInstance().signOut();
                            startActivity(i);
                            finishAffinity();
                        }
                    });
                }
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        termsCL = findViewById(R.id.termsCl);
        termsCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setView(R.layout.termsandcondition);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        privacyCL = findViewById(R.id.privacyCl);
        privacyCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setView(R.layout.privacypolicy);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}