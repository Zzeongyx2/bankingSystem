package com.example.bankingsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.example.bankingsystem.Model.BankLoan;
import com.example.bankingsystem.Model.Loan;
import com.example.bankingsystem.Model.Profile;
import com.example.bankingsystem.Model.db.ApplicationDB;
import com.example.bankingsystem.Model.db.GlobalStorage;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class InterestFragment extends Fragment {
    private Spinner account;
    private TextView txtAccTitle;
    private TextView edtInterestAmount;
    private Button btnPayInterest;
    private double ChangeAmount;
    private double CurrentAmount;

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
        edtInterestAmount = rootView.findViewById(R.id.edt_interest_amount);
        btnPayInterest = rootView.findViewById(R.id.btn_pay_interest);

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
        btnPayInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPayInterest();
            }
        });

        setAdapters();

        CurrentAmount = (double) GlobalStorage.bankLoanHashMap.get(GlobalStorage.currentSelectionAddressString).getAmount();
        edtInterestAmount.setText(String.valueOf(CurrentAmount));

    }

    private void confirmPayInterest() {

            BankLoan changeLoanAmount = GlobalStorage.bankLoanHashMap.get(GlobalStorage.currentSelectionAddressString);

            if((changeLoanAmount.getAmount() == 0)){
                Toast.makeText(getActivity(), "You already Pay interest, Check your interest $" + String.format(String.valueOf(changeLoanAmount.getAmount())), Toast.LENGTH_SHORT).show();
            } else {
                int index = account.getSelectedItemPosition();

                changeLoanAmount.setAmount(0);

                GlobalStorage.bankLoanHashMap.put(GlobalStorage.currentSelectionAddressString, changeLoanAmount);

                ChangeAmount = Double.parseDouble(String.format("%.2f", GlobalStorage.bankLoanHashMap.get(GlobalStorage.currentSelectionAddressString).getAmount()));

                edtInterestAmount.setText(String.valueOf(ChangeAmount));

                Account accs = (Account) account.getItemAtPosition(index);

                userProfile.setLoan(accs, CurrentAmount);
                account.setAdapter(accountAdapter);

                SharedPreferences.Editor prefsEditor = userPreferences.edit();
                json = gson.toJson(userProfile);
                prefsEditor.putString("LastProfileUsed", json).apply();

                Toast.makeText(getActivity(), "Pay interest of $" + String.format("%.2f", CurrentAmount), Toast.LENGTH_SHORT).show();
            }
    }

}
