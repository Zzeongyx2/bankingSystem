package com.example.bankingsystem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankingsystem.Model.Account;
import com.example.bankingsystem.Model.Payee;
import com.example.bankingsystem.Model.Profile;
import com.example.bankingsystem.Model.db.ApplicationDB;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UtilityBillCheckFragment extends Fragment {


    private TextView txt_water_input;
    private TextView txt_electricity_input;
    private TextView txt_gas_input;
    private Button btnMakePayment;

    private int water;
    private int electricity;
    private int gas;
    SharedPreferences userPreferences;
    Gson gson;
    String json;
    Profile userProfile;

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == btnMakePayment.getId()) {
                //페이로 넘어가기
            }
        }
    };

    public UtilityBillCheckFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_utilitycheck, container, false);


        txt_water_input = rootView.findViewById(R.id.txt_water_input);
        txt_electricity_input = rootView.findViewById(R.id.txt_elec_input);
        txt_gas_input = rootView.findViewById(R.id.txt_gas_input);
        btnMakePayment = rootView.findViewById(R.id.btn_make_payment);

        setValues();

        return rootView;
    }

    /**
     * method used to setup the values for the views and fields
     */
    private void setValues() {
        water = 50;
        electricity = 30;
        gas = 20;
        userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);

        gson = new Gson();
        json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);


        txt_water_input.setText(water);
        txt_electricity_input.setText(electricity);
        txt_gas_input.setText(gas);

        btnMakePayment.setOnClickListener(buttonClickListener);

    }

}
