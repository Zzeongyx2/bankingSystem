package com.example.bankingsystem.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Loan {
    public final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd - hh:mm a");
    private String accountName;
    private String loan_timestamp;
    private String deadline;
    private ArrayList<Transaction> transactions;
    private double amount;
    private long dbID;

    public Loan() {

    }
    public Loan(String accountName,  double amount) {
        this.accountName = accountName;
        this.amount = amount;
    }

    public Loan(String accountName, String timestamp, double amount) {
        this.loan_timestamp = timestamp;
        this.accountName = accountName;
        this.amount = amount;
        transactions = new ArrayList<>();

    }

//    public Loan(String accountName, Stri)

    public Loan(String accountName, String timestamp, double amount, long dbID) {
        this(accountName, timestamp, amount);
        this.dbID = dbID;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getLoan_timestamp() {
        return loan_timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setDbId(long dbID) {
        this.dbID = dbID;
    }

    public long getDbID() {
        return dbID;
    }
}
