package com.krash.devguruuastros.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.krash.devguruuastros.R;

import java.util.UUID;

public class TicketActivity extends AppCompatActivity {
    private Button send;
    private EditText msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        send = findViewById(R.id.sendTicket);
        msg = findViewById(R.id.ticketMSG);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg.setText("");
                Toast.makeText(TicketActivity.this, "Your ticket (" + UUID.randomUUID().toString() + ") has been raised. we will revert back shortly!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}