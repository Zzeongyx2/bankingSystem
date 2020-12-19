package com.example.bankingsystem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankingsystem.Model.Account;
import com.example.bankingsystem.Model.OpenBankAccount;
import com.example.bankingsystem.Model.Profile;
import com.example.bankingsystem.Model.db.ApplicationDB;
import com.google.gson.Gson;

import java.util.Locale;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public enum manualNavID {
        ACCOUNTS_ID,
        OPEN_ID
    }

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    private SharedPreferences userPreferences;
    private Gson gson;
    private String json;

    private Profile userProfile;

    private Dialog depositDialog;
    private Spinner spnAccounts;
    private ArrayAdapter<Account> accountAdapter;
    private EditText edtDepositAmount;
    private Button btnCancel;
    private Button btnDeposit;

    private View.OnClickListener depositClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == btnCancel.getId()) {
                depositDialog.dismiss();
                manualNavigation(manualNavID.ACCOUNTS_ID, null);
                Toast.makeText(DrawerActivity.this, "Deposit Cancelled", Toast.LENGTH_SHORT).show();
            } else if (view.getId() == btnDeposit.getId()) {
                makeDeposit();
            }
        }
    };

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("DisplayAccountDialog", true);
                manualNavigation(manualNavID.ACCOUNTS_ID, bundle);
            }
        }
    };

    private DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("DisplayOpenAccountDialog", true);
                manualNavigation(manualNavID.OPEN_ID, bundle);
            }
        }
    };

    public void manualNavigation(manualNavID id, Bundle bundle) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == manualNavID.OPEN_ID) {
            OpenBankingFragment openBankingFragment = new OpenBankingFragment();
            if (bundle != null) {
                openBankingFragment.setArguments(bundle);
            }
            ft.replace(R.id.flContent, openBankingFragment).commit();
            navView.setCheckedItem(R.id.nav_openbankaccounts);
            setTitle("OpenBanking");
        } else if (id == manualNavID.ACCOUNTS_ID) {
            AccountOverviewFragment accountOverviewFragment = new AccountOverviewFragment();
            if (bundle != null) {
                accountOverviewFragment.setArguments(bundle);
        }
        ft.replace(R.id.flContent, accountOverviewFragment).commit();
            navView.setCheckedItem(R.id.nav_accounts);
            setTitle("Accounts");
        }

        drawerLayout.closeDrawers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        drawerLayout = findViewById(R.id.drawer_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        userPreferences = this.getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        gson = new Gson();
        json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);

        loadFromDB();

        SharedPreferences.Editor prefsEditor = userPreferences.edit();
        json = gson.toJson(userProfile);
        prefsEditor.putString("LastProfileUsed", json).apply();

        setupDrawerListener();
        setupHeader();

        //onNavigationItemSelected(navView.getMenu().getItem().); TODO: Try calling the event listener manually for navigation, get rid of the manualNav method

        manualNavigation(manualNavID.ACCOUNTS_ID, null);
    }

    //TODO: Find different way to close keyboard when opening drawer or clean this up
    private void setupDrawerListener() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    private void setupHeader() {

        View headerView = navView.getHeaderView(0);

        //ImageView imgProfilePic = findViewById(R.id.img_profile); //TODO: set the profile image
        TextView txtName = headerView.findViewById(R.id.txt_name);
        TextView txtUsername = headerView.findViewById(R.id.txt_username);

        String name = userProfile.getName();
        txtName.setText(name);

        txtUsername.setText(userProfile.getUserId());
    }

    private void loadFromDB() {
        ApplicationDB applicationDb = new ApplicationDB(getApplicationContext());

        userProfile.setPayeesFromDB(applicationDb.getPayeesFromCurrentProfile(userProfile.getDbId()));
        userProfile.setAccountsFromDB(applicationDb.getAccountsFromCurrentProfile(userProfile.getDbId()));
        userProfile.setBankAccountsFromDB(applicationDb.getBankAccountsFromCurrentProfile(userProfile.getDbId()));

        for (int iAccount = 0; iAccount < userProfile.getAccounts().size(); iAccount++) {
            userProfile.getAccounts().get(iAccount).setTransactions(applicationDb.getTransactionsFromCurrentAccount(userProfile.getDbId(), userProfile.getAccounts().get(iAccount).getAccountNo()));
        }

        for (int iAccount = 0; iAccount < userProfile.getBankAccounts().size(); iAccount++) {
            userProfile.getBankAccounts().get(iAccount).setTransactions(applicationDb.getTransactionsFromCurrentBankAccount(userProfile.getDbId(), userProfile.getBankAccounts().get(iAccount).getAccountNo()));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    public void showDrawerButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.syncState();
    }

    public void showUpButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void displayAccountAlertADialog(String option) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(String.format("%s Error", option))
                .setMessage(String.format("You do not have enough accounts to make a %s. Add another account if you want to make a %s.", option, option.toLowerCase()))
                .setNegativeButton("Cancel", dialogClickListener)
                .setPositiveButton("Add Account", dialogClickListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void displayBankAccountAlertADialog(String option) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

        builder2.setTitle(String.format("%s Error", option))
                .setMessage(String.format("You do not have enough accounts to make a %s. Add another account if you want to make a %s.", option, option.toLowerCase()))
                .setNegativeButton("Cancel", dialogClickListener2)
                .setPositiveButton("Add Bank Account", dialogClickListener2);

        AlertDialog dialog2 = builder2.create();
        dialog2.show();
    }

    private void displayDepositDialog() {

        depositDialog = new Dialog(this);
        depositDialog.setContentView(R.layout.deposit_dialog);

        depositDialog.setCanceledOnTouchOutside(true);
        depositDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                manualNavigation(manualNavID.ACCOUNTS_ID, null);
                Toast.makeText(DrawerActivity.this, "Deposit Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        spnAccounts = depositDialog.findViewById(R.id.dep_spn_accounts);
        accountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userProfile.getAccounts());
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAccounts.setAdapter(accountAdapter);
        spnAccounts.setSelection(0);

        edtDepositAmount = depositDialog.findViewById(R.id.edt_deposit_amount);

        btnCancel = depositDialog.findViewById(R.id.btn_cancel_deposit);
        btnDeposit = depositDialog.findViewById(R.id.btn_deposit);

        btnCancel.setOnClickListener(depositClickListener);
        btnDeposit.setOnClickListener(depositClickListener);

        depositDialog.show();

    }

    /**
     * method used to make a deposit
     */
    private void makeDeposit() {

        int selectedAccountIndex = spnAccounts.getSelectedItemPosition();

        double depositAmount = 0;
        boolean isNum = false;

        try {
            depositAmount = Double.parseDouble(edtDepositAmount.getText().toString());
            isNum = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (depositAmount < 0.01 && !isNum) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        } else {

            Account account = userProfile.getAccounts().get(selectedAccountIndex);
            account.addDepositTransaction(depositAmount);
            OpenBankAccount bankAccount = userProfile.getBankAccounts().get(selectedAccountIndex);
            bankAccount.addDepositTransaction(depositAmount);

            SharedPreferences.Editor prefsEditor = userPreferences.edit();
            gson = new Gson();
            json = gson.toJson(userProfile);
            prefsEditor.putString("LastProfileUsed", json).apply();

            ApplicationDB applicationDb = new ApplicationDB(getApplicationContext());
            applicationDb.overwriteAccount(userProfile, account);
            applicationDb.overwriteBankAccount(userProfile, bankAccount);
            applicationDb.saveNewTransaction(userProfile, account.getAccountNo(),
                    account.getTransactions().get(account.getTransactions().size()-1));
            applicationDb.saveNewBankTransaction(userProfile, bankAccount.getAccountNo(),
                    bankAccount.getTransactions().get(bankAccount.getTransactions().size()-1));

            Toast.makeText(this, "Deposit of $" + String.format(Locale.getDefault(), "%.2f",depositAmount) + " " + "made successfully", Toast.LENGTH_SHORT).show();

            accountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userProfile.getAccounts());
            accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnAccounts.setAdapter(accountAdapter);

            //TODO: Add checkbox if the user wants to make more than one deposit

            depositDialog.dismiss();
            drawerLayout.closeDrawers();
            manualNavigation(manualNavID.ACCOUNTS_ID, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        userPreferences = this.getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        gson = new Gson();
        json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Handle navigation view item clicks here.
        Class fragmentClass = null;
        String title = item.getTitle().toString();

        switch(item.getItemId()) {
            case R.id.nav_accounts:
                fragmentClass = AccountOverviewFragment.class;
                break;
            case R.id.nav_openbankaccounts:
                fragmentClass = OpenBankingFragment.class;
                break;
            case R.id.nav_deposit:
                if (userProfile.getAccounts().size() > 0) {
                    displayDepositDialog();
                } else {
                    displayAccountAlertADialog("Deposit");
                }
                break;
            case R.id.nav_transfer:
                if (userProfile.getAccounts().size() < 2) {
                    displayAccountAlertADialog("Transfer");
                } else {
                    title = "Transfer";
                    fragmentClass = TransferFragment.class;
                }
                break;
            case R.id.nav_openbank:
                if (userProfile.getBankAccounts().size() < 2) {
                    displayBankAccountAlertADialog("OpenBanking");
                } else {
                    title = "OpenBanking";
                    fragmentClass = BankTransferFragment.class;
                }
                break;
            case R.id.nav_payment:
                if (userProfile.getAccounts().size() < 1) {
                    displayAccountAlertADialog("Payment");
                } else {
                    title = "Payment";
                    fragmentClass = PaymentFragment.class;
                }
                break;
            case R.id.nav_utility_check:
                if (userProfile.getAccounts().size() < 1) {
                    displayAccountAlertADialog("UtilityCheck");
                } else {
                    title = "UtilityCheck";
                    fragmentClass = UtilityCheckFragment.class;
                }
                break;
            case R.id.nav_loan1:
                if (userProfile.getAccounts().size() < 1) {
                    displayAccountAlertADialog("Payment");
                } else {
                    title = "Get a Loan";
                    fragmentClass = GetLoanFragment.class;
                }
                break;
            case R.id.nav_loan2:
                if (userProfile.getAccounts().size() < 1) {
                    displayAccountAlertADialog("Payment");
                } else {
                    title = "Expand the loan period";
                    fragmentClass = PeriodFragment.class;
                }
                break;
            case R.id.nav_loan3:
                if (userProfile.getAccounts().size() < 1) {
                    displayAccountAlertADialog("Payment");
                } else {
                    title = "Pay interest";
                    fragmentClass = InterestFragment.class;
                }
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                fragmentClass = AccountOverviewFragment.class;
        }

        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            item.setChecked(true);
            setTitle(title);
            drawerLayout.closeDrawers();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


}
