package com.krash.devguruuastros.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krash.devguruuastros.databinding.ActivityWalletBinding;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WalletActivity extends AppCompatActivity implements PaymentResultListener {
    ActivityWalletBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String sAmount, email, phone;
    int amount, bal;
    Map<String, Object> updateBalance = new HashMap<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bal = Integer.parseInt(snapshot.child("balance").getValue().toString());
                binding.userBalance.setText("₹" + snapshot.child("balance").getValue().toString());
                email = snapshot.child("email").getValue().toString();
                phone = snapshot.child("phoneNo").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        binding.walletBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WalletActivity.this, MainActivity.class).putExtra("comesfrom","wallet");
                startActivity(i);
                finishAffinity();
            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                sAmount = radioButton.getText().toString().substring(1);
                amount = Math.round(Float.parseFloat(sAmount) * 100);
                
            }
        });

        binding.rechargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((binding.radioGroup.getCheckedRadioButtonId() == -1) && (binding.amountET.getText().toString().equals(""))) {
                    Toast.makeText(WalletActivity.this, "Please, Select an Amount!", Toast.LENGTH_SHORT).show();
                } else if (!binding.amountET.getText().toString().equals("")) {
                    if (Integer.parseInt(binding.amountET.getText().toString()) < 10) {
                        Toast.makeText(WalletActivity.this, "Amount should be minimum ₹10", Toast.LENGTH_SHORT).show();
                    } else {
                        amount = Math.round(Float.parseFloat(binding.amountET.getText().toString()) * 100);
                        startRecharge();
                    }
                } else {
                    startRecharge();
                }
            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        updateBalance.put("balance", (bal + (amount / 100)) + "");
        firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(updateBalance);
        binding.radioGroup.clearCheck();
        binding.amountET.setText("");
        Toast.makeText(WalletActivity.this, "Recharge is Successful!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        binding.radioGroup.clearCheck();
        binding.amountET.setText("");
        Toast.makeText(WalletActivity.this, "Recharge Failed: " + s, Toast.LENGTH_SHORT).show();
    }

    public void startRecharge() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_GkTvmdrJpg9qou");
        JSONObject object = new JSONObject();
        try {
            object.put("name", "Devguruuastro");
            object.put("description", "Test payment");
            object.put("theme.color", "");
            object.put("currency", "INR");
            object.put("amount", amount);
            object.put("prefill.contact", phone);
            object.put("prefill.email", email);
            checkout.open(WalletActivity.this, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}