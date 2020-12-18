package com.example.bankingsystem.Model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bankingsystem.Model.Account;
import com.example.bankingsystem.Model.BankTransaction;
import com.example.bankingsystem.Model.OpenBankAccount;
import com.example.bankingsystem.Model.Payee;
import com.example.bankingsystem.Model.Profile;
import com.example.bankingsystem.Model.Transaction;

import java.util.ArrayList;


public class ApplicationDB {

    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;

    private static final String DB_NAME = "userAccounts.db";
    private static final int DB_VERSION = 2;

    //------------------------------------------------------------------- PROFILE TABLE ----------------------- \\
    private static final String PROFILES_TABLE = "Profiles";

    private static final String PROFILE_ID = "_ProfileID";
    private static final String NAME = "name";
    private static final String PHONE = "phoneNum";
    private static final String USERNAME = "userId";
    private static final String PASSWORD = "password";

    private static final int PROFILE_ID_COLUMN = 0;
    private static final int NAME_COLUMN = 1;
    private static final int PHONE_COLUMN = 2;
    private static final int USERNAME_COLUMN = 3;
    private static final int PASSWORD_COLUMN = 4;
    //------------------------------------------------------------------- PROFILE TABLE ----------------------- \\

    //------------------------------------------------------------------- PAYEE TABLE ----------------------- \\
    private static final String PAYEES_TABLE = "Payees";

    private static final String PAYEE_ID = "_PayeeID";
    private static final String PAYEE_NAME = "PayeeName";

    private static final int PAYEE_ID_COLUMN = 1;
    private static final int PAYEE_NAME_COLUMN = 2;
    //------------------------------------------------------------------- PAYEE TABLE ----------------------- \\

    //------------------------------------------------------------------- ACCOUNT TABLE ----------------------- \\
    private static final String ACCOUNTS_TABLE = "Accounts";

    private static final String ACCOUNT_NO = "_AccountNo";
    private static final String ACCOUNT_NAME = "AccountName";
    private static final String ACCOUNT_BALANCE = "AccountBalance";

    private static final int ACCOUNT_NO_COLUMN = 1;
    private static final int ACCOUNT_NAME_COLUMN = 2;
    private static final int ACCOUNT_BALANCE_COLUMN = 3;
    //------------------------------------------------------------------- ACCOUNT TABLE ----------------------- \\

    //------------------------------------------------------------------- BANKACCOUNT TABLE ----------------------- \\
    private static final String BANK_ACCOUNTS_TABLE = "BankAccounts";

    private static final String BANK_ACCOUNT_NO = "Bank_AccountNo";
    private static final String BANK_ACCOUNT_BANK = "Bank_AccountBank";
    private static final String BANK_ACCOUNT_NAME = "BankAccountName";
    private static final String BANK_ACCOUNT_BALANCE = "BankAccountBalance";

    private static final int BANK_ACCOUNT_NO_COLUMN = 1;
    private static final int BANK_ACCOUNT_BANK_COLUMN = 2;
    private static final int BANK_ACCOUNT_NAME_COLUMN = 3;
    private static final int BANK_ACCOUNT_BALANCE_COLUMN = 4;
    //------------------------------------------------------------------- BANKACCOUNT TABLE ----------------------- \\

    //------------------------------------------------------------------- TRANSACTION TABLE ----------------------- \\
    private static final String TRANSACTIONS_TABLE = "Transactions";

    private static final String TRANSACTION_ID = "_TransactionID";
    private static final String TIMESTAMP = "Timestamp";
    private static final String SENDING_ACCOUNT = "SendingAccount";
    private static final String DESTINATION_ACCOUNT = "DestinationAccount";
    private static final String TRANSACTION_PAYEE = "Payee";
    private static final String TRANSACTION_AMOUNT = "Amount";
    private static final String TRANS_TYPE = "Type";

