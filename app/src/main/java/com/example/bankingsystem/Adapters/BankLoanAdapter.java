package com.example.bankingsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bankingsystem.Model.BankLoan;
import com.example.bankingsystem.Model.BankTransaction;
import com.example.bankingsystem.Model.db.GlobalStorage;
import com.example.bankingsystem.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BankLoanAdapter extends BaseAdapter {
    private ArrayList<BankLoan> bankLoanArrayList = new ArrayList<BankLoan>();
    private Context mContext;

    public BankLoanAdapter(Context context) {
        this.mContext = context;

        if (GlobalStorage.bankLoanHashMap.size() != 0) {
            for (String key : GlobalStorage.bankLoanHashMap.keySet()) {
                bankLoanArrayList.add(GlobalStorage.bankLoanHashMap.get(key));
            }
        }

    }

    @Override
    public void notifyDataSetChanged() {
        if (GlobalStorage.bankLoanHashMap.size() != 0) {
            for (String key : GlobalStorage.bankLoanHashMap.keySet()) {
                bankLoanArrayList.add(GlobalStorage.bankLoanHashMap.get(key));
            }
        }
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bankLoanArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.lst_loan, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.loanNameTextView = (TextView)view.findViewById(R.id.txt_loan_name);
            viewHolder.loanTimeStampTextView = (TextView)view.findViewById(R.id.txt_loan_timestamp);
            viewHolder.loanDeadLineTextView = (TextView)view.findViewById(R.id.txt_loan_deadline);
            viewHolder.loanAmountTextView = (TextView)view.findViewById(R.id.txt_loan_amount);

            view.setTag(viewHolder);
        }

        if (bankLoanArrayList.get(i) != null) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();

            viewHolder.loanNameTextView.setText(bankLoanArrayList.get(i).getAccountName());
            viewHolder.loanTimeStampTextView.setText(bankLoanArrayList.get(i).getLoan_timestamp());
            viewHolder.loanDeadLineTextView.setText(bankLoanArrayList.get(i).getDeadline());
            viewHolder.loanAmountTextView.setText(String.valueOf(bankLoanArrayList.get(i).getAmount()));
        }

        return view;
    }

    private static class ViewHolder {
        TextView loanNameTextView;
        TextView loanTimeStampTextView;
        TextView loanDeadLineTextView;
        TextView loanAmountTextView;
    }

}
