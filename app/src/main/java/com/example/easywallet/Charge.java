package com.example.easywallet;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Charge {
    private String id;
    private double amount;
    private String from_id;
    private String to_id;
    private String status;
    private DatabaseReference db;

    public Charge() { }

    public Charge(double amount, String from_id, String to_id) {
        this.amount = amount;
        this.from_id = from_id;
        this.to_id = to_id;
        this.status = "Pending";
    }

    public Charge(double amount, String from_id, String to_id, String status) {
        this.amount = amount;
        this.from_id = from_id;
        this.to_id = to_id;
        this.status = status;
    }

    public Charge(String id, double amount, String from_id, String to_id, String status) {
        this.id = id;
        this.amount = amount;
        this.from_id = from_id;
        this.to_id = to_id;
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public String getFrom_id() {
        return from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public String getId() {
        return this.id;
    }

    public String getStatus() { return  this.status; }

    public double setAmount(double amount) {
        this.amount = amount;
        return amount;
    }

    public String setTo_id(String id) {
        this.to_id = id;
        return id;
    }

    public String setFrom_id(String id) {
        this.from_id = id;
        return id;
    }

    public boolean update() {
        return false;
    }

    public boolean save() {
        db = FirebaseDatabase.getInstance().getReference();
        db.child("charges").push().setValue(this);
        return false;
    }

    public boolean delete() {
        return  false;
    }

    @Override
    public String toString() {
        return getFrom_id() + " billed " + getTo_id() + " $" + getAmount() + "Status: " + getStatus();
    }
}
