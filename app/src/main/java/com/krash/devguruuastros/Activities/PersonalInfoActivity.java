package com.krash.devguruuastros.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.krash.devguruuastros.Models.RequestClass;
import com.krash.devguruuastros.Models.SystemTools;
import com.krash.devguruuastros.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PersonalInfoActivity extends AppCompatActivity {
    EditText dob, tob, pob, occupation, name, problem;
    Spinner genderSpin, marriedSpin;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Button saveBtn;
    int bal, astPrice;
    Dialog dPrice;
    String dur;
    TextView balance, duration;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ProgressBar progressBar2;
    String[] gender = {"Select your Gender", "Male", "Female"};
    String[] married = {"Select your Marital Status", "Married", "Single", "Divorced"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        firebaseDatabase = FirebaseDatabase.getInstance();


        databaseReference = firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("status").getValue().toString().equalsIgnoreCase("completed")) {

                    try {
                        bal = Integer.parseInt(snapshot.child("balance").getValue().toString());
                        balance.setText("Your Current Balance: ₹" + snapshot.child("balance").getValue().toString());
                        if (astPrice > 0) {
                            duration.setText("Max Duration: " + bal / astPrice + " min(s)");
                            dur = bal / astPrice + "";
                        }
                        name.setText(snapshot.child("name").getValue().toString());
                        dob.setText(snapshot.child("DOB").getValue().toString());
                        tob.setText(snapshot.child("TOB").getValue().toString());
                        pob.setText(snapshot.child("POB").getValue().toString());
                        occupation.setText(snapshot.child("Occupation").getValue().toString());
                        problem.setText(snapshot.child("problem").getValue().toString());

//                        Map<String, Object> info = new HashMap<>();
//                        info.put("name", name.getText().toString());
//                        info.put("DOB", dob.getText().toString());
//                        info.put("TOB", tob.getText().toString());
//                        info.put("POB", pob.getText().toString());
//                        info.put("problem", problem.getText().toString());
//                        info.put("Occupation", occupation.getText().toString());
//                        info.put("Gender", genderSpin.getSelectedItem().toString());
//                        info.put("MaritalStatus", marriedSpin.getSelectedItem().toString());
//                        info.put("status", "completed");
//                        info.put("duration", dur);
//                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(info);


                        sendRequest();


                    } catch (Exception e) {
                    }

                }
                else
                {
                    try {
                        bal = Integer.parseInt(snapshot.child("balance").getValue().toString());
                        balance.setText("Your Current Balance: ₹" + snapshot.child("balance").getValue().toString());
                        if (astPrice > 0) {
                            duration.setText("Max Duration: " + bal / astPrice + " min(s)");
                            dur = bal / astPrice + "";
                        }
                        name.setText(snapshot.child("name").getValue().toString());
                        dob.setText(snapshot.child("DOB").getValue().toString());
                        tob.setText(snapshot.child("TOB").getValue().toString());
                        pob.setText(snapshot.child("POB").getValue().toString());
                        occupation.setText(snapshot.child("Occupation").getValue().toString());
                        problem.setText(snapshot.child("problem").getValue().toString());

                    } catch (Exception e) {
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        name = findViewById(R.id.nameUser);
        dob = findViewById(R.id.dobUser);
        tob = findViewById(R.id.tobUser);
        pob = findViewById(R.id.pobUser);
        occupation = findViewById(R.id.occupation);
        problem = findViewById(R.id.userProblem);
        genderSpin = findViewById(R.id.genderSpin);
        marriedSpin = findViewById(R.id.married);
        saveBtn = findViewById(R.id.saveBtn);
        balance = findViewById(R.id.balanceUser);
        duration = findViewById(R.id.durationUser);
        progressBar2 = findViewById(R.id.progressBar2);

        final ArrayAdapter gen = new ArrayAdapter(PersonalInfoActivity.this, android.R.layout.simple_spinner_item, gender);
        gen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpin.setAdapter(gen);

        ArrayAdapter mar = new ArrayAdapter(PersonalInfoActivity.this, android.R.layout.simple_spinner_item, married);
        mar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marriedSpin.setAdapter(mar);

        try {
            astPrice = Integer.parseInt(getIntent().getStringExtra("astPrice"));
        } catch (Exception e) {
        }

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(PersonalInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        tob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(PersonalInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tob.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

//        duration.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dPrice = new Dialog(PersonalInfoActivity.this);
//                dPrice.setContentView(R.layout.price_dialog);
//                dPrice.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                dPrice.setCancelable(true);
//                final EditText qty = dPrice.findViewById(R.id.quantity_no);
//                final Button submitBtn = dPrice.findViewById(R.id.Submitbutton);
//                final TextView text = dPrice.findViewById(R.id.textView24);
//                text.setText("Set Duration");
//                submitBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        try {
//                            if ((astPrice > 0) && (Integer.parseInt(qty.getText().toString())) > bal / astPrice) {
//                                Toast.makeText(PersonalInfoActivity.this, "Your balance is low, Recharge to chat!", Toast.LENGTH_SHORT).show();
//                            }
//                            if (Integer.parseInt(qty.getText().toString()) < 5) {
//                                Toast.makeText(PersonalInfoActivity.this, "Duration should be at least 5 mins!", Toast.LENGTH_SHORT).show();
//                            } else {
//                                duration.setText("Duration: " + qty.getText().toString() + " min(s)");
//                                dur = qty.getText().toString();
//                            }
//                        } catch (Exception ignored) {
//                        }
//                        dPrice.dismiss();
//                    }
//                });
//                dPrice.show();
//            }
//        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBtn.setVisibility(View.GONE);
                progressBar2.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(problem.getText().toString()) || TextUtils.isEmpty(dob.getText().toString()) || TextUtils.isEmpty(tob.getText().toString()) || TextUtils.isEmpty(pob.getText().toString()) || TextUtils.isEmpty(occupation.getText().toString()) || genderSpin.getSelectedItemPosition() == 0 || marriedSpin.getSelectedItemPosition() == 0) {
                    saveBtn.setVisibility(View.VISIBLE);
                    progressBar2.setVisibility(View.GONE);
                    Toast.makeText(PersonalInfoActivity.this, "Please, Fill the details!", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> info = new HashMap<>();
                    info.put("name", name.getText().toString());
                    info.put("DOB", dob.getText().toString());
                    info.put("TOB", tob.getText().toString());
                    info.put("POB", pob.getText().toString());
                    info.put("problem", problem.getText().toString());
                    info.put("Occupation", occupation.getText().toString());
                    info.put("Gender", genderSpin.getSelectedItem().toString());
                    info.put("MaritalStatus", marriedSpin.getSelectedItem().toString());
                    info.put("status", "completed");
                    info.put("duration", dur);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(info);


                    sendRequest();



                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void sendRequest()
    {

        String reqid = FirebaseDatabase.getInstance().getReference().push().getKey();
        String sessionid = UUID.randomUUID().toString();

        final RequestClass rc = new RequestClass(getIntent().getStringExtra("uid").toString(),FirebaseAuth.getInstance().getUid(),reqid,sessionid,name.getText().toString(),"url",dur, SystemTools.getCurrent_time(),SystemTools.getCurrent_date(),"request","true","chat");
        FirebaseDatabase.getInstance().getReference().child("RequestQueue").child(getIntent().getStringExtra("uid")).child(reqid).setValue(rc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent i = new Intent(PersonalInfoActivity.this, UserChatActivity.class);
                    i.putExtra("name", getIntent().getStringExtra("name"));
                    i.putExtra("uid", getIntent().getStringExtra("uid"));
                    i.putExtra("myUID", FirebaseAuth.getInstance().getUid());
                    i.putExtra("astPrice", astPrice + "");
                    i.putExtra("balance",""+bal);
                    i.putExtra("username",""+name.getText());


                    Gson gson = new Gson();
                    String myjson = gson.toJson(rc);
                    //   Toast.makeText(context, ""+myjson, Toast.LENGTH_SHORT).show();
                    Log.d("userobject",myjson);
                    i.putExtra("myjson",myjson);



                startActivity(i);
                finish();

                Toast.makeText(PersonalInfoActivity.this, "Request sended", Toast.LENGTH_SHORT).show();
            }
        });

        
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PersonalInfoActivity.this, MainActivity.class).putExtra("comesfrom","personal");
        startActivity(i);
        finish();
    }
}