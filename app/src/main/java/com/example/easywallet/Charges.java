package com.example.easywallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Charges extends AppCompatActivity {

    private SharedPreferences prefs;
    private DatabaseReference mDatabase;
    private ListView listview;
    private ArrayList<Charge> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charges);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        listview = findViewById(R.id.dyn);

        String email = prefs.getString("email", null);

        Query orders = mDatabase.child("charges").orderByChild("to_id").equalTo(email);

        orders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                values = new ArrayList<Charge>();

                for (DataSnapshot item:dataSnapshot.getChildren()) {
                    String from_id = item.child("from_id").getValue().toString();
                    String to_id = item.child("to_id").getValue().toString();
                    String status = item.child("status").getValue().toString();
                    String id = item.getKey();
                    Double amount = Double.parseDouble(item.child("amount").getValue().toString());
                    Charge charge = new Charge(id, amount, from_id, to_id, status);
                    values.add(charge);
                }
                ListAdapter adapter = new OrderViewAdapter(Charges.this, values);

                listview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String from = values.get(position).getFrom_id();
                String amount = String.valueOf(values.get(position).getAmount());
                String status = values.get(position).getStatus();
                String key = values.get(position).getId();
                Intent intent = new Intent(Charges.this, PayCharge.class);
                intent.putExtra("from", from);
                intent.putExtra("amount", amount);
                intent.putExtra("status", status);
                intent.putExtra("id", key);
                startActivity(intent);
            }
        });
    }
}
