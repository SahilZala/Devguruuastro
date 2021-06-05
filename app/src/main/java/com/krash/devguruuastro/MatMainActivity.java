package com.krash.devguruuastro;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.krash.devguruuastro.Models.MatClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MatMainActivity extends AppCompatActivity {

    EditText username,pobuser,useroccupation,fathername,fatheroccupation,mothername,motheroccupation,boysiblingcount,siblingdetails,dob,dot,mobileno,email,girlsiblingcount;
    Spinner gender,married;
    String genderSelected = "male";
    String marriedSelected = "Single";
    Calendar myCalendar;
    String userid;
    Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat_main);


        userid = getIntent().getStringExtra("userid");

        username = findViewById(R.id.nameUser);
        pobuser = findViewById(R.id.pobUser);
        useroccupation = findViewById(R.id.occupation);
        fathername = findViewById(R.id.fathername);
        fatheroccupation = findViewById(R.id.fatheroccupation);
        mothername = findViewById(R.id.mothername);
        motheroccupation = findViewById(R.id.motheroccupation);
        dob = findViewById(R.id.dobUser);
        dot = findViewById(R.id.tobUser);
        married = findViewById(R.id.married);
        girlsiblingcount = findViewById(R.id.girlsibling);
        mobileno = findViewById(R.id.userPhone);
        email = findViewById(R.id.useremail);

        saveBtn = findViewById(R.id.saveBtn);


        boysiblingcount = findViewById(R.id.boysibling);
        siblingdetails = findViewById(R.id.siblingdetails);

        gender = findViewById(R.id.genderSpin);

        final ArrayList<String> genderList = new ArrayList();
        final ArrayList<String> marriedList = new ArrayList<>();

        genderList.add("male");
        genderList.add("female");

        marriedList.add("Single");
        marriedList.add("Divorced");
        marriedList.add("Widow/widower");
        marriedList.add("Seprates");



        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, genderList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(arrayAdapter);



        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderSelected = genderList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ArrayAdapter<String> marriredArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, marriedList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        married.setAdapter(marriredArrayAdapter);



        married.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                marriedSelected = marriedList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MatMainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MatMainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        dot.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToFirebase();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/mm/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(myCalendar.getTime()));
        
    }

    void sendDataToFirebase()
    {
        if(!username.getText().toString().isEmpty() || !dob.getText().toString().isEmpty() || !dot.getText().toString().isEmpty() || !pobuser.getText().toString().isEmpty() || !useroccupation.getText().toString().isEmpty() || !fathername.getText().toString().isEmpty() || !fatheroccupation.getText().toString().isEmpty() || !mothername.getText().toString().isEmpty() || !motheroccupation.getText().toString().isEmpty() || !boysiblingcount.getText().toString().isEmpty() || !siblingdetails.getText().toString().isEmpty() || !girlsiblingcount.getText().toString().isEmpty() || !mobileno.getText().toString().isEmpty() || !email.getText().toString().isEmpty()){
            DatabaseReference dref = FirebaseDatabase.getInstance().getReference("MatData").child(userid);

            String matid = dref.push().getKey();
            MatClass mc = new MatClass(userid,matid,username.getText().toString(),genderSelected,dob.getText().toString(),dot.getText().toString(),pobuser.getText().toString(),marriedSelected,mobileno.getText().toString(),email.getText().toString(),useroccupation.getText().toString(),fathername.getText().toString(),fatheroccupation.getText().toString(),mothername.getText().toString(),motheroccupation.getText().toString(),boysiblingcount.getText().toString(),girlsiblingcount.getText().toString(),siblingdetails.getText().toString());

            dref.setValue(mc).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MatMainActivity.this, "Data submited succesfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        else{
            Toast.makeText(this, "!Please Complete all field", Toast.LENGTH_SHORT).show();
        }

    }


}