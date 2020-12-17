package com.example.bankingsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.bankingsystem.Model.Loan;
import com.example.bankingsystem.Model.Profile;
import com.example.bankingsystem.Model.db.ApplicationDB;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class InterestFragment extends Fragment {
    private Spinner account;
    private TextView txtAccTitle;
    private TextView txtCredit;
    private EditText edtInterestAmount;
    private Button btnGetLoan;

    ArrayList<Account> accounts;
    ArrayAdapter<Account> accountAdapter;

    SharedPreferences userPreferences;
    Gson gson;
    String json;

    Profile userProfile;

    public InterestFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_interest, container, false);
        txtAccTitle = rootView.findViewById(R.id.txt_acc_title);
        account = rootView.findViewById(R.id.spn_select_acc);
        txtCredit = rootView.findViewById(R.id.txt_credit);
        edtInterestAmount = rootView.findViewById(R.id.edt_interest_amount);
        btnGetLoan = rootView.findViewById(R.id.btn_pay_interest);

        setValues();

        return rootView;
    }

    private void setAdapters() {
        accounts = userProfile.getAccounts();
        accountAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, accounts);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        account.setAdapter(accountAdapter);
    }

    private void setValues() {

        userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);

        gson = new Gson();
        json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);
        txtCredit.setText(userProfile.getCredit());
        btnGetLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPayInterest();
            }
        });

        setAdapters();
    }

    private void confirmPayInterest() {
        int index = account.getSelectedItemPosition();
        boolean isNum = false;
        double loanAmount = 0;

        try {
            loanAmount = Double.parseDouble(edtInterestAmount.getText().toString());
            isNum = true;
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Please enter an amount to get a loan", Toast.LENGTH_SHORT).show();
        }

        if (isNum) {
            if (loanAmount > userProfile.getAccounts().get(index).getAccountBalance()) {
                Toast.makeText(getActivity(), "You cannot pay a interest much than your balance", Toast.LENGTH_LONG).show();
            }
            else {
                Account a = (Account) account.getItemAtPosition(index);
                userProfile.getLoan(a, -loanAmount);
                account.setAdapter(accountAdapter);

                ApplicationDB applicationDb = new ApplicationDB(getActivity().getApplicationContext());
                applicationDb.overwriteAccount(userProfile, a);

                SharedPreferences.Editor prefsEditor = userPreferences.edit();
                json = gson.toJson(userProfile);
                prefsEditor.putString("LastProfileUsed", json).apply();

                edtInterestAmount.getText().clear();

                Toast.makeText(getActivity(), "Pay interest of $" + String.format(Locale.getDefault(), "%.2f",loanAmount) , Toast.LENGTH_SHORT).show();
            }
        }
    }
}
