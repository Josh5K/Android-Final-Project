package com.example.easywallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.GetChars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Orders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Orders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Orders extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences prefs;
    private DatabaseReference mDatabase;
    private ListView listview;
    private View rootView;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Charge> values;

    public Orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Orders.
     */
    // TODO: Rename and change types and number of parameters
    public static Orders newInstance(String param1, String param2) {
        Orders fragment = new Orders();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_orders, container, true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        prefs = getContext().getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        listview = rootView.findViewById(R.id.dyn);

        String email = prefs.getString("email", null);

        Query orders = mDatabase.child("charges").orderByChild("to_id").equalTo(email);
        Log.d("USER22", "Made it 1");

        orders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("USER22", "Made it 2");

                values = new ArrayList<Charge>();

                for (DataSnapshot item:dataSnapshot.getChildren()) {
                    String from_id = item.child("from_id").getValue().toString();
                    String to_id = item.child("to_id").getValue().toString();
                    String status = item.child("status").getValue().toString();
                    Double amount = Double.parseDouble(item.child("amount").getValue().toString());
                    Charge charge = new Charge(amount, from_id, to_id, status);
                    Log.d("USER22", charge.toString());
                    values.add(charge);
                }
                ListAdapter adapter = new OrderViewAdapter(getActivity(), values);

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
                Intent intent = new Intent(getContext(), PayCharge.class);
                intent.putExtra("from", from);
                intent.putExtra("amount", amount);
                intent.putExtra("status", status);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
