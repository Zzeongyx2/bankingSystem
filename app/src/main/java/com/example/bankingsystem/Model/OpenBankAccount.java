package com.example.bankingsystem.Model;

import java.util.ArrayList;
import java.util.Locale;

public class OpenBankAccount {
    private String accountBank;
    private String accountName;
    private String accountNo;
    private double accountBalance;
    private ArrayList<BankTransaction> bankTransactions;
    private long dbID;

    public OpenBankAccount (String accountBank, String accountName, String accountNo, double accountBalance) {
        this.accountBank = accountBank;
        this.accountName = accountName;
        this.accountNo = accountNo;
        this.accountBalance = accountBalance;
        bankTransactions = new ArrayList<>();
    }

    public OpenBankAccount (String accountBank, String accountName, String accountNo, double accountBalance, long dbID) {
        this(accountBank, accountName, accountNo, accountBalance);
        this.dbID = dbID;
    }

    /**
     * Getters for the account name, number and balance
     */
    public String getAccountBank() {
        return accountBank;
    }
    public String getAccountName() {
        return accountName;
    }
    public String getAccountNo() {
        return accountNo;
    }
    public double getAccountBalance() {
        return accountBalance;
    }

    public void setDbID(long dbID) { this.dbID = dbID; }

    public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }

    public ArrayList<BankTransaction> getTransactions() {
        return bankTransactions;
    }

    public void addPaymentTransaction (String payee, double amount) {
        accountBalance -= amount;

        int paymentCount = 0;

        for (int i = 0; i < bankTransactions.size(); i++) {
            if (bankTransactions.get(i).getTransactionType() == BankTransaction.TRANSACTION_TYPE.PAYMENT)  {
                paymentCount++;
            }
        }

        BankTransaction payment = new BankTransaction("T" + (bankTransactions.size() + 1) + "-P" + (paymentCount+1), payee, amount);
        bankTransactions.add(payment);
    }

    public void addDepositTransaction(double amount) {
        accountBalance += amount;

        //TODO: Could be a better way - ie. each time a deposit is added, add it to the master count (global variable - persisted?)
        int depositsCount = 0;

        for (int i = 0; i < bankTransactions.size(); i++) {
            if (bankTransactions.get(i).getTransactionType() == BankTransaction.TRANSACTION_TYPE.DEPOSIT)  {
                depositsCount++;
            }
        }

        BankTransaction deposit = new BankTransaction("T" + (bankTransactions.size() + 1) + "-D" + (depositsCount+1), amount);
        bankTransactions.add(deposit);
    }

    public String toString() {
        return (accountBank + " - " + accountName + " ($" + String.format(Locale.getDefault(), "%.2f",accountBalance) + ")");
    }

    public String toTransactionString() { return (accountBank + " - " + accountName + " (" + accountNo + ")"); }

    public void setTransactions(ArrayList<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }
}
