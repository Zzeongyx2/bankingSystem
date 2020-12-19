package com.example.bankingsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bankingsystem.Model.BankTransaction;
import com.example.bankingsystem.R;

import java.util.ArrayList;

public class BankTransactionAdapter extends ArrayAdapter<BankTransaction> {

    private Context context;
    private int resource;

    public BankTransactionAdapter(Context context, int resource, ArrayList<BankTransaction> bankTransactions) {
        super(context, resource, bankTransactions);

        this.context = context;
        this.resource = resource;
    }

    @Override
    @NonNull
    public View getView (int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        BankTransaction bankTransactions = getItem(position);

        ImageView imgTransactionIcon = convertView.findViewById(R.id.img_transaction);
        TextView txtTransactionTitle = convertView.findViewById(R.id.txt_transaction_type_id);
        TextView txtTransactionTimestamp = convertView.findViewById(R.id.txt_transaction_timestamp);
        TextView txtTransactionInfo = convertView.findViewById(R.id.txt_transaction_info);
        txtTransactionInfo.setVisibility(View.VISIBLE);
        TextView txtTransactionAmount = convertView.findViewById(R.id.txt_transaction_amount);

        txtTransactionTitle.setText(bankTransactions.getTransactionType().toString() + " - " + bankTransactions.getTransactionID());
        txtTransactionTimestamp.setText(bankTransactions.getTimestamp());
        txtTransactionAmount.setText("Amount: $" + String.format("%.2f",bankTransactions.getAmount()));

        if (bankTransactions.getTransactionType() == BankTransaction.TRANSACTION_TYPE.PAYMENT) {
            imgTransactionIcon.setImageResource(R.drawable.lst_payment_icon);
            txtTransactionInfo.setText("To Payee: " + bankTransactions.getPayee());
            txtTransactionAmount.setTextColor(Color.RED);
        } else if (bankTransactions.getTransactionType() == BankTransaction.TRANSACTION_TYPE.TRANSFER) {
            imgTransactionIcon.setImageResource(R.drawable.lst_transfer_icon);
            txtTransactionInfo.setText("From : " +  bankTransactions.getSendingAccount() + " - " + "To : " + bankTransactions.getDestinationAccount());
            txtTransactionAmount.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_light));
        } else if (bankTransactions.getTransactionType() == BankTransaction.TRANSACTION_TYPE.DEPOSIT) {
            imgTransactionIcon.setImageResource(R.drawable.lst_deposit_icon);
            txtTransactionInfo.setVisibility(View.GONE);
            txtTransactionAmount.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
        }

        return convertView;
    }
}
