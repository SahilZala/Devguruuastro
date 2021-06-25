package com.krash.devguruuastros.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.krash.devguruuastros.R;
import com.krash.devguruuastros.databinding.ActivityAstrologerAccountBinding;

import java.util.HashMap;
import java.util.Map;

public class AstrologerAccountActivity extends AppCompatActivity {
    ActivityAstrologerAccountBinding binding;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    Uri selectedImage;
    String profileURL, email;
    int photocount = 0;
    FirebaseStorage storage;
    TextView photocounttext;
    Map<String, Object> account = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAstrologerAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image!");
        progressDialog.setCancelable(false);
        photocounttext = findViewById(R.id.photocount);

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.bioAccount.setEnabled(true);
                binding.editSave.setVisibility(View.VISIBLE);
                binding.phoneAccount.setEnabled(true);
            }
        });

        binding.editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editSave.setVisibility(View.GONE);
                account.clear();
                account.put("mobile", binding.phoneAccount.getText().toString());
                account.put("longB", binding.bioAccount.getText().toString());

                firebaseDatabase.getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid()).updateChildren(account);
                binding.bioAccount.setEnabled(false);
                binding.phoneAccount.setEnabled(false);
                Toast.makeText(AstrologerAccountActivity.this, "Details Updated Successfully!", Toast.LENGTH_SHORT).show();
            }
        });


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 110);
            }
        });

        binding.imageView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AstrologerAccountActivity.this, AstrologerMainActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 110) {
            if(photocount < 6) {
                if (data != null) {
                    if (data.getData() != null) {
                        progressDialog.show();
                        binding.profileImage.setImageURI(data.getData());
                        selectedImage = data.getData();
                        final StorageReference profile = storage.getReference().child(email).child("profileImage");
                        profile.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    profile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            profileURL = uri.toString();
                                            account.clear();
                                            account.put("profileURL", profileURL);
                                            account.put("photocount", ""+(photocount+1));
                                            firebaseDatabase.getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid()).updateChildren(account);

                                            firebaseDatabase.getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid()).child("Photos").child(""+(photocount+1)).setValue(profileURL);


                                        }
                                    });
                                } else {
                                    Toast.makeText(AstrologerAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
            else
            {
                Toast.makeText(this, "You reach limit.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = firebaseDatabase.getReference().child("Astrologers").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.bioAccount.setText(snapshot.child("longB").getValue().toString());
                binding.phoneAccount.setText(snapshot.child("mobile").getValue().toString());
                email = snapshot.child("email").getValue().toString();
                photocount = Integer.parseInt(snapshot.child("photocount").getValue().toString());
                photocounttext.setText(""+String.valueOf(photocount)+"/"+"6");

                Glide.with(getApplicationContext()).load(snapshot.child("profileURL").getValue()).into(binding.profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}