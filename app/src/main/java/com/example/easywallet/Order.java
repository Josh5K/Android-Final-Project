package com.example.easywallet;

public class Order {

    private int Id;
    private String Title;
    private String Description;
    private float Amount;
    private String Date;
    private int User_id;

    public Order(String title, String description, float amount, int user_id) {
        this.Title = title;
        this.Description = description;
        this.Amount = amount;
        this.User_id = user_id;
    }

    public int GetID() {
        return this.User_id;
    }

    public String GetTitle() {
        return this.Title;
    }

    public String GetDescription() {
        return this.Description;
    }

    public float GetAmount() {
        return this.Amount;
    }

    public int GetUserID() {
        return this.User_id;
    }

    public String GetDate() {
        return this.Date;
    }
}
