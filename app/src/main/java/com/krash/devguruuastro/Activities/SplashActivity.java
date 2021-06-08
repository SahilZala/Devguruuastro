package com.krash.devguruuastro.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krash.devguruuastro.R;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SystemClock.sleep(3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference astRef = FirebaseDatabase.getInstance().getReference().child("Astrologers").child(currentUser.getUid());
            astRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot != null) {

                        try {
                            if (Objects.requireNonNull(snapshot.child("isVerified").getValue()).toString().equalsIgnoreCase("true")) {
                                Intent i = new Intent(SplashActivity.this, AstrologerMainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(SplashActivity.this, "astro is not verified at", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ex)
                        {

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(currentUser.getUid())) {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class).putExtra("comesfrom","splash");
                        startActivity(i);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Intent i = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(i);
            finish();
        }
    }
}