    private static final int TRANSACTION_ID_COLUMN = 2;
    private static final int TIMESTAMP_COLUMN = 3;
    private static final int SENDING_ACCOUNT_COLUMN = 4;
    private static final int DESTINATION_ACCOUNT_COLUMN = 5;
    private static final int TRANSACTION_PAYEE_COLUMN = 6;
    private static final int TRANSACTION_AMOUNT_COLUMN = 7;
    private static final int TRANSACTION_TYPE_COLUMN = 8;
    //------------------------------------------------------------------- TRANSACTION TABLE ----------------------- \\

    //------------------------------------------------------------------- TRANSACTION TABLE ----------------------- \\
    private static final String BANK_TRANSACTIONS_TABLE = "Bank_Transactions";

    private static final String BANK_TRANSACTION_ID = "Bank_TransactionID";
    private static final String BANK_TIMESTAMP = "BankTimestamp";
    private static final String BANK_SENDING_BANK_ACCOUNT = "BankSending_Bank_Account";
    private static final String BANK_DESTINATION_BANK_ACCOUNT = "BankDestination_Bank_Account";
    private static final String BANK_SENDING_ACCOUNT = "BankSendingAccount";
    private static final String BANK_DESTINATION_ACCOUNT = "BankDestinationAccount";
    private static final String BANK_TRANSACTION_PAYEE = "BankPayee";
    private static final String BANK_TRANSACTION_AMOUNT = "BankAmount";
    private static final String BANK_TRANS_TYPE = "BankType";

    private static final int BANK_TRANSACTION_ID_COLUMN = 2;
    private static final int BANK_TIMESTAMP_COLUMN = 3;
    private static final int BANK_SENDING_BANK_ACCOUNT_COLUMN = 4;
    private static final int BANK_DESTINATION_BANK_ACCOUNT_COLUMN = 5;
    private static final int BANK_SENDING_ACCOUNT_COLUMN = 6;
    private static final int BANK_DESTINATION_ACCOUNT_COLUMN = 7;
    private static final int BANK_TRANSACTION_PAYEE_COLUMN = 8;
    private static final int BANK_TRANSACTION_AMOUNT_COLUMN = 9;
    private static final int BANK_TRANSACTION_TYPE_COLUMN = 10;
    //------------------------------------------------------------------- TRANSACTION TABLE ----------------------- \\

    private static final String CREATE_PROFILES_TABLE =
            "CREATE TABLE " + PROFILES_TABLE + " (" +
                    PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME + " TEXT, " +
                    PHONE + " TEXT, " +
                    USERNAME + " TEXT, " +
                    PASSWORD + " TEXT)";

    private static final String CREATE_PAYEES_TABLE =
            "CREATE TABLE " + PAYEES_TABLE + " (" +
                    PROFILE_ID + " INTEGER NOT NULL, " +
                    PAYEE_ID + " TEXT NOT NULL, " +
                    PAYEE_NAME + " TEXT, " +
                    "PRIMARY KEY(" + PROFILE_ID + "," + PAYEE_ID + "), " +
                    "FOREIGN KEY(" + PROFILE_ID + ") REFERENCES " + PROFILES_TABLE + "(" + PROFILE_ID + "))";

    private static final String CREATE_ACCOUNTS_TABLE =
            "CREATE TABLE " + ACCOUNTS_TABLE + " (" +
                    PROFILE_ID + " INTEGER NOT NULL, " +
                    ACCOUNT_NO + " TEXT NOT NULL, " +
                    ACCOUNT_NAME + " TEXT, " +
                    ACCOUNT_BALANCE + " REAL, " +
                    "PRIMARY KEY(" + PROFILE_ID + "," + ACCOUNT_NO + "), " +
                    "FOREIGN KEY(" + PROFILE_ID + ") REFERENCES " + PROFILES_TABLE + "(" + PROFILE_ID + "))";

