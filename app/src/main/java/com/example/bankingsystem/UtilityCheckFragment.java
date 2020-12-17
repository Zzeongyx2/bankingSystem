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

public class UtilityCheckFragment extends Fragment {
    private TextView txt_water_input;
    private TextView txt_electricity_input;
    private TextView txt_gas_input;
    private TextView txt_total_input;
    private Button btnMakePayment;


    private int water;
    private int electricity;
    private int gas;
    /*private int waterfee;
    private int electricityfee;
    private int gasfee;

    private String water = String.valueOf(waterfee);
    private String electricity = String.valueOf(electricityfee);
    private String gas = String.valueOf(electricityfee);*/

    private ArrayList<Account> accounts;
    private ArrayAdapter<Account> accountAdapter;

    private ArrayList<Payee> payees;
    private ArrayAdapter<Payee> payeeAdapter;

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == btnMakePayment.getId()) {
                //pay로 화면전환
            }
        }
    };

    private Gson gson;
    private String json;
    private Profile userProfile;
    private SharedPreferences userPreferences;

    public UtilityCheckFragment() {
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
        txt_total_input = rootView.findViewById(R.id.txt_total_input);
        btnMakePayment = rootView.findViewById(R.id.btn_make_payment);

        setValues();

        return rootView;
    }

    /**
     * method used to setup the values for the views and fields
     */
    private void setValues() {
        water= 30;
        electricity = 20;
        gas = 10;

        txt_water_input.setText(String.valueOf(water) + "$");
        txt_electricity_input.setText(String.valueOf(electricity) + "$");
        txt_gas_input.setText(String.valueOf(gas) + "$");
        txt_total_input.setText(String.valueOf(water + electricity + gas) + "$");
        userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        gson = new Gson();
        json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);

        btnMakePayment.setOnClickListener(buttonClickListener);

        accounts = userProfile.getAccounts();
        accountAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, accounts);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        payees = userProfile.getPayees();

        payeeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, payees);
        payeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


    }


}
