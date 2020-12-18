package com.example.bankingsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

public class PeriodFragment extends Fragment {
    private Spinner account;
    private TextView txtAccTitle;
    private TextView txtCredit;
    private DatePicker dtDate;
    private Button btnExpandPeriod;

    ArrayList<Account> accounts;
    ArrayAdapter<Account> accountAdapter;

    SharedPreferences userPreferences;
    Gson gson;
    String json;

    Profile userProfile;

    public PeriodFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_period, container, false);
        txtAccTitle = rootView.findViewById(R.id.txt_acc_title);
        account = rootView.findViewById(R.id.spn_select_acc);
        txtCredit = rootView.findViewById(R.id.txt_credit);
        dtDate = rootView.findViewById(R.id.dp_date);
        btnExpandPeriod = rootView.findViewById(R.id.btn_expand_period);

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
        btnExpandPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmGetLoan();
            }
        });

        setAdapters();
    }

    private void confirmGetLoan() {
        int day = dtDate.getDayOfMonth();
        int month = dtDate.getMonth();
        int year = dtDate.getYear();
        Toast.makeText(getActivity(), year + "년" + (month + 1) + "월" + day + "일까지 대출 기한이 연장되었습니다." , Toast.LENGTH_SHORT).show();
    }
}
