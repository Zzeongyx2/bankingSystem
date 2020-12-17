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
    private Spinner spnSelectAccount;

    private ArrayList<Account> accounts;
    private ArrayAdapter<Account> accountAdapter;

    private int water = 30;
    private int electricity = 20;
    private int gas = 10;



    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == btnMakePayment.getId()) {
                //pay
                makePayment();
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
        spnSelectAccount = rootView.findViewById(R.id.select_acc);
        setValues();

        return rootView;
    }

    /**
     * method used to setup the values for the views and fields
     */
    private void setValues() {

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

        spnSelectAccount.setAdapter(accountAdapter);

    }

    /**
     * method that makes a payment
     */
    private void makePayment() {

        boolean isNum = false;
        double paymentAmount = 0;

        try {
            paymentAmount = Double.parseDouble(String.valueOf(water+electricity+gas));
            if (Double.parseDouble(String.valueOf(water+electricity+gas)) >= 0.01) {
                isNum = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isNum) {

            int selectedAccountIndex = spnSelectAccount.getSelectedItemPosition();

            if (paymentAmount > userProfile.getAccounts().get(selectedAccountIndex).getAccountBalance()) {
                Toast.makeText(getActivity(), "You do not have sufficient funds to make this payment", Toast.LENGTH_SHORT).show();
            } else {

                userProfile.getAccounts().get(selectedAccountIndex).addPaymentTransaction("Utility Bill", paymentAmount);

                accounts = userProfile.getAccounts();
                spnSelectAccount.setAdapter(accountAdapter);
                spnSelectAccount.setSelection(selectedAccountIndex);

                ApplicationDB applicationDb = new ApplicationDB(getActivity().getApplicationContext());
                applicationDb.saveNewTransaction(userProfile, userProfile.getAccounts().get(selectedAccountIndex).getAccountNo(), userProfile.getAccounts().get(selectedAccountIndex).getTransactions().get(userProfile.getAccounts().get(selectedAccountIndex).getTransactions().size()-1));
                applicationDb.overwriteAccount(userProfile, userProfile.getAccounts().get(selectedAccountIndex));

                SharedPreferences.Editor prefsEditor = userPreferences.edit();
                gson = new Gson();
                json = gson.toJson(userProfile);
                prefsEditor.putString("LastProfileUsed", json).apply();

                Toast.makeText(getActivity(), "Utility Bill Payment of $" + String.format(Locale.getDefault(), "%.2f", paymentAmount) + " successfully made", Toast.LENGTH_SHORT).show();
                water = 0;
                electricity = 0;
                gas = 0;
                setValues();
            }
        } else {
            Toast.makeText(getActivity(), "There's nothing to Pay!" , Toast.LENGTH_SHORT).show();
        }
    }


}
