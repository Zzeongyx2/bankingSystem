package com.example.bankingsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bankingsystem.Adapters.BankTransactionAdapter;
import com.example.bankingsystem.Model.BankTransaction;
import com.example.bankingsystem.Model.OpenBankAccount;
import com.example.bankingsystem.Model.Profile;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BankTransactionFragment extends Fragment {

    public enum TransactionTypeFilter {
        ALL_TRANSACTIONS(0),
        PAYMENTS(1),
        TRANSFERS(2),
        DEPOSITS(3);

        private final int transFilterID;
        TransactionTypeFilter(int transFilterID) {
            this.transFilterID = transFilterID;
        }

        public TransactionTypeFilter getTransFilter(int index) {
            for (TransactionTypeFilter filter : TransactionTypeFilter.values()) {
                if (filter.transFilterID == index) {
                    return filter;
                }
            }
            return null;
        }
    }

    public enum DateFilter {
        OLDEST_NEWEST(0),
        NEWEST_OLDEST(1);

        private final int dateFilterID;
        DateFilter(int dateFilterID) {
            this.dateFilterID = dateFilterID;
        }

        public DateFilter getDateFilter(int index) {
            for (DateFilter filter : DateFilter.values()) {
                if (filter.dateFilterID == index) {
                    return filter;
                }
            }
            return null;
        }

    }

    class BankTransactionComparator implements Comparator<BankTransaction> {
        public int compare(BankTransaction transOne, BankTransaction transTwo) {

            Date dateOne = null;
            Date dateTwo = null;

            try {
                dateOne = BankTransaction.DATE_FORMAT.parse(transOne.getTimestamp());
                dateTwo = BankTransaction.DATE_FORMAT.parse(transTwo.getTimestamp());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (dateOne.compareTo(dateTwo) > 0) {
                return (1);
            } else if (dateOne.compareTo(dateTwo) < 0) {
                return (-1);
            } else if (dateOne.compareTo(dateTwo) == 0) {
                return (1);
            }
            return (1);
        }
    }

    private TextView txtAccountName;
    private TextView txtAccountBalance;

    private TextView txtTransactionMsg;
    private TextView txtTransfersMsg;
    private TextView txtPaymentsMsg;
    private TextView txtDepositMsg;

    private Spinner spnAccounts;
    private Spinner spnTransactionTypeFilter;
    private Spinner spnDateFilter;

    private TransactionTypeFilter transFilter;
    private DateFilter dateFilter;

    Spinner.OnItemSelectedListener spnClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getId() == spnAccounts.getId()) {
                selectedAccountIndex = i;
                txtAccountName.setText("Account: " + userProfile.getBankAccounts().get(selectedAccountIndex).toTransactionString());
                txtAccountBalance.setText("Balance: $" + String.format(Locale.getDefault(), "%.2f",userProfile.getBankAccounts().get(selectedAccountIndex).getAccountBalance()));
            }
            else if (adapterView.getId() == spnTransactionTypeFilter.getId()) {
                transFilter = transFilter.getTransFilter(i);
            }
            else if (adapterView.getId() == spnDateFilter.getId()) {
                dateFilter = dateFilter.getDateFilter(i);
            }

            setupTransactionAdapter(selectedAccountIndex, transFilter, dateFilter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private ListView lstTransactions;

    private Profile userProfile;

    private int selectedAccountIndex;

    public BankTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        getActivity().setTitle("Bank Transactions");
        selectedAccountIndex = bundle.getInt("SelectedAccount", 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transaction, container, false);

        txtAccountName = rootView.findViewById(R.id.txt_account_name);
        txtAccountBalance = rootView.findViewById(R.id.txt_account_balance);

        txtTransactionMsg = rootView.findViewById(R.id.txt_no_transactions);
        txtPaymentsMsg = rootView.findViewById(R.id.txt_no_payments);
        txtTransfersMsg = rootView.findViewById(R.id.txt_no_transfers);
        txtDepositMsg = rootView.findViewById(R.id.txt_no_deposits);

        spnAccounts = rootView.findViewById(R.id.spn_accounts);
        spnTransactionTypeFilter = rootView.findViewById(R.id.spn_type_filter);
        spnDateFilter = rootView.findViewById(R.id.spn_date_filter);

        lstTransactions = rootView.findViewById(R.id.lst_transactions);

        ((DrawerActivity) getActivity()).showUpButton();

        setValues();
        return rootView;
    }

    /**
     * method used to setup the values for the views and fields
     */
    private void setValues() {

        SharedPreferences userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);

        transFilter = TransactionTypeFilter.ALL_TRANSACTIONS;
        dateFilter = DateFilter.OLDEST_NEWEST;

        setupTransactionAdapter(selectedAccountIndex, transFilter, dateFilter);

        setupSpinners();
        spnAccounts.setSelection(selectedAccountIndex);

        txtAccountName.setText("Account: " + userProfile.getBankAccounts().get(selectedAccountIndex).toTransactionString());
        txtAccountBalance.setText("Balance: $" + String.format(Locale.getDefault(), "%.2f",userProfile.getBankAccounts().get(selectedAccountIndex).getAccountBalance()));
    }

    private void setupSpinners() {

        ArrayAdapter<OpenBankAccount> bankAccountAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, userProfile.getBankAccounts());
        bankAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAccounts.setAdapter(bankAccountAdapter);

        ArrayAdapter<String> transTypeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.transaction_filters));
        transTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTransactionTypeFilter.setAdapter(transTypeAdapter);

        ArrayAdapter<String> dateFilterAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.date_filters));
        dateFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDateFilter.setAdapter(dateFilterAdapter);

        spnAccounts.setOnItemSelectedListener(spnClickListener);
        spnTransactionTypeFilter.setOnItemSelectedListener(spnClickListener);
        spnDateFilter.setOnItemSelectedListener(spnClickListener);

    }

    /**
     * method used to setup the adapters
     */
    private void setupTransactionAdapter(int selectedAccountIndex, TransactionTypeFilter transFilter, DateFilter dateFilter) {
        ArrayList<BankTransaction> bankTransactions = userProfile.getBankAccounts().get(selectedAccountIndex).getTransactions();

        txtDepositMsg.setVisibility(GONE);
        txtTransfersMsg.setVisibility(GONE);
        txtPaymentsMsg.setVisibility(GONE);

        if (bankTransactions.size() > 0) {

            txtTransactionMsg.setVisibility(GONE);
            lstTransactions.setVisibility(VISIBLE);

            if (dateFilter == DateFilter.OLDEST_NEWEST) {
                Collections.sort(bankTransactions, new BankTransactionComparator());
            } else if (dateFilter == DateFilter.NEWEST_OLDEST) {
                Collections.sort(bankTransactions, Collections.reverseOrder(new BankTransactionComparator()));
            }

            if (transFilter == TransactionTypeFilter.ALL_TRANSACTIONS) {
                BankTransactionAdapter bankTransactionAdapter = new BankTransactionAdapter(getActivity(), R.layout.lst_transactions, bankTransactions);
                lstTransactions.setAdapter(bankTransactionAdapter);
            }
            else if (transFilter == TransactionTypeFilter.PAYMENTS) {
                displayPayments(bankTransactions);
            }
            else if (transFilter == TransactionTypeFilter.TRANSFERS) {
                displayTransfers(bankTransactions);
            }
            else if (transFilter == TransactionTypeFilter.DEPOSITS) {
                displayDeposits(bankTransactions);
            }

        } else {
            txtTransactionMsg.setVisibility(VISIBLE);
            lstTransactions.setVisibility(GONE);
        }

    }

    private void displayPayments(ArrayList<BankTransaction> bankTransactions) {
        ArrayList<BankTransaction> payments = new ArrayList<>();

        for (int i = 0; i < bankTransactions.size(); i++) {
            if (bankTransactions.get(i).getTransactionType() == BankTransaction.TRANSACTION_TYPE.PAYMENT) {
                payments.add(bankTransactions.get(i));
            }
        }
        if (payments.size() == 0) {
            txtPaymentsMsg.setVisibility(VISIBLE);
            lstTransactions.setVisibility(GONE);
        } else {
            lstTransactions.setVisibility(VISIBLE);
            BankTransactionAdapter bankTransactionAdapter = new BankTransactionAdapter(getActivity(), R.layout.lst_bank_transactions, payments);
            lstTransactions.setAdapter(bankTransactionAdapter);
        }
    }

    private void displayTransfers(ArrayList<BankTransaction> bankTransactions) {
        ArrayList<BankTransaction> bankTransfers = new ArrayList<>();

        for (int i = 0; i < bankTransactions.size(); i++) {
            if (bankTransactions.get(i).getTransactionType() == BankTransaction.TRANSACTION_TYPE.TRANSFER) {
                bankTransfers.add(bankTransactions.get(i));
            }
        }
        if (bankTransfers.size() == 0) {
            txtTransfersMsg.setVisibility(VISIBLE);
            lstTransactions.setVisibility(GONE);
        } else {
            lstTransactions.setVisibility(VISIBLE);
            BankTransactionAdapter bankTransactionAdapter = new BankTransactionAdapter(getActivity(), R.layout.lst_transactions, bankTransfers);
            lstTransactions.setAdapter(bankTransactionAdapter);
        }
    }

    private void displayDeposits(ArrayList<BankTransaction> bankTransactions) {
        ArrayList<BankTransaction> deposits = new ArrayList<>();

        for (int i = 0; i < bankTransactions.size(); i++) {
            if (bankTransactions.get(i).getTransactionType() == BankTransaction.TRANSACTION_TYPE.DEPOSIT) {
                deposits.add(bankTransactions.get(i));
            }
        }
        if (deposits.size() == 0) {
            txtDepositMsg.setVisibility(VISIBLE);
            lstTransactions.setVisibility(GONE);
        } else {
            lstTransactions.setVisibility(VISIBLE);
            BankTransactionAdapter bankTransactionAdapter = new BankTransactionAdapter(getActivity(), R.layout.lst_transactions, deposits);
            lstTransactions.setAdapter(bankTransactionAdapter);
        }
    }

}
