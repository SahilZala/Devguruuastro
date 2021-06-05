package com.krash.devguruuastro.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.krash.devguruuastro.R;
import com.krash.devguruuastro.databinding.ForgotPassDialogBinding;

public class LoginActivity extends AppCompatActivity {
    private final String regex_email = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    Dialog forgotDialog;
    ForgotPassDialogBinding binding;
    private TextView registerBtn, forgotPass;
    private EditText email, pass;
    private Button loginBtn;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ForgotPassDialogBinding.inflate(getLayoutInflater());

        pb = findViewById(R.id.loginPB);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        email = findViewById(R.id.loginEmail);
        pass = findViewById(R.id.loginPass);

        forgotDialog = new Dialog(LoginActivity.this);
        forgotDialog.setContentView(binding.getRoot());
        forgotDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        forgotDialog.setCancelable(true);

        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

        forgotPass = findViewById(R.id.forgotPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotDialog.show();
                binding.sendFP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (binding.emailAddress.getText().equals("")) {
                            Toast.makeText(LoginActivity.this, "Enter your registered email address!", Toast.LENGTH_SHORT).show();
                        } else {
                            firebaseAuth.sendPasswordResetEmail(binding.emailAddress.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, "Password reset link sent to your email address!", Toast.LENGTH_SHORT).show();
                                                forgotDialog.dismiss();
                                            } else {
                                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
        });

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText().toString()) && email.getText().toString().matches(regex_email)) {
                    if (pass.getText().toString().length() >= 8) {
                        loginBtn.setVisibility(View.INVISIBLE);
                        pb.setVisibility(View.VISIBLE);
                        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    databaseReference = firebaseDatabase.getReference().child("Astrologers").child(firebaseAuth.getUid());
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.child("isVerified").getValue().equals("false")) {
                                                pb.setVisibility(View.INVISIBLE);
                                                loginBtn.setVisibility(View.VISIBLE);
                                                Toast.makeText(LoginActivity.this, "Please, wait until your credentials are verified!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Intent i = new Intent(LoginActivity.this, AstrologerMainActivity.class);
                                                startActivity(i);
                                                finishAffinity();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                } else {
                                    pb.setVisibility(View.INVISIBLE);
                                    loginBtn.setVisibility(View.VISIBLE);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}