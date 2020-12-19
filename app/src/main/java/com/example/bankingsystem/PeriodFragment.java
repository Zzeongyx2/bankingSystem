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
import com.example.bankingsystem.Model.BankLoan;
import com.example.bankingsystem.Model.Profile;
import com.example.bankingsystem.Model.db.GlobalStorage;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class PeriodFragment extends Fragment {
    private Spinner account;
    private TextView txtAccTitle;
    private TextView dtDate;
    private Button btnExpandPeriod;
    private String currentTime;
    private String deadline;

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
        dtDate = rootView.findViewById(R.id.txt_period);
        btnExpandPeriod = rootView.findViewById(R.id.btn_expand_period);

        setValues();

        return rootView;
    }

    /*private void setAdapters() {
        accounts = userProfile.getAccounts();
        accountAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, accounts);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        account.setAdapter(accountAdapter);
    }*/

    private void setValues() {

        userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);

        currentTime = GlobalStorage.bankLoanHashMap.get(GlobalStorage.currentSelectionAddressString).getDeadline();
        dtDate.setText(currentTime);

        gson = new Gson();
        json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);
        btnExpandPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmExpandPeriod();
            }
        });

        //setAdapters();
    }

    private void confirmExpandPeriod() {
        Account accs = userProfile.getAccounts(GlobalStorage.currentSelectionAddressString);
/*

        account.setAdapter(accountAdapter);

        SharedPreferences.Editor prefsEditor = userPreferences.edit();
        json = gson.toJson(userProfile);
        prefsEditor.putString("LastProfileUsed", json).apply();
*/


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        Date to;
        String dueTime = "";

        try {
            to = sdf.parse(GlobalStorage.bankLoanHashMap.get(GlobalStorage.currentSelectionAddressString).getDeadline());
            Calendar cal = Calendar.getInstance();
            cal.setTime(to);

            cal.add(Calendar.DATE, +3);
            dueTime = sdf.format(cal.getTime()).toString();

            BankLoan changeDueLoan = GlobalStorage.bankLoanHashMap.get(GlobalStorage.currentSelectionAddressString);
            changeDueLoan.setDeadline(dueTime);

            GlobalStorage.bankLoanHashMap.put(GlobalStorage.currentSelectionAddressString, changeDueLoan);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        dtDate.setText(dueTime);

        Toast.makeText(getActivity(), "대출 기한이 3일 연장되었습니다." , Toast.LENGTH_SHORT).show();
    }
}
