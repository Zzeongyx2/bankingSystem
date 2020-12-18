package com.example.bankingsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bankingsystem.Model.OpenBankAccount;
import com.example.bankingsystem.R;

import java.util.ArrayList;

/**
 * Adapter used for displaying accounts
 */

public class BankAccountAdapter extends ArrayAdapter<OpenBankAccount> {

    private Context context;
    private int resource;

    public BankAccountAdapter(Context context, int resource, ArrayList<OpenBankAccount> bankAccounts) {
        super(context, resource, bankAccounts);

        this.context = context;
        this.resource = resource;
    }

    /**
     * function that gets the view from the adapter
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    @NonNull
    public View getView (int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        OpenBankAccount bankAccount = getItem(position);

        TextView txtAccountName = convertView.findViewById(R.id.txt_account_name);
        txtAccountName.setText(bankAccount.getAccountName());

        TextView txtAccountBank = convertView.findViewById(R.id.txt_account_bank);
        txtAccountBank.setText(bankAccount.getAccountBank());

        TextView txtAccountNo = convertView.findViewById(R.id.txt_acc_no);
        txtAccountNo.setText(context.getString(R.string.account_no) + " " + bankAccount.getAccountNo());

        TextView txtAccountBalance = convertView.findViewById(R.id.txt_balance);
        txtAccountBalance.setText("Account balance: $" + String.format("%.2f",bankAccount.getAccountBalance()));

        return convertView;
    }
}
