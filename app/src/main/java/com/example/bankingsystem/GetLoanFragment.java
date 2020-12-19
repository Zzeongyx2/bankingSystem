package com.example.bankingsystem;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankingsystem.Adapters.BankLoanAdapter;
import com.example.bankingsystem.Model.Account;
import com.example.bankingsystem.Model.BankLoan;
import com.example.bankingsystem.Model.Loan;
import com.example.bankingsystem.Model.Profile;
import com.example.bankingsystem.Model.db.ApplicationDB;
import com.example.bankingsystem.Model.db.GlobalStorage;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class GetLoanFragment extends Fragment {
    private Spinner account;
    private TextView txtAccTitle;
    private TextView txtLoanLimit;
    private TextView txtCredit;
    private EditText edtLoanAmount;
    private Button btnGetLoan;

    private Context loanContext;

    private ListView loanListView;

    private Double AccountBalance;

    private BankLoanAdapter bankLoanAdapter;

    private static String[] accountStrings;

    ArrayList<Account> accounts;
    ArrayAdapter<Account> accountAdapter;

    SharedPreferences userPreferences;
    Gson gson;
    String json;

    Profile userProfile;

    public GetLoanFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loanContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_loan, container, false);
        txtAccTitle = rootView.findViewById(R.id.txt_acc_title);
        account = rootView.findViewById(R.id.spn_select_acc);
        txtCredit = rootView.findViewById(R.id.txt_credit);
        edtLoanAmount = rootView.findViewById(R.id.edt_loan_amount);
        btnGetLoan = rootView.findViewById(R.id.btn_get_loan);
        txtLoanLimit = rootView.findViewById(R.id.txt_loan_limit);
        loanListView = rootView.findViewById(R.id.get_loan_listview);

        account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                accountStrings = adapterView.getItemAtPosition(i).toString().split(" ");

                GlobalStorage.currentSelectionAddressString = accountStrings[0];

                Account accs = userProfile.getAccounts(accountStrings[0]);

                if (accs.getAccountBalance() > 1000000) {
                    txtCredit.setText("1등급");
                    txtLoanLimit.setText("100000000");
                } else if (accs.getAccountBalance() > 100000) {
                    txtCredit.setText("2등급");
                    txtLoanLimit.setText("1000000");
                } else if (accs.getAccountBalance() > 10000) {
                    txtCredit.setText("3등급");
                    txtLoanLimit.setText("100000");
                } else if (accs.getAccountBalance() > 1000) {
                    txtCredit.setText("4등급");
                    txtLoanLimit.setText("10000");
                } else {
                    txtCredit.setText("5등급");
                    txtLoanLimit.setText("1000");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        setValues();

        bankLoanAdapter = new BankLoanAdapter(loanContext);
        loanListView.setAdapter(bankLoanAdapter);

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

        setAdapters();

        btnGetLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmGetLoan();
            }
        });

    }

    private void confirmGetLoan() {
        int index = account.getSelectedItemPosition();
        boolean isNum = false;
        boolean isCheckout = false;
        double loanAmount = 0;

        try {
            loanAmount = Double.parseDouble(edtLoanAmount.getText().toString());
            isNum = true;
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Please enter an amount to get a loan", Toast.LENGTH_SHORT).show();
        }

        if (isNum) {
            if (txtCredit.getText().equals("1등급")) {
                if (loanAmount > 100000000) {
                    Toast.makeText(getActivity(), "You cannot get a loan much than $100000000, Check your credit", Toast.LENGTH_LONG).show();

                } else {
                    isCheckout = true;
                }

            } else if (txtCredit.getText().equals("2등급")) {
                if (loanAmount > 1000000) {
                    Toast.makeText(getActivity(), "You cannot get a loan much than $1000000, Check your credit", Toast.LENGTH_LONG).show();

                } else {
                    isCheckout = true;
                }

            } else if (txtCredit.getText().equals("3등급")) {
                if (loanAmount > 100000) {
                    Toast.makeText(getActivity(), "You cannot get a loan much than $100000, Check your credit", Toast.LENGTH_LONG).show();

                } else {
                    isCheckout = true;
                }

            } else if (txtCredit.getText().equals("4등급")) {
                Log.d("GetLoanFragment", "---button--- " );
                if (loanAmount > 10000) {
                    Toast.makeText(getActivity(), "You cannot get a loan much than $10000, Check your credit", Toast.LENGTH_LONG).show();

                } else {
                    isCheckout = true;
                }

            } else if (txtCredit.getText().equals("5등급")) {
                if (loanAmount > 1000) {
                    Toast.makeText(getActivity(), "You cannot get a loan much than $1000, Check your credit", Toast.LENGTH_LONG).show();

                } else {
                    isCheckout = true;
                }

            }

        }

        if (isCheckout) {

            Account accs = userProfile.getAccounts(accountStrings[0]);
            userProfile.getLoan(accs, loanAmount);

            account.setAdapter(accountAdapter);

            SharedPreferences.Editor prefsEditor = userPreferences.edit();
            json = gson.toJson(userProfile);
            prefsEditor.putString("LastProfileUsed", json).apply();

            edtLoanAmount.getText().clear();

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            String currentTime = df.format(cal.getTime()).toString();
            cal.add(Calendar.DATE, +3);
            String dueTime = df.format(cal.getTime()).toString();

            System.out.println("current: " + currentTime);
            System.out.println("after: " + dueTime);

            BankLoan insertLoan = new BankLoan(accs.getAccountName(), currentTime, dueTime, loanAmount * 1.1);
            GlobalStorage.bankLoanHashMap.put(accountStrings[0], insertLoan);

            Toast.makeText(getActivity(), "Get Loan of $" + String.format(Locale.getDefault(), "%.2f", loanAmount), Toast.LENGTH_SHORT).show();


            for (String key : GlobalStorage.bankLoanHashMap.keySet()) {
                BankLoan b = GlobalStorage.bankLoanHashMap.get(key);

                Log.d("GetLoanFragment", "----- " + b.getAccountName() + " " + b.getLoan_timestamp() + " " + b.getDeadline() + " " + b.getAmount());
            }

            bankLoanAdapter.notifyDataSetChanged();
        }
    }
}
