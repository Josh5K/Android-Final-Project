package com.example.easywallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.util.JsonMapper;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PayCharge extends AppCompatActivity {

    private TextView twpayable;
    private TextView twamount;
    private TextView twcurrentBalance;
    private TextView twnewBalance;
    private DatabaseReference mDatabase;
    private SharedPreferences prefs;
    private Double amountValue;
    private Button payCharge;
    private String finalAmount;
    private Button deny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_charge);

        final Intent intent = getIntent();

        String from = intent.getStringExtra("from");
        final String amount = intent.getStringExtra("amount");
        String status = intent.getStringExtra("status");

        twpayable = findViewById(R.id.to);
        twamount = findViewById(R.id.amountOwed);
        twcurrentBalance = findViewById(R.id.beforeBalance);
        twnewBalance = findViewById(R.id.afterBalance);
        TextView newBalance = findViewById(R.id.balanceAfterLabel);
        deny = findViewById(R.id.deny);

        payCharge = findViewById(R.id.payable);
        if(!status.toLowerCase().equals("pending")) {
            payCharge.setVisibility(View.GONE);
            twnewBalance.setVisibility(View.GONE);
            newBalance.setVisibility(View.GONE);
            deny.setVisibility(View.GONE);
        }

        prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        final String email = prefs.getString("email", null);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("charges").child(intent.getStringExtra("id")).child("status").setValue("denied");
            }
        });

        Query user = mDatabase.child("users").orderByChild("email").equalTo(email);

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    amountValue = Double.parseDouble(item.child("balance").getValue().toString());
                }

                twcurrentBalance.setText("$ " + String.valueOf(round(amountValue, 2)));
                finalAmount = String.valueOf(round(amountValue - Double.parseDouble(amount), 2));
                twnewBalance.setText("$ " + String.valueOf(round(amountValue - Double.parseDouble(amount), 2)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        twpayable.setText(from);
        twamount.setText("$ " + amount);

        payCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("charges").child(intent.getStringExtra("id")).child("status").setValue("completed");

                Query user1 = mDatabase.child("users").orderByChild("email").equalTo(email);

                user1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String id = "";
                        for(DataSnapshot item : dataSnapshot.getChildren()) {
                            id = item.getKey();
                        }
                        mDatabase.child("users").child(id).child("balance").setValue(finalAmount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Query user2 = mDatabase.child("users").orderByChild("email").equalTo(twpayable.getText().toString());

                user2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String id = "";
                        double balance = 0;
                        for(DataSnapshot item : dataSnapshot.getChildren()) {
                            id = item.getKey();
                            balance = Double.parseDouble(item.child("balance").getValue().toString());
                        }
                        mDatabase.child("users").child(id).child("balance").setValue(String.valueOf(balance + Double.parseDouble(amount)));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