    private static final String CREATE_BANK_ACCOUNTS_TABLE =
            "CREATE TABLE " + BANK_ACCOUNTS_TABLE + " (" +
                    PROFILE_ID + " INTEGER NOT NULL, " +
                    BANK_ACCOUNT_NO + " TEXT NOT NULL, " +
                    BANK_ACCOUNT_BANK + " TEXT, " +
                    BANK_ACCOUNT_NAME + " TEXT, " +
                    BANK_ACCOUNT_BALANCE + " REAL, " +
                    "PRIMARY KEY(" + PROFILE_ID + "," + BANK_ACCOUNT_NO + "), " +
                    "FOREIGN KEY(" + PROFILE_ID + ") REFERENCES " + PROFILES_TABLE + "(" + PROFILE_ID + "))";

    private static final String CREATE_TRANSACTIONS_TABLE =
            "CREATE TABLE " + TRANSACTIONS_TABLE + " (" +
                    PROFILE_ID + " INTEGER NOT NULL, " +
                    ACCOUNT_NO + " TEXT NOT NULL, " +
                    TRANSACTION_ID + " TEXT NOT NULL, " +
                    TIMESTAMP + " TEXT, " +
                    SENDING_ACCOUNT + " TEXT, " +
                    DESTINATION_ACCOUNT + " TEXT, " +
                    TRANSACTION_PAYEE + " TEXT, " +
                    TRANSACTION_AMOUNT + " REAL, " +
                    TRANS_TYPE + " TEXT, " +
                    "PRIMARY KEY(" + PROFILE_ID + "," + ACCOUNT_NO + "," + TRANSACTION_ID + "), " +
                    "FOREIGN KEY(" + PROFILE_ID + "," + ACCOUNT_NO + ") REFERENCES " +
                    ACCOUNTS_TABLE + "(" + PROFILE_ID + "," + ACCOUNT_NO + ")," +
                    "FOREIGN KEY(" + PROFILE_ID + ") REFERENCES " + PROFILES_TABLE + "(" + PROFILE_ID + "))";

    private static final String CREATE_BANK_TRANSACTIONS_TABLE =
            "CREATE TABLE " + BANK_TRANSACTIONS_TABLE + " (" +
                    PROFILE_ID + " INTEGER NOT NULL, " +
                    BANK_ACCOUNT_NO + " TEXT NOT NULL, " +
                    BANK_TRANSACTION_ID + " TEXT NOT NULL, " +
                    BANK_TIMESTAMP + " TEXT, " +
                    BANK_SENDING_BANK_ACCOUNT + " TEXT, " +
                    BANK_DESTINATION_BANK_ACCOUNT + " TEXT, " +
                    BANK_SENDING_ACCOUNT + " TEXT, " +
                    BANK_DESTINATION_ACCOUNT + " TEXT, " +
                    BANK_TRANSACTION_PAYEE + " TEXT, " +
                    BANK_TRANSACTION_AMOUNT + " REAL, " +
                    BANK_TRANS_TYPE + " TEXT, " +
                    "PRIMARY KEY(" + PROFILE_ID + "," + BANK_ACCOUNT_NO + "," + BANK_TRANSACTION_ID + "), " +
                    "FOREIGN KEY(" + PROFILE_ID + "," + BANK_ACCOUNT_NO + ") REFERENCES " +
                    BANK_ACCOUNTS_TABLE + "(" + PROFILE_ID + "," + BANK_ACCOUNT_NO + ")," +
                    "FOREIGN KEY(" + PROFILE_ID + ") REFERENCES " + PROFILES_TABLE + "(" + PROFILE_ID + "))";

    public ApplicationDB(Context context){
        openHelper = new DBHelper(context, DB_NAME, DB_VERSION);
    }

    //TODO: Remove a profile?
    //TODO: Not needed unless I add implementation for modifying profile information such as name, password, username, etc.
    public void overwriteProfile(Profile profile) {

        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PROFILE_ID,profile.getDbId());
        cv.put(NAME,profile.getName());
        cv.put(PHONE,profile.getPhoneNum());
        cv.put(USERNAME,profile.getUserId());
        cv.put(PASSWORD,profile.getPassword());

