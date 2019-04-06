package com.example.easywallet;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    private int id;
    private String name;
    private double balance;
    private String email;
    private DatabaseReference db;

    public User(String name, double balance, String email) {
        this.name = name;
        this.balance = balance;
        this.email = email;
        db = FirebaseDatabase.getInstance().getReference();
    }

    public User(int id, String name, double balance, String email) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.email = email;
        db = FirebaseDatabase.getInstance().getReference();
    }

    public String getName() {
        return this.name;
    }

    public double getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public int setId(int id) {
        this.id = id;
        return id;
    }

    public double setBalance(double balance) {
        this.balance = balance;
        return balance;
    }

    public String setName(String name) {
        this.name = name;
        return name;
    }

    public String setEmail(String email) {
        this.email = email;
        return email;
    }

    public boolean save() {
        db.child("users").child(String.valueOf(this.id)).setValue(this);
        return true;
    }

    public boolean create() {
        return false;
    }

    public boolean delete() {
        return false;
    }
}
