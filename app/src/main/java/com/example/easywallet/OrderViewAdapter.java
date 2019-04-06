package com.example.easywallet;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class OrderViewAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<Charge> charges;

    public OrderViewAdapter(Activity context, ArrayList<Charge> charges) {
        super(context, R.layout.fragment_orders, charges);

        this.context = context;
        this.charges = charges;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_order_item, null,true);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.titleText);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.descriptionText);
        TextView dateTextField = (TextView) rowView.findViewById(R.id.dateText);
        TextView amountTextField = (TextView) rowView.findViewById(R.id.amountText);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

        Picasso.get().load("https://s.hswstatic.com/gif/money-market-1.jpg").into(imageView);

        nameTextField.setText("Billed by: " + charges.get(position).getFrom_id());
        infoTextField.setText("$" + String.valueOf(charges.get(position).getAmount()));
        dateTextField.setText("Status: " + charges.get(position).getStatus());
        return rowView;
    };
}