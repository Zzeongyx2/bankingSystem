package com.example.bankingsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bankingsystem.Model.Account;
import com.example.bankingsystem.Model.Profile;
import com.example.bankingsystem.Model.db.ApplicationDB;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class TransferFragment extends Fragment {

    private Spinner spnSendingAccount;
    private EditText edtTransferAmount;
    private Spinner spnReceivingAccount;
    private Button btnConfirmTransfer;
    private Button btnConfirmAutoTransfer;
    private CountDownTimer CDT;
    ArrayList<Account> accounts;
    ArrayAdapter<Account> accountAdapter;

    SharedPreferences userPreferences;
    Gson gson;
    String json;
    Profile userProfile;

    public TransferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_transfer, container, false);

        spnSendingAccount = rootView.findViewById(R.id.spn_select_sending_acc);
        edtTransferAmount = rootView.findViewById(R.id.edt_transfer_amount);
        spnReceivingAccount = rootView.findViewById(R.id.spn_select_receiving_acc);
        btnConfirmTransfer = rootView.findViewById(R.id.btn_confirm_transfer);
        btnConfirmAutoTransfer = rootView.findViewById(R.id.btn_confirm_auto_transfer);

        setValues();

        return rootView;
    }

    /**
     * method used to setup the values for the views and fields
     */
    private void setValues() {

        userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);

        gson = new Gson();
        json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);

        btnConfirmTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmTransfer();
            }
        });

        btnConfirmAutoTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAutoTransfer();
            }
        });
        setAdapters();
    }

    /**
     * method that sets up the adapters
     */
    private void setAdapters() {
        accounts = userProfile.getAccounts();
        accountAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, accounts);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnSendingAccount.setAdapter(accountAdapter);
        spnReceivingAccount.setAdapter(accountAdapter);
        spnReceivingAccount.setSelection(1);
    }

    private void confirmAutoTransfer(){//10초마다 confirmTransfer() 실시


        CDT = new CountDownTimer(10 * 10000, 10000) {
            public void onTick(long millisUntilFinished) {
                //반복실행할 구문
                confirmTransfer();
            }
            public void onFinish() {
                //마지막에 실행할 구문
                CDT.start();
            }
        };

        CDT.start(); //CountDownTimer 실행

    }
    /**
     * method that confirms the transfer
     */
    private void confirmTransfer() {

        int receivingAccIndex = spnReceivingAccount.getSelectedItemPosition();
        boolean isNum = false;
        double transferAmount = 0;

        try {
            transferAmount = Double.parseDouble(edtTransferAmount.getText().toString());
            isNum = true;
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Please enter an amount to transfer", Toast.LENGTH_SHORT).show();
        }
        if (isNum) {
            if (spnSendingAccount.getSelectedItemPosition() == receivingAccIndex) {
                Toast.makeText(getActivity(), "You cannot make a transfer to the same account", Toast.LENGTH_SHORT).show();
            }
            else if(transferAmount < 0.01) {
                Toast.makeText(getActivity(), "The minimum amount for a transfer is $0.01", Toast.LENGTH_SHORT).show();

            } else if (transferAmount > userProfile.getAccounts().get(spnSendingAccount.getSelectedItemPosition()).getAccountBalance()) {

                Account acc = (Account) spnSendingAccount.getSelectedItem();
                Toast.makeText(getActivity(), "The account," + " " + acc.toString() + " " + "does not have sufficient funds to make this transfer", Toast.LENGTH_LONG).show();
            } else {

                int sendingAccIndex = spnSendingAccount.getSelectedItemPosition();
                
                Account sendingAccount = (Account) spnSendingAccount.getItemAtPosition(sendingAccIndex);
                Account receivingAccount = (Account) spnReceivingAccount.getItemAtPosition(receivingAccIndex);

                userProfile.addTransferTransaction(sendingAccount, receivingAccount, transferAmount);

                spnSendingAccount.setAdapter(accountAdapter);
                spnReceivingAccount.setAdapter(accountAdapter);

                spnSendingAccount.setSelection(sendingAccIndex);
                spnReceivingAccount.setSelection(receivingAccIndex);

                ApplicationDB applicationDb = new ApplicationDB(getActivity().getApplicationContext());

                applicationDb.overwriteAccount(userProfile, sendingAccount);
                applicationDb.overwriteAccount(userProfile, receivingAccount);

                applicationDb.saveNewTransaction(userProfile, sendingAccount.getAccountNo(),
                        sendingAccount.getTransactions().get(sendingAccount.getTransactions().size()-1));
                applicationDb.saveNewTransaction(userProfile, receivingAccount.getAccountNo(),
                        receivingAccount.getTransactions().get(receivingAccount.getTransactions().size()-1));


                SharedPreferences.Editor prefsEditor = userPreferences.edit();
                json = gson.toJson(userProfile);
                prefsEditor.putString("LastProfileUsed", json).apply();

                Toast.makeText(getActivity(), "Transfer of $" + String.format(Locale.getDefault(), "%.2f",transferAmount) + " successfully made", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
