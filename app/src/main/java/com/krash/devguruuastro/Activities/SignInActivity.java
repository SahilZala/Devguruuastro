package com.krash.devguruuastro.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krash.devguruuastro.Models.User;
import com.krash.devguruuastro.R;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1001;
    public static String phoneNo;
    SignInButton signInButton;
    GoogleSignInClient googleSignInClient;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private TextView astrologerBtn, skipBtn,terms;
    private EditText phoneET;

    AlertDialog.Builder builder;

    ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
       // imageView2 = findViewById(R.id.imageView2);
     //   imageView2.setImageResource(R.drawable.roundlogo);
        mAuth = FirebaseAuth.getInstance();
        phoneET = findViewById(R.id.phoneSignIn);
        astrologerBtn = findViewById(R.id.astrologerBtn);
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Signing In, Please Wait!");

        terms = findViewById(R.id.terms);

        builder = new AlertDialog.Builder(SignInActivity.this);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setView(R.layout.privacypolicy);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(phoneET.getText().toString())) {
                    phoneNo = phoneET.getText().toString();
                    signInToGoogle();
                } else {
                    Toast.makeText(SignInActivity.this, "Please, Enter your Phone Number!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        configureGoogleClient();

        astrologerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void configureGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void signInToGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "Google Sign in Succeeded", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                showToastMessage("Google Sign in Failed " + e);
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(FirebaseAuth.getInstance().getUid())) {
                                    Intent i = new Intent(SignInActivity.this, MainActivity.class).putExtra("comesfrom","signin");
                                    progressDialog.dismiss();
                                    startActivity(i);
                                } else {
                                    User user = new User(FirebaseAuth.getInstance().getUid(), account.getEmail(), account.getDisplayName(), phoneET.getText().toString(), account.getPhotoUrl().toString(), "Nil", "", "0");
                                    firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(user);
                                    showToastMessage("Firebase Authentication Succeeded ");
                                    Intent i = new Intent(SignInActivity.this, MainActivity.class);
                                    progressDialog.dismiss();
                                    startActivity(i);
                                }
                                finishAffinity();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
    }

    private void showToastMessage(String message) {
        Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG).show();
    }
}