        database.update(PROFILES_TABLE, cv, PROFILE_ID + "=?", new String[] {String.valueOf(profile.getDbId())});
        database.close();
    }

    public void saveNewProfile(Profile profile) {

        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, profile.getName());
        cv.put(PHONE, profile.getPhoneNum());
        cv.put(USERNAME, profile.getUserId());
        cv.put(PASSWORD, profile.getPassword());

        long id = database.insert(PROFILES_TABLE, null, cv);

        profile.setDbId(id);

        database.close();
    }

    //TODO: Overwrite or remove payee?
    public void saveNewPayee(Profile profile, Payee payee) {
        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(PROFILE_ID, profile.getDbId());
        cv.put(PAYEE_ID, payee.getPayeeID());
        cv.put(PAYEE_NAME, payee.getPayeeName());

        long id = database.insert(PAYEES_TABLE, null, cv);

        payee.setDbId(id);

        database.close();
    }

    public void saveNewTransaction(Profile profile, String accountNo, Transaction transaction) {
        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PROFILE_ID, profile.getDbId());
        cv.put(ACCOUNT_NO, accountNo);
        cv.put(TRANSACTION_ID, transaction.getTransactionID());
        cv.put(TIMESTAMP, transaction.getTimestamp());

        if (transaction.getTransactionType() == Transaction.TRANSACTION_TYPE.TRANSFER) {
            cv.put(SENDING_ACCOUNT, transaction.getSendingAccount());
            cv.put(DESTINATION_ACCOUNT, transaction.getDestinationAccount());
            cv.putNull(TRANSACTION_PAYEE);
        } else if (transaction.getTransactionType() == Transaction.TRANSACTION_TYPE.PAYMENT) {
            cv.putNull(SENDING_ACCOUNT);
            cv.putNull(DESTINATION_ACCOUNT);
            cv.put(TRANSACTION_PAYEE, transaction.getPayee());
        } else if (transaction.getTransactionType() == Transaction.TRANSACTION_TYPE.DEPOSIT) {
            cv.putNull(SENDING_ACCOUNT);
            cv.putNull(DESTINATION_ACCOUNT);
            cv.putNull(TRANSACTION_PAYEE);
        }

        cv.put(TRANSACTION_AMOUNT, transaction.getAmount());
        cv.put(TRANS_TYPE, transaction.getTransactionType().toString());

        long id = database.insert(TRANSACTIONS_TABLE, null, cv);

        transaction.setDbId(id);

        database.close();
    }

    public void saveNewBankTransaction(Profile profile, String accountNo, BankTransaction bankTransaction) {
        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PROFILE_ID, profile.getDbId());
        cv.put(BANK_ACCOUNT_NO, accountNo);
        cv.put(TRANSACTION_ID, bankTransaction.getTransactionID());
        cv.put(TIMESTAMP, bankTransaction.getTimestamp());

        if (bankTransaction.getTransactionType() == BankTransaction.TRANSACTION_TYPE.TRANSFER) {
            cv.put(BANK_SENDING_ACCOUNT, bankTransaction.getSendingAccount());
            cv.put(BANK_DESTINATION_ACCOUNT, bankTransaction.getDestinationAccount());
            cv.putNull(TRANSACTION_PAYEE);
        } else if (bankTransaction.getTransactionType() == BankTransaction.TRANSACTION_TYPE.PAYMENT) {
            cv.put(BANK_SENDING_ACCOUNT, bankTransaction.getSendingAccount());
            cv.put(BANK_DESTINATION_ACCOUNT, bankTransaction.getDestinationAccount());
            cv.put(TRANSACTION_PAYEE, bankTransaction.getPayee());
        } else if (bankTransaction.getTransactionType() == BankTransaction.TRANSACTION_TYPE.DEPOSIT) {
            cv.put(BANK_SENDING_ACCOUNT, bankTransaction.getSendingAccount());
            cv.put(BANK_DESTINATION_ACCOUNT, bankTransaction.getDestinationAccount());
            cv.putNull(TRANSACTION_PAYEE);
        }

        cv.put(TRANSACTION_AMOUNT, bankTransaction.getAmount());
        cv.put(TRANS_TYPE, bankTransaction.getTransactionType().toString());

        long id = database.insert(BANK_TRANSACTIONS_TABLE, null, cv);

        bankTransaction.setDbId(id);

        database.close();
    }

    //TODO: Remove an account?
    public void overwriteAccount(Profile profile, Account account) {

        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PROFILE_ID,profile.getDbId());
        cv.put(ACCOUNT_NO,account.getAccountNo());
        cv.put(ACCOUNT_NAME,account.getAccountName());
        cv.put(ACCOUNT_BALANCE, account.getAccountBalance());

        database.update(ACCOUNTS_TABLE, cv, PROFILE_ID + "=? AND " + ACCOUNT_NO +"=?",
                new String[] {String.valueOf(profile.getDbId()), account.getAccountNo()});
        database.close();
    }

    public void overwriteBankAccount(Profile profile, OpenBankAccount bankAccount) {

        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PROFILE_ID,profile.getDbId());
        cv.put(BANK_ACCOUNT_NO,bankAccount.getAccountNo());
        cv.put(BANK_ACCOUNT_BANK,bankAccount.getAccountBank());
        cv.put(BANK_ACCOUNT_NAME,bankAccount.getAccountName());
        cv.put(BANK_ACCOUNT_BALANCE, bankAccount.getAccountBalance());

        database.update(BANK_ACCOUNTS_TABLE, cv, PROFILE_ID + "=? AND " + BANK_ACCOUNT_NO +"=?",
                new String[] {String.valueOf(profile.getDbId()), bankAccount.getAccountNo()});
        database.close();
    }

    public void saveNewAccount(Profile profile, Account account) {

        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PROFILE_ID, profile.getDbId());
        cv.put(ACCOUNT_NO, account.getAccountNo());
        cv.put(ACCOUNT_NAME, account.getAccountName());
        cv.put(ACCOUNT_BALANCE, account.getAccountBalance());

        long id = database.insert(ACCOUNTS_TABLE, null, cv);

        account.setDbID(id);

        database.close();
    }

    public void saveNewBankAccount(Profile profile, OpenBankAccount bankAccount) {

        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PROFILE_ID, profile.getDbId());
        cv.put(BANK_ACCOUNT_NO, bankAccount.getAccountNo());
        cv.put(BANK_ACCOUNT_BANK, bankAccount.getAccountBank());
        cv.put(BANK_ACCOUNT_NAME, bankAccount.getAccountName());
        cv.put(BANK_ACCOUNT_BALANCE, bankAccount.getAccountBalance());

        long id = database.insert(BANK_ACCOUNTS_TABLE, null, cv);

        bankAccount.setDbID(id);

        database.close();
    }

    public ArrayList<Profile> getAllProfiles(){

        ArrayList<Profile> profiles = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(PROFILES_TABLE, null,null,null,null,
                null, null);
        getProfilesFromCursor(profiles, cursor);

        cursor.close();
        database.close();

        return profiles;
    }
    private void getProfilesFromCursor(ArrayList<Profile> profiles, Cursor cursor) {
        // returns true if pointed to a record
        while (cursor.moveToNext()){

            long id = cursor.getLong(PROFILE_ID_COLUMN);
            String name = cursor.getString(NAME_COLUMN);
            String phoneNum = cursor.getString(PHONE_COLUMN);
            String userId = cursor.getString(USERNAME_COLUMN);
            String password = cursor.getString(PASSWORD_COLUMN);

            //ArrayList<Account> accounts = new ArrayList<>();
            //ArrayList<Payee> payees = new ArrayList<>();

            profiles.add(new Profile(name, phoneNum, userId, password, id));
        }
    }

    public ArrayList<Payee> getPayeesFromCurrentProfile(long profileID) {
        ArrayList<Payee> payees = new ArrayList<>();
        database = openHelper.getReadableDatabase();

        Cursor cursor = database.query(PAYEES_TABLE, null, null, null, null,
                null ,null);
        getPayeesFromCursor(profileID, payees, cursor);

        cursor.close();
        database.close();

        return payees;
    }
    private void getPayeesFromCursor(long profileID, ArrayList<Payee> payees, Cursor cursor) {

        while (cursor.moveToNext()) {

            if (profileID == cursor.getLong(PROFILE_ID_COLUMN)) {
                long id = cursor.getLong(PROFILE_ID_COLUMN);
                String payeeID = cursor.getString(PAYEE_ID_COLUMN);
                String payeeName = cursor.getString(PAYEE_NAME_COLUMN);

                payees.add(new Payee(payeeID, payeeName, id));
            }
        }
    }

    public ArrayList<Transaction> getTransactionsFromCurrentAccount(long profileID, String accountNo) {

        ArrayList<Transaction> transactions = new ArrayList<>();
        database = openHelper.getReadableDatabase();

        Cursor cursor = database.query(TRANSACTIONS_TABLE, null, null, null, null,
                null ,null);

        getTransactionsFromCursor(profileID, accountNo, transactions, cursor);

        cursor.close();
        database.close();

        return transactions;
    }

    public ArrayList<BankTransaction> getTransactionsFromCurrentBankAccount(long profileID, String accountNo) {

        ArrayList<BankTransaction> BankTransactions = new ArrayList<>();
        database = openHelper.getReadableDatabase();

        Cursor cursor = database.query(BANK_TRANSACTIONS_TABLE, null, null, null, null,
                null ,null);

        getBankTransactionsFromCursor(profileID, accountNo, BankTransactions, cursor);

        cursor.close();
        database.close();

        return BankTransactions;
    }

    private void getTransactionsFromCursor(long profileID, String accountNo, ArrayList<Transaction> transactions, Cursor cursor) {

        while (cursor.moveToNext()) {

            if (profileID == cursor.getLong(PROFILE_ID_COLUMN)) {
                long id = cursor.getLong(PROFILE_ID_COLUMN);
                if (accountNo.equals(cursor.getString(ACCOUNT_NO_COLUMN))) {
                    String transactionID = cursor.getString(TRANSACTION_ID_COLUMN);
                    String timestamp = cursor.getString(TIMESTAMP_COLUMN);
                    String sendingAccount = cursor.getString(SENDING_ACCOUNT_COLUMN);
                    String destinationAccount = cursor.getString(DESTINATION_ACCOUNT_COLUMN);
                    String payee = cursor.getString(TRANSACTION_PAYEE_COLUMN);
                    double amount = cursor.getDouble(TRANSACTION_AMOUNT_COLUMN);
                    Transaction.TRANSACTION_TYPE transactionType = Transaction.TRANSACTION_TYPE.valueOf(cursor.getString(TRANSACTION_TYPE_COLUMN));

                    if (transactionType == Transaction.TRANSACTION_TYPE.PAYMENT) {
                        transactions.add(new Transaction(transactionID, timestamp, payee, amount, id));
                    } else if (transactionType == Transaction.TRANSACTION_TYPE.TRANSFER) {
                        transactions.add(new Transaction(transactionID, timestamp, sendingAccount, destinationAccount, amount, id));
                    } else if (transactionType == Transaction.TRANSACTION_TYPE.DEPOSIT) {
                        transactions.add(new Transaction(transactionID, timestamp, amount, id));
                    }
                }

            }
        }
    }

    private void getBankTransactionsFromCursor(long profileID, String accountNo, ArrayList<BankTransaction> bankTransactions, Cursor cursor) {

        while (cursor.moveToNext()) {

            if (profileID == cursor.getLong(PROFILE_ID_COLUMN)) {
                long id = cursor.getLong(PROFILE_ID_COLUMN);
                if (accountNo.equals(cursor.getString(BANK_ACCOUNT_NO_COLUMN))) {
                    String transactionID = cursor.getString(BANK_TRANSACTION_ID_COLUMN);
                    String timestamp = cursor.getString(BANK_TIMESTAMP_COLUMN);
                    String sendingAccount = cursor.getString(BANK_SENDING_ACCOUNT_COLUMN);
                    String destinationAccount = cursor.getString(BANK_DESTINATION_ACCOUNT_COLUMN);
                    String payee = cursor.getString(BANK_TRANSACTION_PAYEE_COLUMN);
                    double amount = cursor.getDouble(BANK_TRANSACTION_AMOUNT_COLUMN);
                    BankTransaction.TRANSACTION_TYPE transactionType = BankTransaction.TRANSACTION_TYPE.valueOf(cursor.getString(BANK_TRANSACTION_TYPE_COLUMN));

                    if (transactionType == BankTransaction.TRANSACTION_TYPE.PAYMENT) {
                        bankTransactions.add(new BankTransaction(transactionID, timestamp, payee, amount, id));
                    } else if (transactionType == BankTransaction.TRANSACTION_TYPE.TRANSFER) {
                        bankTransactions.add(new BankTransaction(transactionID, timestamp, sendingAccount, destinationAccount, amount, id));
                    } else if (transactionType == BankTransaction.TRANSACTION_TYPE.DEPOSIT) {
                        bankTransactions.add(new BankTransaction(transactionID, timestamp, amount, id));
                    }
                }

            }
        }
    }

    public ArrayList<Account> getAccountsFromCurrentProfile(long profileID) {

        ArrayList<Account> accounts = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(ACCOUNTS_TABLE, null, null, null, null,
                null ,null);
        getAccountsFromCursor(profileID, accounts, cursor);

        cursor.close();
        database.close();

        return accounts;
    }

    public ArrayList<OpenBankAccount> getBankAccountsFromCurrentProfile(long profileID) {

        ArrayList<OpenBankAccount> bankAccounts = new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(BANK_ACCOUNTS_TABLE, null, null, null, null,
                null ,null);
        getBankAccountsFromCursor(profileID, bankAccounts, cursor);

        cursor.close();
        database.close();

        return bankAccounts;
    }

    private void getAccountsFromCursor(long profileID, ArrayList<Account> accounts, Cursor cursor) {

        while (cursor.moveToNext()) {

            if (profileID == cursor.getLong(PROFILE_ID_COLUMN)) {
                long id = cursor.getLong(PROFILE_ID_COLUMN);
                String accountNo = cursor.getString(ACCOUNT_NO_COLUMN);
                String accountName = cursor.getString(ACCOUNT_NAME_COLUMN);
                double accountBalance = cursor.getDouble(ACCOUNT_BALANCE_COLUMN);

                accounts.add(new Account(accountName, accountNo, accountBalance, id));
            }
        }
    }

    private void getBankAccountsFromCursor(long profileID, ArrayList<OpenBankAccount> bankAccounts, Cursor cursor) {

        while (cursor.moveToNext()) {

            if (profileID == cursor.getLong(PROFILE_ID_COLUMN)) {
                long id = cursor.getLong(PROFILE_ID_COLUMN);
                String accountNo = cursor.getString(BANK_ACCOUNT_NO_COLUMN);
                String accountBank = cursor.getString(BANK_ACCOUNT_BANK_COLUMN);
                String accountName = cursor.getString(BANK_ACCOUNT_NAME_COLUMN);
                double accountBalance = cursor.getDouble(BANK_ACCOUNT_BALANCE_COLUMN);

                bankAccounts.add(new OpenBankAccount(accountBank, accountName, accountNo, accountBalance, id));
            }
        }
    }

    private static class DBHelper extends SQLiteOpenHelper {

        private DBHelper(Context context, String name, int version) {
            super(context, name, null, version);
        }

        // if the db doesn't exist , the runtime calls this fn . we dont have to check if it exists
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_PROFILES_TABLE);
            db.execSQL(CREATE_PAYEES_TABLE);
            db.execSQL(CREATE_ACCOUNTS_TABLE);
            db.execSQL(CREATE_BANK_ACCOUNTS_TABLE);
            db.execSQL(CREATE_TRANSACTIONS_TABLE);
            db.execSQL(CREATE_BANK_TRANSACTIONS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // drop the table
            db.execSQL("DROP TABLE IF EXISTS " + PROFILES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PAYEES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + BANK_ACCOUNTS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + BANK_TRANSACTIONS_TABLE);
            onCreate(db);
        }
    }
}
