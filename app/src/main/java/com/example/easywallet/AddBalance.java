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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

public class AddBalance extends AppCompatActivity {


    private CardInputWidget mCardInputWidget;
    private Button validate;
    private SharedPreferences prefs;
    private DatabaseReference mDatabase;
    private String email;
    private TextView amountField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        amountField = findViewById(R.id.addAmount);

        email = prefs.getString("email", null);

        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        validate = findViewById(R.id.validate);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card cardToSave = mCardInputWidget.getCard();
                if (cardToSave == null) {
                    Toast.makeText(AddBalance.this, "Please fill out all fields.", Toast.LENGTH_LONG).show();
                }
                else {
                    Stripe stripe = new Stripe(AddBalance.this, "pk_test_QmFEdvNyHMgACVrZqj5rUqwB");
                    stripe.createToken(
                            cardToSave,
                            new TokenCallback() {
                                @Override
                                public void onError(Exception error) {
                                    Toast.makeText(AddBalance.this, "Error Saving Card.", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onSuccess(Token token) {
                                   Query user = mDatabase.child("users").orderByChild("email").equalTo(email);

                                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            double amount = 0;
                                            int id = 0;
                                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                                id = Integer.parseInt(item.getKey().toString());
                                                amount = Double.parseDouble(item.child("balance").getValue().toString());
                                            }

                                            amount += Double.parseDouble(amountField.getText().toString());
                                            mDatabase.child("users").child(String.valueOf(id)).child("balance").setValue(amount);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                    );
                }
            }
        });
    }
}
