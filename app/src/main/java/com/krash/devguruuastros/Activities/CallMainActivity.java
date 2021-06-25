package com.krash.devguruuastros.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krash.devguruuastros.Adapters.ChatMainAdapter;
import com.krash.devguruuastros.Models.AstrologerModel;
import com.krash.devguruuastros.R;

import java.util.ArrayList;
import java.util.List;

public class CallMainActivity extends AppCompatActivity {
    private final List<AstrologerModel> astrologerModelListOnline = new ArrayList<>();
    private RecyclerView callAstRV;
    private ChatMainAdapter chatMainAdapter;
    ImageView callmainbackbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_main);

        callmainbackbtn = findViewById(R.id.callmainbackbtn);
        callAstRV = findViewById(R.id.callMainRV);
        FirebaseDatabase.getInstance().getReference().child("Astrologers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                astrologerModelListOnline.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("callOnline").getValue().toString().equals("Online")) {
                        AstrologerModel model = snapshot1.getValue(AstrologerModel.class);
                        astrologerModelListOnline.add(model);
                    }
                }

                callAstRV.setLayoutManager(new LinearLayoutManager(CallMainActivity.this, LinearLayoutManager.VERTICAL, false));
                chatMainAdapter = new ChatMainAdapter(astrologerModelListOnline);
                callAstRV.setAdapter(chatMainAdapter);
                chatMainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CallMainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        callmainbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}