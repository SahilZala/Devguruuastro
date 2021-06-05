package com.krash.devguruuastro.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.krash.devguruuastro.Models.AstrologerModel;
import com.krash.devguruuastro.R;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {
    private final String regex_email = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    Uri selectedImage, bachUri, mastUri, skillUri, aadhaarUri, panUri, checkUri;
    DatePickerDialog datePickerDialog;
    FirebaseStorage storage;
    ProgressBar progressBar;
    ConstraintLayout personalCL, qualCL, oCL, dCL;
    String profileURL, bachURL, mastURL, skillURL, aadharURL, panURL, checkURL;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    String uid;
    private EditText name, email, address, mobile, dob, bachD, masD, otherQ, skills, exp, longB, workD,
            accountD, aadhaarNO, panNO, language, father , hoursSpend, pass;
//    link
    private EditText accountholdername,ifsccode;
    private Button pBtn, regBtn, qBtn, oBtn, qBackBtn, oBackBtn, regBackBtn;
    private ImageView profileImage;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        storage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image!");
        progressDialog.setCancelable(false);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        mobile = findViewById(R.id.phone);
        dob = findViewById(R.id.dob);
        progressBar = findViewById(R.id.regPB);
        bachD = findViewById(R.id.bachD);
        masD = findViewById(R.id.masterD);
        otherQ = findViewById(R.id.otherQ);
        skills = findViewById(R.id.skills);
        exp = findViewById(R.id.exp);
        longB = findViewById(R.id.longB);
        workD = findViewById(R.id.workStatus);
        accountD = findViewById(R.id.accountDetails);
        accountholdername = findViewById(R.id.accountholdername);
        ifsccode = findViewById(R.id.ifsccode);

        aadhaarNO = findViewById(R.id.aadharNO);
        panNO = findViewById(R.id.panNO);
        language = findViewById(R.id.languagesKnown);
        father = findViewById(R.id.fathusname);
      //  link = findViewById(R.id.videoURL);
        hoursSpend = findViewById(R.id.numberYou);
        pass = findViewById(R.id.password);

        personalCL = findViewById(R.id.personalCL);
        qualCL = findViewById(R.id.qualCL);
        oCL = findViewById(R.id.oCL);
        dCL = findViewById(R.id.dCL);

        firebaseDatabase = FirebaseDatabase.getInstance();
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.confirmation_layout);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.border_back));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);
                datePickerDialog.show();
            }
        });

        regBtn = findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aadhaarUri == null) {
                    Toast.makeText(RegistrationActivity.this, "Please, Upload your Aadhaar Card!", Toast.LENGTH_SHORT).show();
                } else if (panUri == null) {
                    Toast.makeText(RegistrationActivity.this, "Please, Upload your Pan Card!", Toast.LENGTH_SHORT).show();
                }
                else if (checkUri == null) {
                    Toast.makeText(RegistrationActivity.this, "Please, Upload your Canceld check!", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(accountD.getText().toString()) || TextUtils.isEmpty(aadhaarNO.getText().toString()) || aadhaarNO.getText().toString().length() != 12 || TextUtils.isEmpty(panNO.getText().toString()) || panNO.getText().toString().length() != 10 || TextUtils.isEmpty(accountholdername.getText().toString()) || TextUtils.isEmpty(ifsccode.getText().toString())) {
                    Toast.makeText(RegistrationActivity.this, "Please, Fill all the details correctly!", Toast.LENGTH_SHORT).show();
                } else {
                    regBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    saveDetails();
                }
            }
        });

        regBackBtn = findViewById(R.id.regBackBtn);
        regBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dCL.setVisibility(View.GONE);
                oCL.setVisibility(View.VISIBLE);
            }
        });

        pBtn = findViewById(R.id.pBtn);
        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImage == null) {
                    Toast.makeText(RegistrationActivity.this, "Please, Upload your profile photo!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(mobile.getText().toString()) || TextUtils.isEmpty(pass.getText().toString()) || TextUtils.isEmpty(address.getText().toString()) || TextUtils.isEmpty(dob.getText().toString()) || TextUtils.isEmpty(email.getText().toString())) {
                    if (email.getText().toString().matches(regex_email)) {
                        Toast.makeText(RegistrationActivity.this, "Please, fill all the details!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Enter valid Email Address!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    personalCL.setVisibility(View.GONE);
                    qualCL.setVisibility(View.VISIBLE);
                }

            }
        });

        qBtn = findViewById(R.id.qBtn);
        qBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bachUri == null) {
                    Toast.makeText(RegistrationActivity.this, "Upload your Bachelor's Degree!", Toast.LENGTH_SHORT).show();
                } else if (skillUri == null) {
                    Toast.makeText(RegistrationActivity.this, "Upload your Skills Certificate!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(bachD.getText().toString()) || TextUtils.isEmpty(skills.getText().toString()) || TextUtils.isEmpty(exp.getText().toString()) || TextUtils.isEmpty(workD.getText().toString()) || TextUtils.isEmpty(otherQ.getText().toString())) {
                    Toast.makeText(RegistrationActivity.this, "Please, Fill all the details!", Toast.LENGTH_SHORT).show();
                } else {
                    qualCL.setVisibility(View.GONE);
                    oCL.setVisibility(View.VISIBLE);
                }
            }
        });

        qBackBtn = findViewById(R.id.qBackBtn);
        qBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualCL.setVisibility(View.GONE);
                personalCL.setVisibility(View.VISIBLE);
            }
        });

        oBtn = findViewById(R.id.oBtn);
        oBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(longB.getText().toString()) || TextUtils.isEmpty(language.getText().toString()) || TextUtils.isEmpty(father.getText().toString()) || TextUtils.isEmpty(hoursSpend.getText().toString())) {
                    Toast.makeText(RegistrationActivity.this, "Please, Fill all the details!", Toast.LENGTH_SHORT).show();
                } else {
                    oCL.setVisibility(View.GONE);
                    dCL.setVisibility(View.VISIBLE);
                }
            }
        });

        oBackBtn = findViewById(R.id.oBackBtn);
        oBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oCL.setVisibility(View.GONE);
                qualCL.setVisibility(View.VISIBLE);
            }
        });

        profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Please, Enter your Email Address First!", Toast.LENGTH_SHORT).show();
                } else if (!email.getText().toString().matches(regex_email)) {
                    Toast.makeText(RegistrationActivity.this, "", Toast.LENGTH_SHORT).show();
                } else {
                    email.setEnabled(false);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 10);
                }
            }
        });

        TextView uploadBach = findViewById(R.id.uploadBach);
        uploadBach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 20);
            }
        });

        TextView uploadMast = findViewById(R.id.uploadMast);
        uploadMast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 30);
            }
        });

        TextView cSkills = findViewById(R.id.cSkills);
        cSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 40);
            }
        });

        TextView uploadAadhaar = findViewById(R.id.uploadAadhaar);
        uploadAadhaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 50);
            }
        });

        TextView uploadPan = findViewById(R.id.uploadPan);
        uploadPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 60);
            }
        });

        TextView uploadCheck = findViewById(R.id.canceldcheck);
        uploadCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 70);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                if (data.getData() != null) {
                    progressDialog.show();
                    profileImage.setImageURI(data.getData());
                    selectedImage = data.getData();
                    final StorageReference profile = storage.getReference().child(email.getText().toString()).child("profileImage");
                    profile.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                profile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        profileURL = uri.toString();
                                    }
                                });
                            } else {
                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        } else if (requestCode == 20) {
            if (data != null) {
                if (data.getData() != null) {
                    progressDialog.show();
                    bachUri = data.getData();
                    final StorageReference bach = storage.getReference().child(email.getText().toString()).child("bachelorDegree");
                    bach.putFile(bachUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                bach.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        bachURL = uri.toString();
                                    }
                                });
                            } else {
                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        } else if (requestCode == 30) {
            if (data != null) {
                if (data.getData() != null) {
                    if (!masD.getText().toString().equals("")) {
                        progressDialog.show();
                        mastUri = data.getData();
                        final StorageReference mast = storage.getReference().child(email.getText().toString()).child("masterDegree");
                        mast.putFile(mastUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    mast.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            mastURL = uri.toString();
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, "Please, Enter your Master's Degree!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (requestCode == 40) {
            if (data != null) {
                if (data.getData() != null) {
                    progressDialog.show();
                    skillUri = data.getData();
                    final StorageReference skill = storage.getReference().child(email.getText().toString()).child("skillCertificate");
                    skill.putFile(skillUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                skill.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        skillURL = uri.toString();
                                    }
                                });
                            } else {
                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        } else if (requestCode == 50) {
            if (data != null) {
                if (data.getData() != null) {
                    progressDialog.show();
                    aadhaarUri = data.getData();
                    final StorageReference aadhar = storage.getReference().child(email.getText().toString()).child("aadhaarCard");
                    aadhar.putFile(aadhaarUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                aadhar.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        aadharURL = uri.toString();
                                    }
                                });
                            } else {
                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        } else if (requestCode == 60) {
            if (data != null) {
                if (data.getData() != null) {
                    progressDialog.show();
                    panUri = data.getData();
                    final StorageReference pan = storage.getReference().child(email.getText().toString()).child("panCard");
                    pan.putFile(panUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                pan.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        panURL = uri.toString();
                                    }
                                });
                            } else {
                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }
        else if (requestCode == 70) {
            if (data != null) {
                if (data.getData() != null) {
                    progressDialog.show();
                    checkUri = data.getData();
                    final StorageReference check = storage.getReference().child(email.getText().toString()).child("cancelCheck");
                    check.putFile(checkUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                check.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        checkURL= uri.toString();
                                    }
                                });
                            } else {
                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }
    }

    private void saveDetails() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uid = FirebaseAuth.getInstance().getUid();
                    AstrologerModel astrologer;
                    if (masD.getText().toString().equals("")) {
                        astrologer = new AstrologerModel(uid, profileURL, name.getText().toString(), email.getText().toString(), pass.getText().toString(), address.getText().toString(), mobile.getText().toString(), dob.getText().toString(), bachD.getText().toString(), bachURL, "", "", otherQ.getText().toString(), skills.getText().toString(), skillURL, exp.getText().toString(), longB.getText().toString(), workD.getText().toString(), accountD.getText().toString(), aadhaarNO.getText().toString(), aadharURL, panNO.getText().toString(), panURL, language.getText().toString(), father.getText().toString(), "video url", hoursSpend.getText().toString(), "Offline", "Offline", "0", "0", "0", "0", "0", "false", "false", "0",accountholdername.getText().toString(),ifsccode.getText().toString(),checkURL,"0","1");
                    } else {
                        astrologer = new AstrologerModel(uid, profileURL, name.getText().toString(), email.getText().toString(), pass.getText().toString(), address.getText().toString(), mobile.getText().toString(), dob.getText().toString(), bachD.getText().toString(), bachURL, masD.getText().toString(), mastURL, otherQ.getText().toString(), skills.getText().toString(), skillURL, exp.getText().toString(), longB.getText().toString(), workD.getText().toString(), accountD.getText().toString(), aadhaarNO.getText().toString(), aadharURL, panNO.getText().toString(), panURL, language.getText().toString(), father.getText().toString(), "video url", hoursSpend.getText().toString(), "Offline", "Offline", "0", "0", "0", "0", "0", "false", "false", "0",accountholdername.getText().toString(),ifsccode.getText().toString(),checkURL,"0","1");
                    }
                    firebaseDatabase.getReference().child("Astrologers")
                            .child(uid)
                            .setValue(astrologer)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loadingDialog.show();
                                    loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    });

                                }
                            });
                } else {
                    progressBar.setVisibility(View.GONE);
                    regBtn.setVisibility(View.VISIBLE);
                    loadingDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}