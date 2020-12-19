package com.example.bankingsystem.Model;

public class BankLoan {
    private String accountName;
    private String loan_timestamp;
    private String deadline;
    private double amount;

    public BankLoan() {
        this.accountName = "";
        this.loan_timestamp = "";
        this.deadline = "";
        this.amount = 0;
    }

    public BankLoan(String accountName, String loan_timestamp, String deadline, double amount) {
        this.accountName = accountName;
        this.loan_timestamp = loan_timestamp;
        this.deadline = deadline;
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getLoan_timestamp() {
        return loan_timestamp;
    }

    public void setLoan_timestamp(String loan_timestamp) {
        this.loan_timestamp = loan_timestamp;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
