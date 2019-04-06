package com.example.easywallet;

import android.content.Context;
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

public class NewOrder extends AppCompatActivity {

    private TextView email;
    private TextView amount;
    private Button create;
    private String toEmail;
    private double toAmount;
    private SharedPreferences prefs;
    private DatabaseReference mDatabase;
    private String currentUserId;
    private String toUserId;
    private String currentUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        currentUserEmail = prefs.getString("email", null);

        email = findViewById(R.id.order_email);
        amount = findViewById(R.id.order_amount);
        create = findViewById(R.id.create_order);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save order to database.

                toAmount = Double.parseDouble(amount.getText().toString());
                toEmail = email.getText().toString();

                Charge charge = new Charge(toAmount, currentUserEmail, toEmail);
                charge.save();
            }
        });
    }
}
