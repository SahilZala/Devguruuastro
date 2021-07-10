package com.krash.devguruuastros.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
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
import com.krash.devguruuastros.BuildConfig;
import com.krash.devguruuastros.R;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new versionChecker().execute();



        //  Log.d("comes from",getIntent() .toString());
        SystemClock.sleep(3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
//
//        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
//        if(getIntent().getStringExtra("data") != null)
//        {
//            Toast.makeText(this, "hhh", Toast.LENGTH_SHORT).show();
//            if(getIntent().getStringExtra("data").equalsIgnoreCase("user"))
//            {
//                Toast.makeText(this, "daad", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(),UsersActivity.class));
//            }
//        }


    }



    class versionChecker extends AsyncTask<String, Void, String> {
        String newVersion;

        @Override
        protected String doInBackground(String... params) {

            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.krash.devguruuastros")
                        .timeout(30000)
                        .get()
                        .select("div.hAyfc:nth-child(4)>"+
                                    "span:nth-child(2) > div:nth-child(1)"+
                                    "> span:nth-child(1)")
                        .first()
                        .ownText();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return newVersion;
        }

        @Override
        protected void onPostExecute(String s) {
         currentVersion = BuildConfig.VERSION_NAME;

         if(newVersion != null)
         {
             float cversion = Float.parseFloat(currentVersion);
             float lversion = Float.parseFloat(newVersion);

             if(lversion > cversion)
             {
                 showUpdateDialog();

             }
             else
             {
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

        }
    }

    String currentVersion;

    private void showUpdateDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.krash.devguruuastros")));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //background.start();


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
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }
}
