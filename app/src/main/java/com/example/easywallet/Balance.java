package com.example.easywallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.preference.PreferenceManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.util.JsonMapper;

import org.json.JSONArray;
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Balance.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Balance#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Balance extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button addBalance;
    private View rootView;
    private SharedPreferences prefs;
    private DatabaseReference mDatabase;
    private double amount;
    private TextView amountField;
    private Object currentUser;
    private String email;

    public Balance() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Balance.
     */
    // TODO: Rename and change types and number of parameters
    public static Balance newInstance(String param1, String param2) {
        Balance fragment = new Balance();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_balance, null, true);

        addBalance = rootView.findViewById(R.id.add_balance);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        amountField = rootView.findViewById(R.id.balance);

        prefs = getContext().getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        email = prefs.getString("email", null);

        setBalance();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());

        String name = settings.getString("display_name", null);
        String city = settings.getString("city", null);


        if(name != null && city != null) {
            TextView welcome = rootView.findViewById(R.id.welcome);
            welcome.setText("Hi " + name + ". Welcome to your city of " + city + " mobile money transfer app!");
        }

        addBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddBalance.class);
                startActivity(intent);
            }
        });

        return rootView;
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

    public void setBalance() {
        Query user = mDatabase.child("users").orderByChild("email").equalTo(email);

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    currentUser = JsonMapper.parseJson(dataSnapshot.getValue().toString());
                }
                catch (Exception e) {
                    Log.d("EX", "TryCatch");
                }

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    amount = Double.parseDouble(item.child("balance").getValue().toString());
                }

                amountField.setText("$" + String.valueOf(amount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        setBalance();
        super.onResume();
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
