package com.example.bankingsystem.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Loan {
    public final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd - hh:mm a");
    private String accountName;
    private String loan_timestamp;
    private double amount;
    private long dbID;

    public Loan() {

    }

    public Loan(String accountName, double amount) {
        this.accountName = accountName;
        this.amount = amount;
    }

    public Loan(String accountName, String timestamp, double amount, long dbID) {
        this(accountName, amount);
        this.loan_timestamp = timestamp;
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
