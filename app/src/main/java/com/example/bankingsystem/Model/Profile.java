package com.example.bankingsystem.Model;

import java.util.ArrayList;

public class Profile {

    private String name;
    private String userId;
    private String password;
    private String phoneNum;
    private ArrayList<Account> accounts;
    private ArrayList<OpenBankAccount> bankAccounts;
    private ArrayList<Payee> payees;
    private long dbId;
    private String credit;

    public Profile (String name, String phoneNum, String userId, String password) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.userId = userId;
        this.password = password;
        bankAccounts = new ArrayList<>();
        accounts = new ArrayList<>();
        payees = new ArrayList<>();
        credit = "4등급";
    }

    public Profile (String name, String phoneNum, String userId, String password, long dbId) {
        this(name, phoneNum, userId, password);
        this.dbId = dbId;
    }

    public Profile(String name, String phoneNum){
        this.name = name;
        this.phoneNum = phoneNum;
    }


    /**
     * getters used to access the private fields of the profile
     */
    public String getCredit() {
        return credit;
    }

    public String getName() {
        return name;
    }
    public String getPhoneNum(){
        return phoneNum;
    }
    public String getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }
    public ArrayList<OpenBankAccount> getBankAccounts() { return bankAccounts; }
    public ArrayList<Account> getAccounts() { return accounts; }
    public Account getAccounts(String name) {
        if (name != null) {
            for (Account a : accounts) {
                if (name.contains(a.getAccountName())) {
                    return a;
                }
            }
        }
        return null;
    }
    public ArrayList<Payee> getPayees() { return payees; }
    public long getDbId() { return dbId; }
    public void setDbId(long dbId) { this.dbId = dbId; }

    public void addAccount(String accountName, double accountBalance) {
        String accNo = "A" + (accounts.size() + 1);
        Account account = new Account(accountName, accNo, accountBalance);
        accounts.add(account);
    }

    public void addBankAccount(String accountBank, String accountName, double accountBalance) {
        String accNo = "A" + (bankAccounts.size() + 1);
        OpenBankAccount bankaccounts = new OpenBankAccount(accountBank, accountName, accNo, accountBalance);
        bankAccounts.add(bankaccounts);
    }
    public void setAccountsFromDB(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public void setBankAccountsFromDB(ArrayList<OpenBankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public void addTransferTransaction(Account sendingAcc, Account receivingAcc, double transferAmount) {

        sendingAcc.setAccountBalance(sendingAcc.getAccountBalance() - transferAmount);
        receivingAcc.setAccountBalance(receivingAcc.getAccountBalance() + transferAmount);

        int sendingAccTransferCount = 0;
        int receivingAccTransferCount = 0;
        for (int i = 0; i < sendingAcc.getTransactions().size(); i ++) {
            if (sendingAcc.getTransactions().get(i).getTransactionType() == Transaction.TRANSACTION_TYPE.TRANSFER) {
                sendingAccTransferCount++;
            }
        }
        for (int i = 0; i < receivingAcc.getTransactions().size(); i++) {
            if (receivingAcc.getTransactions().get(i).getTransactionType() == Transaction.TRANSACTION_TYPE.TRANSFER) {
                receivingAccTransferCount++;
            }
        }

        sendingAcc.getTransactions().add(new Transaction("T" + (sendingAcc.getTransactions().size() + 1) + "-T" + (sendingAccTransferCount+1), sendingAcc.toTransactionString(), receivingAcc.toTransactionString(), transferAmount));
        receivingAcc.getTransactions().add(new Transaction("T" + (receivingAcc.getTransactions().size() + 1) + "-T" + (receivingAccTransferCount+1), sendingAcc.toTransactionString(), receivingAcc.toTransactionString(), transferAmount));
    }

    public void addTransferBankTransaction(OpenBankAccount sendingAcc, OpenBankAccount receivingAcc, double transferAmount) {

        sendingAcc.setAccountBalance(sendingAcc.getAccountBalance() - transferAmount);
        receivingAcc.setAccountBalance(receivingAcc.getAccountBalance() + transferAmount);

        int sendingAccTransferCount = 0;
        int receivingAccTransferCount = 0;

        for (int i = 0; i < sendingAcc.getTransactions().size(); i ++) {
            if (sendingAcc.getTransactions().get(i).getTransactionType() == BankTransaction.TRANSACTION_TYPE.TRANSFER) {
                sendingAccTransferCount++;
            }
        }
        for (int i = 0; i < receivingAcc.getTransactions().size(); i++) {
            if (receivingAcc.getTransactions().get(i).getTransactionType() == BankTransaction.TRANSACTION_TYPE.TRANSFER) {
                receivingAccTransferCount++;
            }
        }

        sendingAcc.getTransactions().add(new BankTransaction("T" + (sendingAcc.getTransactions().size() + 1) + "-T" + (sendingAccTransferCount+1), sendingAcc.toTransactionString(), receivingAcc.toTransactionString(), transferAmount));
        receivingAcc.getTransactions().add(new BankTransaction("T" + (receivingAcc.getTransactions().size() + 1) + "-T" + (receivingAccTransferCount+1), sendingAcc.toTransactionString(), receivingAcc.toTransactionString(), transferAmount));
    }

    public void addPayee(String payeeName) {
        String payeeID = "P" + (payees.size() + 1);
        Payee payee = new Payee(payeeID, payeeName);
        payees.add(payee);
    }

    public void getLoan(Account account, Double amount) {
        account.setAccountBalance(account.getAccountBalance() + amount);
    }

    public void setLoan(Account account, Double amount) {
        account.setAccountBalance(account.getAccountBalance() - amount);
    }

    public Double getBalance(Account account) {
        return account.getAccountBalance();
    }

    public void setPayeesFromDB(ArrayList<Payee> payees) {
        this.payees = payees;
    }
}
