package com.example.bankingsystem.Model.db;

import com.example.bankingsystem.Model.BankLoan;

import java.util.HashMap;

public class GlobalStorage {
    public static HashMap<String, BankLoan> bankLoanHashMap = new HashMap<String, BankLoan>();
    public static String currentSelectionAddressString = "";
}
